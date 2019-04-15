package com.meiyou.hbase.manager.service.impl;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.meiyou.hbase.manager.entity.Cluster;
import com.meiyou.hbase.manager.entity.MySnapshotDescription;
import com.meiyou.hbase.manager.job.SnapshotDeleteJob;
import com.meiyou.hbase.manager.service.ClusterService;
import com.meiyou.hbase.manager.service.SnapShotService;
import com.meiyou.hbase.manager.utils.HBaseFacade;
import com.meiyou.hbase.manager.utils.HBaseFacadeFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SnapshotServiceImpl implements SnapShotService {

    @Autowired
    private ClusterService clusterService;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public List<MySnapshotDescription> list(String clusterName) {
        Cluster cluster = clusterService.getClusterByName(clusterName);
        HBaseFacade administrator = HBaseFacadeFactory
                .createAdministrator(cluster.getClusterName(), cluster.getZookeeperAddress(), cluster.getZnodeParent());
        List<MySnapshotDescription> descriptionList = administrator.listSnapshotsDesc();
        return descriptionList;
    }

    @Override
    public boolean delSnapshot(String clusterName, String name) {
        Cluster cluster = clusterService.getClusterByName(clusterName);
        HBaseFacade administrator = HBaseFacadeFactory
                .createAdministrator(cluster.getClusterName(), cluster.getZookeeperAddress(), cluster.getZnodeParent());
        administrator.operateTableSnapshot(name, "", HBaseFacade.HTableSnapshotOpEnum.DELETE_SNAPSHOT);
        return true;
    }
    
    @Override
    public boolean createSnapshot(String clusterName,String tableName,String snapshotName){
        log.info("start to create snapshot : clusterName = {} ,tableName = {} ==> snapshotName = {}",clusterName,tableName,snapshotName);
        Cluster cluster = clusterService.getClusterByName(clusterName);
        HBaseFacade administrator = HBaseFacadeFactory
                .createAdministrator(cluster.getClusterName(), cluster.getZookeeperAddress(), cluster.getZnodeParent());
        administrator.operateTableSnapshot(snapshotName,tableName,HBaseFacade.HTableSnapshotOpEnum.CREATE_SNAPSHOT);
        log.info("create snapshot finished [clusterName,tableName,snapshotName] = [{},{},{}]!",clusterName,tableName,snapshotName);
        return true;
    }

    @Override
    public boolean deployAutoClean() {
        TriggerKey triggerKey = TriggerKey.triggerKey("N_SNAPSHOT_AUTO_CLEAN", "G_SNAPSHOT_AUTO_CLEAN_NAME");
        JobKey jobKey = JobKey.jobKey("N_SNAPSHOT_AUTO_CLEAN", "G_SNAPSHOT_AUTO_CLEAN_NAME");
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            //删除JOB
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);

           JobDataMap map = new JobDataMap();
            String description = "Snapshot auto clean job";
            JobDetail jobDetail =JobBuilder.newJob(SnapshotDeleteJob.class)
                    .withIdentity(jobKey)
                    .withDescription(description)
                    .setJobData(map)
                    .storeDurably()
                    .build();

            Trigger trigger= TriggerBuilder.newTrigger()
                    .withIdentity("N_SNAPSHOT_AUTO_CLEAN","G_SNAPSHOT_AUTO_CLEAN_NAME")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/10 * * * ? "))  //每两个小时清理一次
                    .build();

            scheduler.scheduleJob(jobDetail,trigger);
            log.info("Snapshot auto clean job deploy success !");
        } catch (SchedulerException e) {
            log.info("Error while Refresh " + e.getMessage());
        }
        return false;
    }
}
