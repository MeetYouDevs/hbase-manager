package com.meiyou.hbase.manager.dao;

import org.springframework.data.repository.CrudRepository;

import com.meiyou.hbase.manager.entity.RunCluster;

public interface RunClusterRepository extends CrudRepository<RunCluster, Integer>{
	RunCluster findByClusterName(String clusterName);
}
