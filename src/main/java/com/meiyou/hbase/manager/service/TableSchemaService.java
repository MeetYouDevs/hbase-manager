package com.meiyou.hbase.manager.service;

import com.meiyou.hbase.manager.entity.TableSchema;

import java.util.List;

public interface TableSchemaService {
    public void saveOrUpdate(TableSchema schema);
    public List<TableSchema> get(String clusterName, String tableName);
    public void deleteById(Integer id);
}
