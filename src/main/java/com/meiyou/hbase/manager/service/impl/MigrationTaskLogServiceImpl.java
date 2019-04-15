package com.meiyou.hbase.manager.service.impl;

import com.meiyou.hbase.manager.dao.MigrationTaskLogRepository;
import com.meiyou.hbase.manager.entity.MigrationTaskLog;
import com.meiyou.hbase.manager.service.MigrationTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MigrationTaskLogServiceImpl implements MigrationTaskLogService {

    @Autowired
    private MigrationTaskLogRepository repository;

    @Override
    public List<MigrationTaskLog> list() {
        List<MigrationTaskLog> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public MigrationTaskLog findById(Integer id) {
        if(repository.existsById(id)){
            return  repository.findById(id).get();
        }else{
            return  null;
        }
    }

    @Override
    public void save(MigrationTaskLog log) {
        repository.save(log);
    }
}
