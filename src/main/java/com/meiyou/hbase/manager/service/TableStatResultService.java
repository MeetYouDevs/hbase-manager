package com.meiyou.hbase.manager.service;

import java.util.List;

import com.meiyou.hbase.manager.entity.TableStatResult;

public interface TableStatResultService {
	/**
	 * 查询表统计信息
	 * 
	 * @return
	 */
	public List<TableStatResult> listTableStatResultByClusterName(String clusterName);
	
	/**
	 * 批量添加统计信息
	 * 
	 * @return
	 */
	public void batchSave(List<TableStatResult> tableStatResultList);
}
