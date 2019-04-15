package com.meiyou.hbase.manager.service;

import com.meiyou.hbase.manager.entity.MigrationTaskLog;

import java.util.List;

public interface MigrationTaskLogService {
    List<MigrationTaskLog> list();
    MigrationTaskLog findById(Integer id);
    void save(MigrationTaskLog log);
}
