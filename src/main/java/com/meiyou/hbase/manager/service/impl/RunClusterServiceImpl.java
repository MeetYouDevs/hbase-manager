package com.meiyou.hbase.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.meiyou.hbase.manager.dao.RunClusterRepository;
import com.meiyou.hbase.manager.entity.RunCluster;
import com.meiyou.hbase.manager.service.RunClusterService;

@Service
public class RunClusterServiceImpl implements RunClusterService {
	@Resource
	private RunClusterRepository runClusterRepository;

	@Override
	public List<RunCluster> listRunCluster() {
		List<RunCluster> runClusterList = new ArrayList<RunCluster>();
		runClusterRepository.findAll().forEach(runClusterList::add);
		return runClusterList;
	}

	@Override
	public void addRunCluster(RunCluster runCluster) {
		runClusterRepository.save(runCluster);
	}

	@Override
	public void deleteRunCluster(Integer id) {
		runClusterRepository.deleteById(id);
	}

}
