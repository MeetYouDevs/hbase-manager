package com.meiyou.hbase.manager.dao;

import org.springframework.data.repository.CrudRepository;
import com.meiyou.hbase.manager.entity.Cluster;

public interface ClusterRepository extends CrudRepository<Cluster, Integer>{
	Cluster findClusterByClusterName(String clusterName);
}
