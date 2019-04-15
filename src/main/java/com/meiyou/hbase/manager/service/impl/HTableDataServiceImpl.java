package com.meiyou.hbase.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.dao.TableSchemaRepository;
import com.meiyou.hbase.manager.entity.Cluster;
import com.meiyou.hbase.manager.entity.DataCell;
import com.meiyou.hbase.manager.entity.TableSchema;
import com.meiyou.hbase.manager.service.ClusterService;
import com.meiyou.hbase.manager.service.HTableDataService;
import com.meiyou.hbase.manager.utils.HBaseFacade;
import com.meiyou.hbase.manager.utils.HBaseFacadeFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HTableDataServiceImpl implements HTableDataService {

	private static final Logger logger = LoggerFactory.getLogger(HTableDataServiceImpl.class);

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private TableSchemaRepository tableSchemaRepository;

	@Override
	public Map<String, DataCell> get(String clusterName, String tableName, String rowKey) {

		// 获取集群信息
		Cluster cluster = clusterService.getClusterByName(clusterName);

		HBaseFacade administrator = HBaseFacadeFactory.createAdministrator(cluster.getClusterName(),
				cluster.getZookeeperAddress(), cluster.getZnodeParent());

		Map<String, DataCell> data = administrator.get(tableName, rowKey, null, null);

		List<TableSchema> schemas = tableSchemaRepository.findAllByClusterNameAndTableName(clusterName, tableName);

		logger.info("before convert : " + JSON.toJSONString(data));
		data = covertOneDataType(data, schemas);
		logger.info("after convert : " + JSON.toJSONString(data));

		return data;
	}

	@Override
	public Map<String, Map<String, DataCell>> findByKeyPrefix(String clusterName, String tableName, String rowKeyPrefix,
			int limit) {

		// 获取集群信息
		Cluster cluster = clusterService.getClusterByName(clusterName);

		HBaseFacade administrator = HBaseFacadeFactory.createAdministrator(cluster.getClusterName(),
				cluster.getZookeeperAddress(), cluster.getZnodeParent());
		Map<String, Map<String, DataCell>> data = administrator.scanByRowKeyPrefix(tableName, rowKeyPrefix, "", "",
				limit);

		data = covertDataType(clusterName, tableName, data);

		return data;
	}

	@Override
	public Map<String, Map<String, DataCell>> scan(String clusterName, String tableName, int limit) {

		Cluster cluster = clusterService.getClusterByName(clusterName);

		HBaseFacade administrator = HBaseFacadeFactory.createAdministrator(cluster.getClusterName(),
				cluster.getZookeeperAddress(), cluster.getZnodeParent());

		Map<String, Map<String, DataCell>> data = administrator.scan(tableName, limit);
		data = covertDataType(clusterName, tableName, data);
		return data;
	}

	private Map<String, Map<String, DataCell>> covertDataType(String clusterName, String tableName,
			Map<String, Map<String, DataCell>> dataCellMap) {
		// 数据类型转换
		List<TableSchema> schemas = tableSchemaRepository.findAllByClusterNameAndTableName(clusterName, tableName);
		for (Map.Entry<String, Map<String, DataCell>> entry : dataCellMap.entrySet()) {
			Map<String, DataCell> oneData = entry.getValue();
			oneData = covertOneDataType(oneData, schemas);
			dataCellMap.put(entry.getKey(), oneData);
		}
		return dataCellMap;
	}

	private Map<String, DataCell> covertOneDataType(Map<String, DataCell> dataCellMap, List<TableSchema> schemaList) {
		// 遍历数据字段
		for (Map.Entry<String, DataCell> entry : dataCellMap.entrySet()) {
			String dataColName = entry.getKey();
			DataCell dataCell = entry.getValue();

			for (TableSchema schema : schemaList) {
				int nameType = schema.getNameType();
				if (isSameColumn(nameType, schema.getColumnName(), dataColName)) {
					String dataType = schema.getDataType();
					if (dataType.equals("string")) {
						// nothing to do
					} else if (dataType.equals("long")) {
						// 取出value，转换之后set回去
						DataCell data = dataCell;
						Long value = Bytes.toLong(data.getValue().getBytes());
						data.setValue(String.valueOf(value));
						dataCellMap.put(dataColName, data);
					}
				}
			}

		}

		return dataCellMap;
	}

	public boolean isSameColumn(int nameType, String schemaColName, String dataColName) {
		if (nameType == 0) {
			if (dataColName.equals(schemaColName)) {
				return true;
			}
		} else if (nameType == 1) {
			if (dataColName.startsWith(schemaColName)) {
				return true;
			}
		}
		return false;
	}

	public Map<String, TableSchema> getSchemaMap(String clusterName, String tableName) {
		List<TableSchema> schemas = tableSchemaRepository.findAllByClusterNameAndTableName(clusterName, tableName);
		Map<String, TableSchema> colsTypeMap = new HashMap<>();
		for (TableSchema s : schemas) {
			colsTypeMap.put(s.getColumnName(), s);
		}
		return colsTypeMap;
	}
}
