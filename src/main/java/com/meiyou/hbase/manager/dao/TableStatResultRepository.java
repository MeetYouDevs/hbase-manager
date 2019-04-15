package com.meiyou.hbase.manager.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.meiyou.hbase.manager.entity.TableStatResult;

public interface TableStatResultRepository extends CrudRepository<TableStatResult, Integer>{
	List<TableStatResult> findAllByClusterName(String clusterName);
}
