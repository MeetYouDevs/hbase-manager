package com.meiyou.hbase.manager.service;

import com.meiyou.hbase.manager.entity.ScheduleJob;
import com.meiyou.hbase.manager.entity.SnapshotTask;
import org.quartz.SchedulerException;

import java.util.Date;
import java.util.List;

public interface SnapshotTaskService {
    SnapshotTask findById(Integer id);
    List<SnapshotTask> list(String clusterName);
    List<SnapshotTask> findAll();
    SnapshotTask add(SnapshotTask snapshotTask);
    void delete(Integer id);
    void runCreateSnapshot(Integer snapshotId, Date fireTime);
    void runDeleteSnapshot(Date fireTime);

    void deploySnapshotTask(SnapshotTask task);
    void unDeploySnapshotTask(Integer id) throws SchedulerException;
    void cleanSnapshotTask() throws SchedulerException;
    List<ScheduleJob> listScheduleJob() throws SchedulerException;
}
