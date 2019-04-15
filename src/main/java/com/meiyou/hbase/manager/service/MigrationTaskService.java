package com.meiyou.hbase.manager.service;

import com.meiyou.hbase.manager.entity.MigrationTask;
import com.meiyou.hbase.manager.entity.ScheduleJob;
import org.quartz.SchedulerException;

import java.util.Date;
import java.util.List;

public interface MigrationTaskService {
    List<MigrationTask> list();
    MigrationTask findById(Integer id);
    void save(MigrationTask task);
    void delete(Integer id);

    void deployMigration(MigrationTask task) throws SchedulerException;
    void unDeployMigration(Integer id) throws SchedulerException;
    void cleanExpireMigrationJobs() throws SchedulerException;
    List<ScheduleJob> listScheduleJob() throws SchedulerException;

    /**
     * 迁移
     * @param logId
     * @param task
     * @param fileTime
     */
    void runMigration(int logId,MigrationTask task, Date fileTime);

    void runMigrationAsync(int logId,MigrationTask task,Date fireTime);

    /**
     * 调度立即执行一次
     * @param taskId
     * @throws SchedulerException
     */
    void runOnce(int taskId) throws SchedulerException;

    /**
     *
     * @param taskId
     * @return 日志ID
     */
    int runByManual(int taskId);

    int createTaskLog(MigrationTask task, Date fireTime,Integer fireType);
}
