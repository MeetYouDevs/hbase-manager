package com.meiyou.hbase.manager.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.meiyou.hbase.manager.entity.TableStatTask;

public interface TableStatTaskRepository extends CrudRepository<TableStatTask, Integer> {
	List<TableStatTask> findAllByClusterName(String clusterName);
}
