package com.meiyou.hbase.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiyou.hbase.manager.dao.ClusterRepository;
import com.meiyou.hbase.manager.entity.Cluster;
import com.meiyou.hbase.manager.service.ClusterService;
import com.meiyou.hbase.manager.utils.HBaseFacade;
import com.meiyou.hbase.manager.utils.HBaseFacadeFactory;

@Service
public class ClusterServiceImpl implements ClusterService {
	@Autowired
	private ClusterRepository clusterRepository;

	@Override
	public List<Cluster> listCluster() {
		List<Cluster> clusterList = new ArrayList<Cluster>();
		clusterRepository.findAll().forEach(clusterList::add);
		return clusterList;
	}

	@Override
	public Cluster getClusterByName(String clusterName) {
		Cluster cluster = clusterRepository.findClusterByClusterName(clusterName);
		HBaseFacade hbaseFacade = null;
		Integer tableCount = 0;
		try {
			hbaseFacade = HBaseFacadeFactory.createAdministrator(clusterName, cluster.getZookeeperAddress(),
					cluster.getZnodeParent());
			tableCount = hbaseFacade.getTableCount();
		} catch (Exception e) {
		}
		cluster.setTableCount(tableCount);
		return cluster;
	}

	@Override
	public void save(Cluster cluster) {
		clusterRepository.save(cluster);
	}

	@Override
	public void deleteById(Integer id) {
		clusterRepository.deleteById(id);
	}
}
