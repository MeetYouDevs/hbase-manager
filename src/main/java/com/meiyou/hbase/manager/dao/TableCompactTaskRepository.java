package com.meiyou.hbase.manager.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.meiyou.hbase.manager.entity.TableCompactTask;

public interface TableCompactTaskRepository extends CrudRepository<TableCompactTask, Integer>{
	List<TableCompactTask> findAllByClusterName(String clusterName);
}
