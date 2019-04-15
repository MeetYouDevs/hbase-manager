package com.meiyou.hbase.manager.service;

import java.util.List;

import com.meiyou.hbase.manager.entity.Table;

public interface TableService {
	/**
	 * 查询表列表
	 * 
	 * @return
	 */
	public List<Table> listTableByClusterName(String clusterName, boolean isContainMetrics);

	/**
	 * 查询表名列表
	 * 
	 * @return
	 */
	public List<String> listTableNameByClusterName(String clusterName);

	/**
	 * 查询表信息
	 * 
	 * @return
	 */
	public Table getTableByName(String clusterName, String tableName);

	/**
	 * 新建集群中的表
	 * 
	 * @return
	 */
	public int save(String clusterName, String ddlStr);

	/**
	 * 修改集群中的表
	 * 
	 * @return
	 */
	public int update(String clusterName, String ddlStr);

	/**
	 * 开启表
	 * 
	 * @return
	 */
	public void enableTable(String clusterName, String tableName);

	/**
	 * 关闭表
	 * 
	 * @return
	 */
	public void disableTable(String clusterName, String tableName);

	/**
	 * 删除表
	 * 
	 * @return
	 */
	public void deleteTable(String clusterName, String tableName);

	/**
	 * 合并表
	 * 
	 * @return
	 */
	public void compactTable(String clusterName, String tableName);
	
	/**
	 * 获取表大小
	 * 
	 * @return
	 */
	public Long getTableSpaceSize(String clusterName, String namespace, String tableName);

}
