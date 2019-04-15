package com.meiyou.hbase.manager.service;

import java.util.List;

import com.meiyou.hbase.manager.entity.Cluster;

public interface ClusterService {
	/**
	 * 查询集群列表
	 * 
	 * @return
	 */
	public List<Cluster> listCluster();

	/**
	 * 查询集群信息
	 * 
	 * @return
	 */
	public Cluster getClusterByName(String clusterName);

	/**
	 * 保存|修改集群信息
	 * 
	 * @param cluster
	 */
	public void save(Cluster cluster);

	/**
	 * 删除集群信息
	 * 
	 * @return
	 */
	public void deleteById(Integer id);
}
