package com.meiyou.hbase.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiyou.hbase.manager.dao.TableStatResultRepository;
import com.meiyou.hbase.manager.entity.TableStatResult;
import com.meiyou.hbase.manager.service.TableStatResultService;

@Service
public class TableStatResultServiceImpl implements TableStatResultService {
	@Autowired
	private TableStatResultRepository tableStatResultRepository;
	
	@Override
	public List<TableStatResult> listTableStatResultByClusterName(String clusterName) {
		return tableStatResultRepository.findAllByClusterName(clusterName);
	}
	
	@Override
	public void batchSave(List<TableStatResult> tableStatResultList) {
		tableStatResultRepository.saveAll(tableStatResultList);
	}

}
