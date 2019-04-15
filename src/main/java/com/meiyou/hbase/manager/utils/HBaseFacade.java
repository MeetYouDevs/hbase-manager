package com.meiyou.hbase.manager.utils;

import java.io.IOException;
import java.util.*;

import com.meiyou.hbase.manager.entity.MySnapshotDescription;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.replication.ReplicationAdmin;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.SnapshotDescription;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.replication.ReplicationException;
import org.apache.hadoop.hbase.replication.ReplicationPeerConfig;
import org.apache.hadoop.hbase.replication.ReplicationSerDeHelper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.RegionSplitter.HexStringSplit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meiyou.hbase.manager.entity.DataCell;
import com.meiyou.hbase.manager.entity.Family;
import com.meiyou.hbase.manager.entity.Table;

public class HBaseFacade {
	private static final Logger LOGGER = LoggerFactory.getLogger(HBaseFacade.class);

	public static final String HBASE_ZK = "hbase.zookeeper.quorum";

	public static final String HBASE_ZNODE_PARENT = "zookeeper.znode.parent";

	private static final String DEFAULT_HBASE_ZNODE_PARENT = "/hbase";

	private static final String DEFAULT_USER_NAME = "hbase";

	private static final String CLIENT_RETRIES_NUMBER = "hbase.client.retries.number";
	
	private static final String ZOOKEEPER_RECOVERY_RETRY = "zookeeper.recovery.retry";

	private static final String DEFAULT_CLIENT_RETRIES_NUMBER = "3";

	private Connection connection = null;

	private ReplicationAdmin replicationAdmin;

	private String zkServers = null;

	private String znode;

	public HBaseFacade(String zkServers) {
		this(zkServers, DEFAULT_HBASE_ZNODE_PARENT);
	}

	public HBaseFacade(String zkServers, String znode) {
		this.zkServers = zkServers;
		this.znode = znode;
		init();
	}

	private void init() {
		Configuration conf = HBaseConfiguration.create();
		// zk地址
		conf.set(HBASE_ZK, this.zkServers);
		// zk根路径
		conf.set(HBASE_ZNODE_PARENT, this.znode);
		// 连接重试次数
		conf.set(CLIENT_RETRIES_NUMBER, DEFAULT_CLIENT_RETRIES_NUMBER);
		conf.set(ZOOKEEPER_RECOVERY_RETRY, DEFAULT_CLIENT_RETRIES_NUMBER);
		// 当前用户
		System.setProperty("HADOOP_USER_NAME", DEFAULT_USER_NAME);
		try {
			this.connection = ConnectionFactory.createConnection(conf);
			this.replicationAdmin = new ReplicationAdmin(conf);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// 添加钩子
		ResourceUtils.addToShutDownHook(connection);
		ResourceUtils.addToShutDownHook(replicationAdmin);
	}

	public static interface HTableHandler<T> {
		T doCall(org.apache.hadoop.hbase.client.Table table);
	}

	public <T> T handleTable(String tableName, HTableHandler<T> hTableHandle) {
		org.apache.hadoop.hbase.client.Table table = null;
		try {
			table = this.connection.getTable(TableName.valueOf(tableName));
			return hTableHandle.doCall(table);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (null != table) {
				try {
					table.close();
				} catch (IOException e) {
					LOGGER.error(e.toString(), e);
				}
			}
		}
	}

	public Map<String, DataCell> get(final String tableName, final String rowKey, final String family,
			final String qualifier) {
		return handleTable(tableName, new HTableHandler<Map<String, DataCell>>() {
			@Override
			public Map<String, DataCell> doCall(org.apache.hadoop.hbase.client.Table table) {
				HTable htable = (HTable) table;
				try {
					Get get = new Get(Bytes.toBytes(rowKey));
					if (StringUtils.isNotBlank(family)) {
						if (StringUtils.isNotBlank(qualifier)) {
							get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
						} else {
							get.addFamily(Bytes.toBytes(family));
						}
					}
					Map<String, DataCell> data = null;
					if (htable.exists(get)) {
						Result result = htable.get(get);
						List<Cell> celllist = result.listCells();
						data = new HashMap<String, DataCell>();
						for (Cell cell : celllist) {
							DataCell dataCell = new DataCell();
							dataCell.setRowKey(rowKey);
							String column = Bytes.toString(CellUtil.cloneFamily(cell)) + ":"
									+ Bytes.toString(CellUtil.cloneQualifier(cell));
							dataCell.setColumn(column);
							dataCell.setTimestamp(cell.getTimestamp());
							dataCell.setValue(Bytes.toString(CellUtil.cloneValue(cell)));
							data.put(column, dataCell);
						}
					}
					return data;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public Map<String, Map<String, DataCell>> scan(final String tableName, final int limit) {
		return scanByRowKeyPrefix(tableName, null, null, null, limit);
	}

	public Map<String, Map<String, DataCell>> scanByRowKeyPrefix(final String tableName, final String rowKeyPrefix,
			final String family, final String qualifier, final int limit) {
		return handleTable(tableName, new HTableHandler<Map<String, Map<String, DataCell>>>() {
			@Override
			public Map<String, Map<String, DataCell>> doCall(org.apache.hadoop.hbase.client.Table table) {
				HTable htable = (HTable) table;
				try {
					Scan scan = new Scan();
					if (StringUtils.isNotBlank(rowKeyPrefix)) {
						scan.setFilter(new PrefixFilter(rowKeyPrefix.getBytes()));
					}
					scan.setCacheBlocks(false);
					scan.setCaching(limit);
					ResultScanner resultScanner = htable.getScanner(scan);
					Map<String, Map<String, DataCell>> data = new HashMap<String, Map<String, DataCell>>();
					int counter = 0;
					for (Result result : resultScanner) {
						if (++counter > limit) {
							return data;
						}
						List<Cell> list = result.listCells();
						Map<String, DataCell> tmp = new HashMap<String, DataCell>();
						String rowKey = Bytes.toString(result.getRow());
						for (Cell cell : list) {
							DataCell dataCell = new DataCell();
							dataCell.setRowKey(rowKey);
							String column = Bytes.toString(CellUtil.cloneFamily(cell)) + ":"
									+ Bytes.toString(CellUtil.cloneQualifier(cell));
							dataCell.setColumn(column);
							dataCell.setTimestamp(cell.getTimestamp());
							dataCell.setValue(Bytes.toString(CellUtil.cloneValue(cell)));
							tmp.put(column, dataCell);
						}
						data.put(rowKey, tmp);
					}
					return data;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public void put(final String tableName, final Put put) {
		handleTable(tableName, new HTableHandler<Void>() {
			@Override
			public Void doCall(org.apache.hadoop.hbase.client.Table table) {
				HTable htable = (HTable) table;
				try {
					htable.put(put);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}

	public void batchPut(final String tableName, final List<Put> batchPut) {
		handleTable(tableName, new HTableHandler<Void>() {
			@Override
			public Void doCall(org.apache.hadoop.hbase.client.Table table) {
				HTable htable = (HTable) table;
				htable.setAutoFlush(false, false);
				try {
					htable.setWriteBufferSize(10 * 1024 * 1024);
					htable.put(batchPut);
					htable.flushCommits();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}

	private static interface AdminHandler<T> {
		T doCall(Admin admin);
	}

	private <T> T handleAdmin(AdminHandler<T> adminHandle) {
		Admin admin = null;
		try {
			admin = this.connection.getAdmin();
			return adminHandle.doCall(admin);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (null != admin) {
				try {
					admin.close();
				} catch (IOException e) {
					LOGGER.error(e.toString(), e);
				}
			}
		}
	}

	public String[] listNamespaces() {
		return handleAdmin(new AdminHandler<String[]>() {
			@Override
			public String[] doCall(Admin admin) {
				try {
					NamespaceDescriptor[] namespaceList = admin.listNamespaceDescriptors();
					int length = namespaceList.length;
					String[] result = new String[length];
					for (int i = 0; i < length; i++) {
						result[i] = new String(namespaceList[i].getName());
					}
					return result;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public void createNamespace(String namespace) {
		handleAdmin(new AdminHandler<Void>() {
			@Override
			public Void doCall(Admin admin) {
				try {
					admin.createNamespace(NamespaceDescriptor.create(namespace).build());
					LOGGER.info("Create namespace: {} success!", namespace);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}

	public Table getTableByName(String tableName) {
		return handleAdmin(new AdminHandler<Table>() {
			@Override
			public Table doCall(Admin admin) {
				try {
					HTableDescriptor hTableDescriptor = admin.getTableDescriptor(TableName.valueOf(tableName));
					Table table = new Table();
					table.setNamespace(hTableDescriptor.getTableName().getNamespaceAsString());
					table.setTableName(hTableDescriptor.getNameAsString());
					// table.setStatus(admin.isTableEnabled(hTableDescriptor.getTableName())==true?1:0);
					HColumnDescriptor[] familyList = hTableDescriptor.getColumnFamilies();
					List<Family> families = new ArrayList<Family>();
					for (HColumnDescriptor descriptor : familyList) {
						Family family = new Family();
						family.setFamilyName(Bytes.toString(descriptor.getName()));
						family.setTimeToLive(descriptor.getTimeToLive());
						family.setBlockSize(descriptor.getBlocksize());
						family.setCompressionType(compressionTypeExchange(descriptor.getCompressionType()));
						family.setBlockCacheEnabled(descriptor.isBlockCacheEnabled() == true ? 1 : 0);
						family.setReplicationScope(descriptor.getScope());
						family.setMinVersion(descriptor.getMinVersions());
						family.setMaxVersion(descriptor.getMaxVersions());
						family.setBloomFilterType(bloomFilterTypeExchange(descriptor.getBloomFilterType()));
						families.add(family);
					}
					table.setFamilies(families);
					table.setDesc(JsonUtils.toJSON(families));
					return table;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public List<Table> listTables() {
		return handleAdmin(new AdminHandler<List<Table>>() {
			@Override
			public List<Table> doCall(Admin admin) {
				try {
					HTableDescriptor[] tableList = admin.listTables();
					List<Table> result = new ArrayList<Table>(tableList.length);
					for (HTableDescriptor hTableDescriptor : tableList) {
						Table table = new Table();
						table.setNamespace(hTableDescriptor.getTableName().getNamespaceAsString());
						table.setTableName(hTableDescriptor.getNameAsString());
						// table.setStatus(admin.isTableEnabled(hTableDescriptor.getTableName())==true?1:0);
						HColumnDescriptor[] familyList = hTableDescriptor.getColumnFamilies();
						List<Family> families = new ArrayList<Family>();
						List<String> familyNames = new ArrayList<String>();
						for (HColumnDescriptor descriptor : familyList) {
							Family family = new Family();
							String familyName = Bytes.toString(descriptor.getName());
							familyNames.add(familyName);
							family.setFamilyName(familyName);
							family.setTimeToLive(descriptor.getTimeToLive());
							family.setBlockSize(descriptor.getBlocksize());
							family.setCompressionType(compressionTypeExchange(descriptor.getCompressionType()));
							family.setBlockCacheEnabled(descriptor.isBlockCacheEnabled() == true ? 1 : 0);
							family.setReplicationScope(descriptor.getScope());
							family.setMinVersion(descriptor.getMinVersions());
							family.setMaxVersion(descriptor.getMaxVersions());
							family.setBloomFilterType(bloomFilterTypeExchange(descriptor.getBloomFilterType()));
							families.add(family);
						}
						table.setFamilies(families);
						// table.setDesc(hTableDescriptor.toString());
						table.setDesc(StringUtils.join(familyNames, ", "));
						result.add(table);
					}
					return result;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public List<String> listTableNames() {
		return handleAdmin(new AdminHandler<List<String>>() {
			@Override
			public List<String> doCall(Admin admin) {
				try {
					TableName[] tableNames = admin.listTableNames();
					List<String> result = new ArrayList<String>(tableNames.length);
					for (TableName tableName : tableNames) {
						result.add(Bytes.toString(tableName.getName()));
					}
					return result;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public Integer getTableCount() {
		return handleAdmin(new AdminHandler<Integer>() {
			@Override
			public Integer doCall(Admin admin) {
				try {
					TableName[] tableList = admin.listTableNames();
					return tableList.length;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public void createTable(Table table) {
		handleAdmin(new AdminHandler<Void>() {
			@Override
			public Void doCall(Admin admin) {
				try {
					TableName tableName = TableName.valueOf(table.getTableName());
					if (admin.tableExists(tableName)) {
						LOGGER.warn("table:{} exists!", tableName.getName());
					} else {
						HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
						List<Family> families = table.getFamilies();
						for (Family family : families) {
							HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(family.getFamilyName());
							if (null != family.getTimeToLive()) {
								hColumnDescriptor.setTimeToLive(family.getTimeToLive());
							}
							if (null != family.getBlockSize()) {
								hColumnDescriptor.setBlocksize(family.getBlockSize());
							}
							if (null != family.getCompressionType()) {
								hColumnDescriptor
										.setCompressionType(compressionTypeExchange(family.getCompressionType()));
							}
							if (null != family.getBlockCacheEnabled()) {
								hColumnDescriptor
										.setBlockCacheEnabled(family.getBlockCacheEnabled() == 1 ? true : false);
							}
							if (null != family.getReplicationScope()) {
								hColumnDescriptor.setScope(family.getReplicationScope());
							}
							if (null != family.getMinVersion()) {
								hColumnDescriptor.setMinVersions(family.getMinVersion());
							}
							if (null != family.getMaxVersion()) {
								hColumnDescriptor.setMaxVersions(family.getMaxVersion());
							}
							if (null != family.getBloomFilterType()) {
								hColumnDescriptor
										.setBloomFilterType(bloomFilterTypeExchange(family.getBloomFilterType()));
							}
							tableDescriptor.addFamily(hColumnDescriptor);
						}
						// 预分区
						if (table.getNumRegions() != 1) {
							HexStringSplit hexStringSplit = new HexStringSplit();
							byte[][] splitKeys = hexStringSplit.split(table.getNumRegions());
							admin.createTable(tableDescriptor, splitKeys);
						} else {
							admin.createTable(tableDescriptor);
						}

						LOGGER.info("Create table: {} success!", tableName.getName());
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}

	private Compression.Algorithm compressionTypeExchange(Integer compressionType) {
		switch (compressionType) {
		case 1:
			return Compression.Algorithm.SNAPPY;
		case 2:
			return Compression.Algorithm.LZO;
		case 3:
			return Compression.Algorithm.GZ;
		case 4:
			return Compression.Algorithm.LZ4;
		case 0:
			return Compression.Algorithm.NONE;
		default:
			break;
		}
		return null;
	}

	private Integer compressionTypeExchange(Compression.Algorithm compressionType) {
		switch (compressionType) {
		case SNAPPY:
			return 1;
		case LZO:
			return 2;
		case GZ:
			return 3;
		case LZ4:
			return 4;
		case NONE:
			return 0;
		default:
			break;
		}
		return null;
	}

	private BloomType bloomFilterTypeExchange(Integer bloomFilterType) {
		switch (bloomFilterType) {
		case 1:
			return BloomType.ROW;
		case 2:
			return BloomType.ROWCOL;
		case 0:
			return BloomType.NONE;
		default:
			break;
		}
		return null;
	}

	private Integer bloomFilterTypeExchange(BloomType bloomFilterType) {
		switch (bloomFilterType) {
		case ROW:
			return 1;
		case ROWCOL:
			return 2;
		case NONE:
			return 0;
		default:
			break;
		}
		return null;
	}

	public void alterTable(Table table) {
		handleAdmin(new AdminHandler<Void>() {
			@Override
			public Void doCall(Admin admin) {
				try {
					TableName tableName = TableName.valueOf(table.getTableName());
					if (!admin.tableExists(tableName)) {
						LOGGER.warn("table:{} do not exists!", tableName.getName());
					} else {
						HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
						List<Family> families = table.getFamilies();
						for (Family family : families) {
							HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(family.getFamilyName());
							if (null != family.getTimeToLive()) {
								hColumnDescriptor.setTimeToLive(family.getTimeToLive());
							}
							if (null != family.getBlockSize()) {
								hColumnDescriptor.setBlocksize(family.getBlockSize());
							}
							if (null != family.getCompressionType()) {
								hColumnDescriptor
										.setCompressionType(compressionTypeExchange(family.getCompressionType()));
							}
							if (null != family.getBlockCacheEnabled()) {
								hColumnDescriptor
										.setBlockCacheEnabled(family.getBlockCacheEnabled() == 1 ? true : false);
							}
							if (null != family.getReplicationScope()) {
								hColumnDescriptor.setScope(family.getReplicationScope());
							}
							if (null != family.getMinVersion()) {
								hColumnDescriptor.setMinVersions(family.getMinVersion());
							}
							if (null != family.getMaxVersion()) {
								hColumnDescriptor.setMaxVersions(family.getMaxVersion());
							}
							if (null != family.getBloomFilterType()) {
								hColumnDescriptor
										.setBloomFilterType(bloomFilterTypeExchange(family.getBloomFilterType()));
							}
							tableDescriptor.addFamily(hColumnDescriptor);
						}
						admin.modifyTable(tableName, tableDescriptor);
						LOGGER.info("Alter table: {} success!", tableName.getName());
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}

	public void operateTable(String tableName, HTableOpEnum hTableOpEnum) {
		handleAdmin(new AdminHandler<Void>() {
			@Override
			public Void doCall(Admin admin) {
				try {
					TableName table = TableName.valueOf(tableName);
					if (!admin.tableExists(table)) {
						LOGGER.warn("Table: {} not exists!", table.getName());
						return null;
					}
					switch (hTableOpEnum) {
					case ENABLE:
						if (!admin.isTableEnabled(table)) {
							admin.enableTable(table);
						}
						break;
					case DISABLE:
						if (admin.isTableEnabled(table)) {
							admin.disableTable(table);
						}
						break;
					case DELETE:
						if (admin.isTableEnabled(table)) {
							admin.disableTable(table);
						}
						admin.deleteTable(table);
						break;
					case COMPACT:
						admin.compact(table);
						break;
					case MAJOR_COMPACT:
						admin.majorCompact(table);
						break;
					case SPLIT:
						admin.split(table);
						break;
					case FLUSH:
						admin.flush(table);
						break;
					default:
						break;
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}

	public enum HTableOpEnum {
		ENABLE, DISABLE, DELETE, COMPACT, MAJOR_COMPACT, SPLIT, FLUSH;
	}

	public String[] listSnapshots() {
		return handleAdmin(new AdminHandler<String[]>() {
			@Override
			public String[] doCall(Admin admin) {
				try {
					List<SnapshotDescription> snapshotList = admin.listSnapshots();
					int size = snapshotList.size();
					String[] result = new String[size];
					for (int i = 0; i < size; i++) {
						result[i] = new String(snapshotList.get(i).getName());
					}
					return result;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public List<MySnapshotDescription> listSnapshotsDesc() {
		return handleAdmin(new AdminHandler<List<MySnapshotDescription>>() {
			@Override
			public List<MySnapshotDescription> doCall(Admin admin) {
				try {
					List<MySnapshotDescription> descList = new ArrayList<>();
					List<SnapshotDescription> snapshotList = admin.listSnapshots();
					for (SnapshotDescription s : snapshotList) {
						MySnapshotDescription desc = new MySnapshotDescription();
						desc.setName(s.getName());
						desc.setTableName(s.getTable());
						desc.setCreateTime(new Date(s.getCreationTime()));
						descList.add(desc);
					}
					return descList;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public void operateTableSnapshot(String snapshotName, String tableName, HTableSnapshotOpEnum hTableSnapshotOpEnum) {
		handleAdmin(new AdminHandler<Void>() {
			@Override
			public Void doCall(Admin admin) {
				try {

					if (HTableSnapshotOpEnum.DELETE_SNAPSHOT == hTableSnapshotOpEnum) {
						admin.deleteSnapshot(snapshotName);
						return null;
					}

					TableName table = TableName.valueOf(tableName);
					boolean tableExists = admin.tableExists(table);
					if (!tableExists) {
						if (HTableSnapshotOpEnum.CREATE_SNAPSHOT == hTableSnapshotOpEnum
								|| HTableSnapshotOpEnum.RESTORE_SNAPSHOT == hTableSnapshotOpEnum) {
							LOGGER.warn("Table: {} not exists!", table.getName());
							return null;
						}
					} else {
						if (HTableSnapshotOpEnum.CLONE_SNAPSHOT == hTableSnapshotOpEnum) {
							LOGGER.warn("Table: {} exists!", table.getName());
							return null;
						}
					}
					switch (hTableSnapshotOpEnum) {
					case CREATE_SNAPSHOT:
						admin.snapshot(snapshotName, table);
						break;
					case CLONE_SNAPSHOT:
						admin.cloneSnapshot(snapshotName, table);
						break;
					// case DELETE_SNAPSHOT:
					// admin.deleteSnapshot(snapshotName);
					// break;
					case RESTORE_SNAPSHOT:
						admin.disableTable(table);
						admin.restoreSnapshot(snapshotName);
						break;
					default:
						break;
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}

	public enum HTableSnapshotOpEnum {
		CREATE_SNAPSHOT, CLONE_SNAPSHOT, DELETE_SNAPSHOT, RESTORE_SNAPSHOT;
	}

	public void addPeer(String id, String clusterKey) {
		this.addPeer(id, clusterKey, null);
	}

	public void addPeer(String id, String clusterKey, String tableCFs) {
		try {
			replicationAdmin.addPeer(id, new ReplicationPeerConfig().setClusterKey(clusterKey),
					ReplicationSerDeHelper.parseTableCFsFromConfig(tableCFs));
		} catch (ReplicationException e) {
			LOGGER.error(e.toString(), e);
		}
	}

	public void operateTableReplicationPeer(String id, HTableReplicationPeerOpEnum hTableReplicationPeerOpEnum) {
		try {
			switch (hTableReplicationPeerOpEnum) {
			case ENABLE_PEER:
				replicationAdmin.enablePeer(id);
				break;
			case DISABLE_PEER:
				replicationAdmin.disablePeer(id);
				break;
			case DELETE_PEER:
				replicationAdmin.removePeer(id);
				break;
			default:
				break;
			}
		} catch (ReplicationException e) {
			throw new RuntimeException(e);
		}
	}

	public enum HTableReplicationPeerOpEnum {
		ENABLE_PEER, DISABLE_PEER, DELETE_PEER;
	}

}
