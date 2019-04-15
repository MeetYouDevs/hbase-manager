package com.meiyou.hbase.manager.service;

import java.util.List;

import com.meiyou.hbase.manager.entity.TableStatTask;

public interface TableStatTaskService {
	/**
	 * 查询统计任务列表
	 * 
	 * @return
	 */
	public List<TableStatTask> listAllStatTask();

	/**
	 * 查询集群统计任务列表
	 * 
	 * @return
	 */
	public List<TableStatTask> listStatTask(String clusterName);

	/**
	 * 查询统计任务信息
	 * 
	 * @return
	 */
	public TableStatTask findById(Integer id);

	/**
	 * 添加表统计任务
	 * 
	 * @return
	 */
	public void addStatTask(Integer id, String clusterName, String tableName, Integer intervalPosition, Integer interval,
			Integer offsetPosition, Integer offset);

	/**
	 * 删除表统计任务
	 * 
	 * @return
	 */
	public void deleteById(Integer id);
	
	/**
	 * 执行表统计任务
	 * 
	 * @return
	 */
	public void executeStatTask(String clusterName, String tableName);
	
	/**
	 * 部署任务
	 * @param task
	 */
	public void deployStat(TableStatTask task);
	
	/**
	 * 取消任务部署
	 * @param task
	 */
	public void unDeployStat(Integer id);
	
}
