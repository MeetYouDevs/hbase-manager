package com.meiyou.hbase.manager.service;

import com.meiyou.hbase.manager.entity.DataCell;

import java.util.Map;

public interface HTableDataService {
    Map<String,DataCell> get(String clusterName, String tableName, final String rowKey);
    Map<String, Map<String, DataCell>> findByKeyPrefix(String clusterName, String tableName, final String rowKeyPrefix, int limit);
    Map<String, Map<String, DataCell>> scan(String clusterName, String tableName,int limit);
}
