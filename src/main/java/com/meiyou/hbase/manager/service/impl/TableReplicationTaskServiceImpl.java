package com.meiyou.hbase.manager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.meiyou.hbase.manager.dao.TableReplicationTaskRepository;
import com.meiyou.hbase.manager.entity.Cluster;
import com.meiyou.hbase.manager.entity.TableReplicationTask;
import com.meiyou.hbase.manager.service.ClusterService;
import com.meiyou.hbase.manager.service.TableReplicationTaskService;
import com.meiyou.hbase.manager.utils.HBaseFacade;
import com.meiyou.hbase.manager.utils.HBaseFacade.HTableReplicationPeerOpEnum;
import com.meiyou.hbase.manager.utils.HBaseFacadeFactory;

@Service
public class TableReplicationTaskServiceImpl implements TableReplicationTaskService {
	@Autowired
	private ClusterService clusterService;

	@Autowired
	private TableReplicationTaskRepository tableReplicationTaskRepository;

	@Override
	public List<TableReplicationTask> listReplicationTask() {
		List<TableReplicationTask> tableReplicationTaskList = new ArrayList<TableReplicationTask>();
		tableReplicationTaskRepository.findAll().forEach(tableReplicationTaskList::add);
		return tableReplicationTaskList;
	}

	@Override
	public void addReplicationTask(String sourceCluster, String sourceTable, String targetCluster, String targetTable) {
		// 存在性校验, 避免表与表之间互相同步
		List<TableReplicationTask> tableReplicationTaskList = tableReplicationTaskRepository
				.findAllBySourceTable(sourceTable);
		if (!CollectionUtils.isEmpty(tableReplicationTaskList)) {
			for (TableReplicationTask tmp : tableReplicationTaskList) {
				String sourceClusterTmp = tmp.getSourceCluster();
				String targetClusterTmp = tmp.getTargetCluster();
				if (sourceClusterTmp.equals(sourceCluster) && targetClusterTmp.equals(targetCluster)) {
					return;
				}
				if (sourceClusterTmp.equals(targetCluster) && targetClusterTmp.equals(sourceCluster)) {
					return;
				}
			}
		}
		// 源集群
		Cluster source = clusterService.getClusterByName(sourceCluster);
		// 目标集群
		Cluster target = clusterService.getClusterByName(targetCluster);
		HBaseFacade sourceFacade = HBaseFacadeFactory.createAdministrator(source.getClusterName(),
				source.getZookeeperAddress(), source.getZnodeParent());
		// 随机生成peerId
		String peerId = UUID.randomUUID().toString().replaceAll("-", "");
		// 源集群添加复制连接
		sourceFacade.addPeer(peerId, target.getZookeeperAddress() + ":" + target.getZnodeParent(), targetTable);
		TableReplicationTask tableReplicationTask = new TableReplicationTask(null, sourceCluster, sourceTable,
				targetCluster, targetTable, peerId, new Date());
		tableReplicationTaskRepository.save(tableReplicationTask);
	}

	@Override
	public void deleteReplicationTask(Integer id) {
		TableReplicationTask tableReplicationTask = tableReplicationTaskRepository.findById(id).get();
		Cluster source = clusterService.getClusterByName(tableReplicationTask.getSourceCluster());
		HBaseFacade sourceFacade = HBaseFacadeFactory.createAdministrator(source.getClusterName(),
				source.getZookeeperAddress(), source.getZnodeParent());
		// 源集群移除复制连接
		sourceFacade.operateTableReplicationPeer(tableReplicationTask.getPeerId(),
				HTableReplicationPeerOpEnum.DELETE_PEER);
		tableReplicationTaskRepository.deleteById(id);
	}

}
