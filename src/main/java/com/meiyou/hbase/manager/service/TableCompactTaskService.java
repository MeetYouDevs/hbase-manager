package com.meiyou.hbase.manager.service;

import java.util.List;

import com.meiyou.hbase.manager.entity.TableCompactTask;

public interface TableCompactTaskService {
	/**
	 * 查询集群合并任务列表
	 * 
	 * @return
	 */
	public List<TableCompactTask> listCompactTask(String clusterName);

	/**
	 * 查询合并任务信息
	 * 
	 * @return
	 */
	public TableCompactTask findById(Integer id);

	/**
	 * 添加表合并任务
	 * 
	 * @return
	 */
	public void addCompactTask(Integer id, String clusterName, String tableName, Integer intervalPosition,
			Integer interval, Integer offsetPosition, Integer offset);

	/**
	 * 删除表合并任务
	 * 
	 * @return
	 */
	public void deleteById(Integer id);
	
	/**
	 * 部署任务
	 * @param task
	 */
	public void deployCompact(TableCompactTask task);
	
	/**
	 * 取消任务部署
	 * @param task
	 */
	public void unDeployCompact(Integer id);

}
