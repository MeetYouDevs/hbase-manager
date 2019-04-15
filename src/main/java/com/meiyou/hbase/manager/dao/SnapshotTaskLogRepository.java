package com.meiyou.hbase.manager.dao;

import com.meiyou.hbase.manager.entity.SnapshotTaskLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import javax.transaction.Transactional;
import java.util.List;

public interface SnapshotTaskLogRepository extends CrudRepository<SnapshotTaskLog,Integer> {
    List<SnapshotTaskLog> findSnapshotTaskLogsByHasDeletedEquals(Integer hasDeleted);

    @Modifying
    @Transactional
    @Query(value = "update snapshot_task_log log set log.has_deleted = ?2 where log.id = ?1",nativeQuery = true)
    void updateHasDeleted(int id,int hasDeleted);
}
