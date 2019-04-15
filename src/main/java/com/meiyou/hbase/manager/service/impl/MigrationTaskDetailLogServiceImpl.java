package com.meiyou.hbase.manager.service.impl;

import com.meiyou.hbase.manager.dao.MigrationTaskDetailLogRepository;
import com.meiyou.hbase.manager.entity.MigrationTaskDetailLog;
import com.meiyou.hbase.manager.service.MigrationTaskDetailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MigrationTaskDetailLogServiceImpl implements MigrationTaskDetailLogService {

    @Autowired
    private MigrationTaskDetailLogRepository repository;

    @Override
    public List<MigrationTaskDetailLog> findAllByLogId(Integer logId,Long curMaxId) {
        if(curMaxId>0){
            return repository.findAllByMigrationTaskLogIdEqualsAndIdIsGreaterThanOrderByIdAsc(logId,curMaxId);
        }else{
            return repository.findAllByMigrationTaskLogIdEqualsOrderByIdAsc(logId);
        }
    }
}
