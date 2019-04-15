package com.meiyou.hbase.manager.dao;

import com.meiyou.hbase.manager.entity.SnapshotTask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SnapshotTaskRepository extends CrudRepository<SnapshotTask,Integer> {
    List<SnapshotTask> findAllByClusterEquals(String cluster);
}
