package com.meiyou.hbase.manager.dao;


import com.meiyou.hbase.manager.entity.MigrationTaskDetailLog;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MigrationTaskDetailLogRepository extends CrudRepository<MigrationTaskDetailLog,Integer> {
    List<MigrationTaskDetailLog> findAllByMigrationTaskLogIdEqualsOrderByIdAsc(Integer logId);
    List<MigrationTaskDetailLog> findAllByMigrationTaskLogIdEqualsAndIdIsGreaterThanOrderByIdAsc(Integer logId,Long curMaxId);
}
