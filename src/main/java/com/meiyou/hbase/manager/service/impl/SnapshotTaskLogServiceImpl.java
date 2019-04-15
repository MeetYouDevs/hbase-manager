package com.meiyou.hbase.manager.service.impl;

import com.meiyou.hbase.manager.dao.SnapshotTaskLogRepository;
import com.meiyou.hbase.manager.entity.SnapshotTaskLog;
import com.meiyou.hbase.manager.service.SnapshotTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SnapshotTaskLogServiceImpl implements SnapshotTaskLogService{
    @Autowired
    private SnapshotTaskLogRepository repository;

    @Override
    public List<SnapshotTaskLog> list() {
        List<SnapshotTaskLog> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public SnapshotTaskLog findById(Integer id) {
        if(repository.existsById(id)){
            return  repository.findById(id).get();
        }else{
            return  null;
        }
    }

    @Override
    public void save(SnapshotTaskLog log) {
        repository.save(log);
    }

    @Override
    public List<SnapshotTaskLog> getNotDeletedLog() {
        return  repository.findSnapshotTaskLogsByHasDeletedEquals(0);
    }

    @Override
    public void updateHasDeleted(int id,int hasDeleted) {
        repository.updateHasDeleted(id,hasDeleted);
    }
}
