package com.meiyou.hbase.manager.service.impl;

import com.meiyou.hbase.manager.dao.TableSchemaRepository;
import com.meiyou.hbase.manager.entity.TableSchema;
import com.meiyou.hbase.manager.service.TableSchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableSchemaServiceImpl implements TableSchemaService {

	@Autowired
	private TableSchemaRepository tableSchemaRepository;

	@Override
	public void saveOrUpdate(TableSchema schema) {
		tableSchemaRepository.save(schema);
	}

	@Override
	public List<TableSchema> get(String clusterName, String tableName) {
		return tableSchemaRepository.findAllByClusterNameAndTableName(clusterName, tableName);
	}

	@Override
	public void deleteById(Integer id) {
		tableSchemaRepository.deleteById(id);
	}
}
