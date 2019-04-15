package com.meiyou.hbase.manager.dao;

import com.meiyou.hbase.manager.entity.MigrationTaskLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;

public interface MigrationTaskLogRepository extends CrudRepository<MigrationTaskLog,Integer>{
    @Modifying
    @Transactional
    @Query(value = "update migration_task_log log set log.end_time = ?2 where log.id = ?1",nativeQuery = true)
    void finishLog(int id, Date endTime);

}
