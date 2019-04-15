package com.meiyou.hbase.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiyou.hbase.manager.constant.Constant;
import com.meiyou.hbase.manager.entity.Cluster;
import com.meiyou.hbase.manager.entity.Table;
import com.meiyou.hbase.manager.service.ClusterService;
import com.meiyou.hbase.manager.service.TableService;
import com.meiyou.hbase.manager.utils.HBaseFacade;
import com.meiyou.hbase.manager.utils.HBaseFacadeFactory;
import com.meiyou.hbase.manager.utils.HDFSFacade;
import com.meiyou.hbase.manager.utils.HDFSFacadeFactory;
import com.meiyou.hbase.manager.utils.JsonUtils;
import com.meiyou.hbase.manager.utils.HBaseFacade.HTableOpEnum;

@Service
public class TableServiceImpl implements TableService {
	@Autowired
	private ClusterService clusterService;
	
	@Override
	public List<Table> listTableByClusterName(String clusterName, boolean isContainMetrics) {
		Cluster cluster = clusterService.getClusterByName(clusterName);
		HBaseFacade hbaseFacade = HBaseFacadeFactory.createAdministrator(cluster.getClusterName(),
				cluster.getZookeeperAddress(), cluster.getZnodeParent());
		List<Table> tableList = hbaseFacade.listTables();
		String hdfsRootpath = cluster.getHdfsRootPath();
		HDFSFacade hdfsFacade = HDFSFacadeFactory.createHDFSFacade(hdfsRootpath);
		String tableDataPath = hdfsRootpath + "/data/";
		// 包含统计信息
		if(isContainMetrics) {
			for (Table table : tableList) {
				table.setSpaceSize(
						hdfsFacade.getPathSpaceSize(tableDataPath + table.getNamespace() + "/" + table.getTableName()));
			}
		}
		return tableList;
	}

	@Override
	public List<String> listTableNameByClusterName(String clusterName) {
		Cluster cluster = clusterService.getClusterByName(clusterName);
		HBaseFacade hbaseFacade = HBaseFacadeFactory.createAdministrator(cluster.getClusterName(),
				cluster.getZookeeperAddress(), cluster.getZnodeParent());
		return hbaseFacade.listTableNames();
	}

	@Override
	public Table getTableByName(String clusterName, String tableName) {
		Cluster cluster = clusterService.getClusterByName(clusterName);
		HBaseFacade hbaseFacade = HBaseFacadeFactory.createAdministrator(clusterName, cluster.getZookeeperAddress(),
				cluster.getZnodeParent());
		Table table = hbaseFacade.getTableByName(tableName);
		return table;
	}

	@Override
	public int save(String clusterName, String ddlStr) {
		Table table = JsonUtils.getJsonVal(ddlStr, Constant.TABLE_TYPE);
		if (null != table) {
			Cluster cluster = clusterService.getClusterByName(clusterName);
			HBaseFacade hbaseFacade = HBaseFacadeFactory.createAdministrator(clusterName, cluster.getZookeeperAddress(),
					cluster.getZnodeParent());
			hbaseFacade.createTable(table);
		}
		return 0;
	}

	@Override
	public int update(String clusterName, String ddlStr) {
		Table table = JsonUtils.getJsonVal(ddlStr, Constant.TABLE_TYPE);
		if (null != table) {
			Cluster cluster = clusterService.getClusterByName(clusterName);
			HBaseFacade hbaseFacade = HBaseFacadeFactory.createAdministrator(clusterName, cluster.getZookeeperAddress(),
					cluster.getZnodeParent());
			hbaseFacade.alterTable(table);
		}
		return 0;
	}

	@Override
	public void enableTable(String clusterName, String tableName) {
		Cluster cluster = clusterService.getClusterByName(clusterName);
		HBaseFacade hbaseFacade = HBaseFacadeFactory.createAdministrator(clusterName, cluster.getZookeeperAddress(),
				cluster.getZnodeParent());
		hbaseFacade.operateTable(tableName, HTableOpEnum.ENABLE);
	}

	@Override
	public void disableTable(String clusterName, String tableName) {
		Cluster cluster = clusterService.getClusterByName(clusterName);
		HBaseFacade hbaseFacade = HBaseFacadeFactory.createAdministrator(clusterName, cluster.getZookeeperAddress(),
				cluster.getZnodeParent());
		hbaseFacade.operateTable(tableName, HTableOpEnum.DISABLE);
	}

	@Override
	public void deleteTable(String clusterName, String tableName) {
		Cluster cluster = clusterService.getClusterByName(clusterName);
		HBaseFacade hbaseFacade = HBaseFacadeFactory.createAdministrator(clusterName, cluster.getZookeeperAddress(),
				cluster.getZnodeParent());
		hbaseFacade.operateTable(tableName, HTableOpEnum.DELETE);
	}

	@Override
	public void compactTable(String clusterName, String tableName) {
		Cluster cluster = clusterService.getClusterByName(clusterName);
		HBaseFacade hbaseFacade = HBaseFacadeFactory.createAdministrator(clusterName, cluster.getZookeeperAddress(),
				cluster.getZnodeParent());
		hbaseFacade.operateTable(tableName, HTableOpEnum.COMPACT);
	}

	@Override
	public Long getTableSpaceSize(String clusterName, String namespace, String tableName) {
		Cluster cluster = clusterService.getClusterByName(clusterName);
		String hdfsRootpath = cluster.getHdfsRootPath();
		HDFSFacade hdfsFacade = HDFSFacadeFactory.createHDFSFacade(hdfsRootpath);
		String tableDataPath = new StringBuilder(hdfsRootpath).append("/data/").append(namespace).append("/").append(tableName).toString();
		long spaceSize = hdfsFacade.getPathSpaceSize(tableDataPath);
		return spaceSize;
	}

}
