package com.meiyou.hbase.manager.service;

import java.util.List;

import com.meiyou.hbase.manager.entity.TableReplicationTask;

public interface TableReplicationTaskService {
	/**
	 * 查询表同步任务列表
	 * 
	 * @return
	 */
	public List<TableReplicationTask> listReplicationTask();

	/**
	 * 添加表同步任务
	 * 
	 * @return
	 */
	public void addReplicationTask(String sourceCluster, String sourceTable, String targetCluster, String targetTable);

	/**
	 * 删除表同步任务
	 * 
	 * @return
	 */
	public void deleteReplicationTask(Integer id);
	
}
