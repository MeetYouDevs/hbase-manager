package com.meiyou.hbase.manager.service;


import com.meiyou.hbase.manager.entity.MigrationTaskDetailLog;

import java.util.List;

public interface MigrationTaskDetailLogService {
     List<MigrationTaskDetailLog> findAllByLogId(Integer logId,Long curMaxId);
}
