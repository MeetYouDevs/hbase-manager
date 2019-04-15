package com.meiyou.hbase.manager.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.meiyou.hbase.manager.entity.TableReplicationTask;

public interface TableReplicationTaskRepository extends CrudRepository<TableReplicationTask, Integer>{
	List<TableReplicationTask> findAllBySourceTable(String sourceTable);
}
