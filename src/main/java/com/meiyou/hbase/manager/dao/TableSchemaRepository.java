package com.meiyou.hbase.manager.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.meiyou.hbase.manager.entity.TableSchema;

public interface TableSchemaRepository extends CrudRepository<TableSchema, Integer>{
	List<TableSchema> findAllByClusterNameAndTableName(String clusterName, String tableName);
}
