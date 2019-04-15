package com.meiyou.hbase.manager.service;

import com.meiyou.hbase.manager.entity.SnapshotTaskLog;

import java.util.List;

public interface SnapshotTaskLogService {
    List<SnapshotTaskLog> list();
    SnapshotTaskLog findById(Integer id);
    void save(SnapshotTaskLog log);
    List<SnapshotTaskLog> getNotDeletedLog();
    void updateHasDeleted(int id,int hasDeleted);

}
