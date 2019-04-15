package com.meiyou.hbase.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.Session;
import com.meiyou.hbase.manager.constant.Constant;
import com.meiyou.hbase.manager.dao.*;
import com.meiyou.hbase.manager.entity.*;
import com.meiyou.hbase.manager.job.MigrationJob;
import com.meiyou.hbase.manager.service.ClusterService;
import com.meiyou.hbase.manager.service.MigrationTaskService;
import com.meiyou.hbase.manager.utils.CommonUtils;
import com.meiyou.hbase.manager.utils.HBaseFacade;
import com.meiyou.hbase.manager.utils.HBaseFacadeFactory;
import com.meiyou.hbase.manager.utils.ShellUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class MigrationTaskServiceImpl implements MigrationTaskService {

    @Autowired
    private MigrationTaskRepository repository;

    @Autowired
    private RunClusterRepository runClusterRepository;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private MigrationTaskLogRepository  migrationTaskLogRepository;

    @Autowired
    private MigrationTaskDetailLogRepository migrationTaskDetailLogRepository;
    
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public List<MigrationTask> list() {
        List<MigrationTask> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public MigrationTask findById(Integer id) {
        if(repository.existsById(id)){
            return  repository.findById(id).get();
        }else{
            return  null;
        }
    }

    @Override
    public void save(MigrationTask task) {
        repository.save(task);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public void deployMigration(MigrationTask task) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(task);
        JobKey jobKey =  getJobKey(task);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //删除JOB
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);

            JobDataMap map =  getJobDataMap(task);
            String desc = task.getSourceTable();
            JobDetail jobDetail = geJobDetail(jobKey,desc , map);

            if (task.getCustomCronOn() == 1) {
                scheduler.scheduleJob(jobDetail,getTrigger(task));
                log.info("Refresh Job : " + task.getId() + "\t table: " + task.getSourceTable() + "\t cron: "+task.getCustomCron()+" success !");
            } else {
                log.warn("Refresh Job : " + task.getId() + "\t table: " + task.getSourceTable() + " failed ! , " +
                        "Because the Job CustomCronOn is " + task.getCustomCronOn() );
            }
    }

    @Override
    public void unDeployMigration(Integer id) throws SchedulerException {
        if(repository.existsById(id)){
            MigrationTask task = repository.findById(id).get();
            TriggerKey triggerKey = getTriggerKey(task);
            JobKey jobKey =  getJobKey(task);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        }
    }

    /**
     * 计划中的任务
     * @throws SchedulerException
     */
    public  List<ScheduleJob> listScheduleJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
        for(JobKey jobKey : jobKeys){
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for(Trigger trigger : triggers){
                ScheduleJob job = new ScheduleJob();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setJobDesc("触发器:"+ trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if(trigger instanceof CronTrigger){
                    CronTrigger cronTrigger =(CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                    job.setNextFireTime(cronTrigger.getNextFireTime());
                    job.setPreviousFireTime( cronTrigger.getPreviousFireTime());
                }
                jobList.add(job);
            }
        }
        log.info(JSON.toJSONString(jobList));
        return jobList;
    }

    @Override
    public void runMigration(int logId,MigrationTask task,Date fileTime) {

        String sourceCluster = task.getSourceCluster();
        String sourceTable = task.getSourceTable();

        int currentTime = (int) (System.currentTimeMillis() / 1000);
        String snapshotName = task.getSourceTable()+"_"+currentTime;//自动生成 表名+时间戳

        String targetCluster = task.getTargetCluster();
        String targetTable = task.getTargetTable();

        String runCluster = task.getRunCluster();
        String runQueue = task.getRunQueue();
        String args = task.getProgramArguments();

        logTaskDetail(logId,"Hbase表迁移信息："+JSON.toJSONString(task));
        logTaskDetail(logId,String.format("开始创建快照 : sourceCluster = %s ,sourceTable = %s ==> snapshotName = %s",sourceCluster,sourceTable,snapshotName));

        boolean createResult = this.createSnapshot(sourceCluster,sourceTable,snapshotName);

        if(createResult){
            logTaskDetail(logId,String.format("快照创建完成，快照名称：%s",snapshotName));
        }else{
            logTaskDetail(logId,String.format("快照创建失败!"));
        }

        //2、
        boolean exportResult = false;
        if(createResult){
            logTaskDetail(logId,String.format("开始导出快照到目标集群 : runClusterName=%s, snapshotName = %s,targetClusterName = %s",runCluster,snapshotName,targetCluster));
            try {
                exportResult = this.exportSnapshot(runCluster,runQueue,sourceCluster,targetCluster,snapshotName,args);
            } catch (Exception e) {
                exportResult=false;
                logTaskDetail(logId,String.format("快照导出异常：%s",e.getMessage()));
            }
        }

        //结果检查
        if(exportResult){
            logTaskDetail(logId,String.format("快照导出成功， 目标集群：%s",targetCluster));
        }else{
            logTaskDetail(logId,String.format("快照导出失败！快照：%s",snapshotName));
        }

        //3、
        boolean cloneResult = false;
        if(exportResult){
            logTaskDetail(logId,String.format("开始克隆快照  : snapshotName = %s ==> targetCluster = %s ,targetTable = %s ",snapshotName,targetCluster,targetTable));
            cloneResult = this.cloneSnapshot(targetCluster,targetTable,snapshotName);
        }

        //结果检查
        if(cloneResult){
            logTaskDetail(logId,String.format("快照克隆完成， 生产目标表：%s",targetTable));
        }else{
            logTaskDetail(logId,String.format("快照克隆失败！"));
        }

        if(createResult){//清理源集群快照
            this.deleteSnapshot(sourceCluster,sourceTable,snapshotName);
            logTaskDetail(logId,String.format("删除源集群快照完成 : sourceCluster = %s ,snapshotName=%s ",sourceCluster,snapshotName));
        }

        if(exportResult){//删除目标集群快照
            this.deleteSnapshot(targetCluster,sourceTable,snapshotName);
            logTaskDetail(logId,String.format("删除目标集群快照完成 : targetCluster = %s ,snapshotName=%s ",targetCluster,snapshotName));;
        }

        if(cloneResult){
            logTaskDetail(logId,String.format("Hbase表迁移完成!"));
        }else{
            logTaskDetail(logId,String.format("Hbase表迁移失败！"));
        }

        logTaskEnd(logId);
    }

    @Override
    @Async
    public void runOnce(int taskId) throws SchedulerException {
        if(repository.existsById(taskId)){
            MigrationTask task = repository.findById(taskId).get();
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = getJobKey(task);
            scheduler.triggerJob(jobKey);
        }
    }

    @Override
    public int runByManual(int taskId) {
        //生成日志ID
        MigrationTask task = findById(taskId);
        int logId = createTaskLog(task,new Date(),Constant.FIRE_TYPE_MANUAL);
        runMigrationAsync( logId, task,new Date()); //异步执行
        return logId;
    }

    @Async
    @Override
    public void runMigrationAsync(int logId,MigrationTask task,Date fireTime){
        runMigration(logId,task,fireTime);
    }

    @Override
    public void cleanExpireMigrationJobs() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());
        for (JobKey jobKey : set) {
            if(jobKey.getName().equals(Constant.JOB_MIGRATION)){//迁移任务
                //检查任务状态，失效下线任务
                int migrationId =  Integer.parseInt(jobKey.getGroup());
                MigrationTask task = repository.findMigrationTaskByIdEqualsAndStatusEquals(migrationId,Constant.STATUS_NORMAL);
                if(task==null){
                    scheduler.deleteJob(jobKey);
                    log.info("scheduler deleteJob :name={},group={}",jobKey.getName(),jobKey.getGroup());
                }

            }
        }
    }

    /**
     *
     * @param clusterName
     * @param tableName
     * @param snapshotName
     * @return
     */
    private boolean createSnapshot(String clusterName,String tableName,String snapshotName){

        log.info("start to create snapshot : clusterName = {} ,tableName = {} ==> snapshotName = {}",clusterName,tableName,snapshotName);
        // #SNAPSHOT 拷贝
        // 例如需要将 test  迁移至的集群
        //  1.进入原集群的hbase shell
        //  snapshot 'test'， 'snapshot_test'

        //获取集群信息
        Cluster cluster = clusterService.getClusterByName(clusterName);
        HBaseFacade administrator = HBaseFacadeFactory
                .createAdministrator(cluster.getClusterName(), cluster.getZookeeperAddress(), cluster.getZnodeParent());
        administrator.operateTableSnapshot(snapshotName,tableName,HBaseFacade.HTableSnapshotOpEnum.CREATE_SNAPSHOT);
        log.info("create snapshot finished !");
        return true;
    }

    private boolean deleteSnapshot(String clusterName,String tableName,String snapshotName){

        log.info("start to delete snapshot : clusterName = {} ,tableName = {} ==> snapshotName = {}",clusterName,tableName,snapshotName);

        //获取集群信息
        Cluster cluster = clusterService.getClusterByName(clusterName);
        HBaseFacade administrator = HBaseFacadeFactory
                .createAdministrator(cluster.getClusterName(), cluster.getZookeeperAddress(), cluster.getZnodeParent());

        administrator.operateTableSnapshot(snapshotName,tableName,HBaseFacade.HTableSnapshotOpEnum.DELETE_SNAPSHOT);
        log.info("delete snapshot finished !");
        return true;
    }
    private boolean exportSnapshot(String runClusterName ,String runQueque,String sourceClusterName,String targetClusterName,String snapshotName,String args) throws Exception {

        log.info("start to export snapshot :runClusterName={}, sourceClusterName = {} ,targetClusterName = {} , snapshotName = {}",runClusterName,sourceClusterName,targetClusterName,snapshotName);
        RunCluster runCluster = runClusterRepository.findByClusterName(runClusterName);
        if(runCluster.getId()==null){
            log.error("run cluster is not exist ! ,runClusterName = {}",runClusterName);
            return false;
        }
        String host = runCluster.getIp();
        String port = runCluster.getPort();
        String sshUser = runCluster.getSshUser();
        String sshPasswod = runCluster.getSshPassword();

        //获取集群信息
        Cluster sourceCluster = clusterService.getClusterByName(sourceClusterName);
        String zkAddr = sourceCluster.getZookeeperAddress();
        /**
         * 从zk配置中解析次出host和Port
         */
        Pair<String, String> zkInfo = CommonUtils.parseZkAddr(zkAddr);
        String quorum  = zkInfo.getFirst();
        String clientPort = zkInfo.getSecond();

        String rootDir = sourceCluster.getHdfsRootPath();
        String znodeParent = sourceCluster.getZnodeParent();

        Cluster targetCluster = clusterService.getClusterByName(targetClusterName);
        String targetPath = targetCluster.getHdfsRootPath();

        String cmd = String.format("hbase org.apache.hadoop.hbase.snapshot.ExportSnapshot -Dhbase.rootdir=%s -Dzookeeper.znode.parent=%s -Dhbase.zookeeper.quorum=%s -Dhbase.zookeeper.property.clientPort=%s -Dmapreduce.job.queuename=%s -snapshot %s  -copy-to %s %s",
                rootDir,
                znodeParent,
                quorum,
                clientPort,
                runQueque,
                snapshotName,
                targetPath,
                args);

        log.info("String to run cmd : {}",cmd);
        Session session = ShellUtils.connect(host,sshUser,sshPasswod,Integer.parseInt(port));
        ShellUtils.runCmd(session,cmd,null);
        log.info("command finished!");
        //2. 将snapshot 导出至目标集群
        //在有配置文件的环境下
        //hbase --conf /etc/hbase2/conf/ org.apache.hadoop.hbase.snapshot.ExportSnapshot -Dmapreduce.map.memory.mb=10240  -Dmapreduce.job.queuename=test -snapshot snapshot_user_show_tag -copy-to  -mappers 20

        //在没有配置文件环境下
        //hbase org.apache.hadoop.hbase.snapshot.ExportSnapshot -Dhbase.rootdir= -Dzookeeper.znode.parent=/hbase2 -Dhbase.zookeeper.quorum= -Dhbase.zookeeper.property.clientPort=2181 -Dmapreduce.map.memory.mb=1024  -Dmapreduce.job.queuename=test -snapshot snapshot_user_show_tag  -copy-to  -mappers 1
        return true;
    }
    private boolean cloneSnapshot(String clusterName,String tableName,String snapshotName){
        log.info("start to copy snapshot : snapshotName = {} ==> clusterName = {} ,tableName = {} ",snapshotName,clusterName,tableName);
        //3.进入目标集群的hbase shell
        //clone_snapshot  ‘snapshot_user_show_tag' ,’user_show_tag’

        //获取集群信息
        Cluster cluster = clusterService.getClusterByName(clusterName);
        HBaseFacade administrator = HBaseFacadeFactory
                .createAdministrator(cluster.getClusterName(), cluster.getZookeeperAddress(), cluster.getZnodeParent());

        administrator.operateTableSnapshot(snapshotName,tableName,HBaseFacade.HTableSnapshotOpEnum.CLONE_SNAPSHOT);//克隆快照
        log.info("clone snapshot finished !");
        return true;
    }


    private JobDataMap getJobDataMap(MigrationTask task) {
        JobDataMap map = new JobDataMap();
        map.put("MIG_ID", task.getId());
        return map;
    }

    private JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map) {
        return JobBuilder.newJob(MigrationJob.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                .storeDurably()
                .build();
    }

    private Trigger getTrigger(MigrationTask task) {
        return TriggerBuilder.newTrigger()
                .withIdentity("MIG_"+task.getId(),"MIG_"+task.getId())
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCustomCron()))
                .build();
    }

    private JobKey getJobKey(MigrationTask task) {
        return JobKey.jobKey("MIGRATION", ""+task.getId());
    }

    private TriggerKey getTriggerKey(MigrationTask task) {
        return TriggerKey.triggerKey("MIG_"+task.getId(), "MIG_"+task.getId());
    }

    @Override
    public int createTaskLog(MigrationTask task, Date fireTime,Integer fireType){
        MigrationTaskLog log = new MigrationTaskLog();

        log.setFireType(fireType);

        log.setMigrationTaskId(task.getId());
        log.setSourceCluster(task.getSourceCluster());
        log.setSourceTableName(task.getSourceTable());
        log.setTargetCluster(task.getTargetCluster());
        log.setTargetTableName(task.getTargetTable());

        log.setSnapshotName(task.getSourceTable()+"_"+fireTime.getTime()); //表名+时间错

        log.setFireTime(fireTime);
        log.setCreateTime(new Date());
        log.setStartTime(new Date());

        MigrationTaskLog savedLog = migrationTaskLogRepository.save(log);

        return savedLog.getId();
    }

    private void logTaskEnd(int logId){
        migrationTaskLogRepository.finishLog(logId,new Date());
    }

    private void logTaskDetail(int logId,String content){
        MigrationTaskDetailLog detailLog = new MigrationTaskDetailLog(logId,content);
        migrationTaskDetailLogRepository.save(detailLog);
    }
}
