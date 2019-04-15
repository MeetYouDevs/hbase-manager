package com.meiyou.hbase.manager.service;

import java.util.List;

import com.meiyou.hbase.manager.entity.RunCluster;

public interface RunClusterService {
	public List<RunCluster> listRunCluster();

	public void addRunCluster(RunCluster runCluster);

	public void deleteRunCluster(Integer id);
}
