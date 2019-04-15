package com.meiyou.hbase.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.constant.Constant;
import com.meiyou.hbase.manager.dao.SnapshotTaskRepository;
import com.meiyou.hbase.manager.entity.*;
import com.meiyou.hbase.manager.job.SnapshotJob;
import com.meiyou.hbase.manager.service.ClusterService;
import com.meiyou.hbase.manager.service.SnapShotService;
import com.meiyou.hbase.manager.service.SnapshotTaskLogService;
import com.meiyou.hbase.manager.service.SnapshotTaskService;
import com.meiyou.hbase.manager.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
public class SnapshotTaskServiceImpl implements SnapshotTaskService {
    @Autowired
    private SnapshotTaskRepository repository;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private SnapShotService snapShotService;

    @Autowired
    private SnapshotTaskLogService snapshotTaskLogService;

    @Autowired
    private ClusterService clusterService;

    @Override
    public SnapshotTask findById(Integer id) {
        if(repository.existsById(id)){
            return  repository.findById(id).get();
        }else{
            return  null;
        }
    }

    @Override
    public List<SnapshotTask> list(String clusterName) {
        return  repository.findAllByClusterEquals(clusterName);
    }

    @Override
    public List<SnapshotTask> findAll() {
        List<SnapshotTask> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public SnapshotTask add(SnapshotTask snapshotTask) {
        SnapshotTask savedTask = repository.save(snapshotTask);
        return savedTask;
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public void runCreateSnapshot(Integer snapshotId, Date fireTime) {
        if(!repository.existsById(snapshotId)){
            log.error("Snapshot task not exist! snapshotId={}",snapshotId);
            return;
        }
        SnapshotTask snapshotTask = repository.findById(snapshotId).get();

        //有效期和状态检查  TODO
        if(snapshotTask.getStatus()==Constant.STATUS_DISABLE||fireTime.before(snapshotTask.getInureDate())||fireTime.after(snapshotTask.getExpireDate())){
            log.warn("Snapshot task status is STATUS_DISABLE or out of date! snapshotId={}",snapshotId);
            return;
        }

        try {
            String snapshotName = snapshotTask.getSnapshotName();
            String tableName = snapshotTask.getTableName();
            String clusterName = snapshotTask.getCluster();

            /**周期类型(1-小时 2-天; 3-周 4-月)
             * 快照名称规则
             * 小时 表名_YYYYMMDDHH
             * 日  表名_YYYYMMDD
             * 月 表名_YYYYMM
             */

                int cycleType = snapshotTask.getCycleType();
                String suffix = DateUtils.format("yyyyMMdd",fireTime);; //快照后缀

                if(cycleType== Constant.TASK_CYCLE_TYPE_HOUR){//小时
                    suffix = DateUtils.format("yyyyMMddHH",fireTime);
                }else if(cycleType==Constant.TASK_CYCLE_TYPE_DAY){//天
                    suffix = DateUtils.format("yyyyMMdd",fireTime);
                }else if(cycleType==Constant.TASK_CYCLE_TYPE_WEEK){//周
                    suffix = DateUtils.format("yyyyMMdd",fireTime);
                }else if(cycleType==Constant.TASK_CYCLE_TYPE_MONTH){//月
                    suffix = DateUtils.format("yyyyMM",fireTime);
                }
                if(StringUtils.isEmpty(snapshotName)){//没指定快照名
                    snapshotName = tableName+"_"+suffix;
                }else {
                    snapshotName = snapshotName+"_"+suffix;
                }

            log.info("begin to create Snapshot : clusterName={},tableName={},snapshotName={}",clusterName,tableName,snapshotName);

            Date statTime = new Date();
            snapShotService.createSnapshot(clusterName,tableName,snapshotName);
            Date endTime = new Date();

            //记录日志
            SnapshotTaskLog snapshotTaskLog = new SnapshotTaskLog();

            snapshotTaskLog.setSnapshotTaskId(snapshotTask.getId());
            snapshotTaskLog.setCluster(snapshotTask.getCluster());
            snapshotTaskLog.setTableName(snapshotTask.getTableName());
            snapshotTaskLog.setSnapshotName(snapshotName);

            snapshotTaskLog.setSuffix(suffix);

            snapshotTaskLog.setStartTime(statTime);
            snapshotTaskLog.setEndTime(endTime);
            snapshotTaskLog.setFireTime(fireTime);
            snapshotTaskLog.setCreateTime(new Date());
            snapshotTaskLog.setHasDeleted(0);

            snapshotTaskLogService.save(snapshotTaskLog);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runDeleteSnapshot(Date fireTime) {
        //1、获取未清理的快照日志
        List<SnapshotTaskLog> snapshotTaskLogList = snapshotTaskLogService.getNotDeletedLog();

        //2、计算出各个定时清理任务需要清理的截至时间
        List<SnapshotTask> snapshotTaskList = (List<SnapshotTask>) repository.findAll();
        Map<Integer,String> snapshotTaskDeleteLineMap = new HashMap<>();


        for(SnapshotTask task : snapshotTaskList){
            Integer keepCnt = task.getKeepCnt();
            Integer cycleType = task.getCycleType();
            String dateLineStr ;
            if(cycleType==Constant.TASK_CYCLE_TYPE_HOUR){
                Date dateLine  = DateUtils.passHour(fireTime,-1*keepCnt);
                dateLineStr = DateUtils.format_YYYYYMMDDHH(dateLine);
            }else if(cycleType==Constant.TASK_CYCLE_TYPE_DAY){
                Date dateLine = DateUtils.passDay(fireTime,-1*keepCnt);
                dateLineStr = DateUtils.format_YYYYYMMDD(dateLine);
            }else if(cycleType==Constant.TASK_CYCLE_TYPE_WEEK){
                Date dateLine = DateUtils.passDay(fireTime,-7*keepCnt);
                dateLineStr = DateUtils.format_YYYYYMMDD(dateLine);
            }else if(cycleType==Constant.TASK_CYCLE_TYPE_MONTH){
                Date dateLine = DateUtils.passMonth(fireTime,-1*keepCnt);
                dateLineStr = DateUtils.format_YYYYYMM(dateLine);
            }else {
                log.error("周期类型错误！cycleType={}",cycleType);
                return;
            }
            snapshotTaskDeleteLineMap.put(task.getId(),dateLineStr);
        }

        log.info("任务清理时间线:{}", JSON.toJSONString(snapshotTaskDeleteLineMap));

        //3、时间检查，过滤出需要清理的日志

        List<Cluster> clusterList = clusterService.listCluster();

//      List<String> clusterNameList = new ArrayList<>();
        List<SnapshotTaskLog> timeToDeleteTaskLogList = new ArrayList<>();
        for(SnapshotTaskLog taskLog : snapshotTaskLogList){
            String taskDateLine = snapshotTaskDeleteLineMap.get(taskLog.getSnapshotTaskId());
            if(StringUtils.isBlank(taskDateLine)) {
            	continue;
            }
            if(taskLog.getSuffix()!= null && taskLog.getSuffix().compareTo(taskDateLine) <= 0){
                timeToDeleteTaskLogList.add(taskLog);
//              if(clusterList.contains(taskLog.getCluster())){//集群有效性检查
//              	clusterNameList.add(taskLog.getCluster());
//              }
            }
        }
        log.info("待清理快照:{}", JSON.toJSONString(timeToDeleteTaskLogList));

        //4.获取集群快照清单，删除前做是否存在比较
        Map<String,String> snapshotInClusterMap = new HashMap<>(); //key 集群名:快照名称
        for(Cluster c:clusterList){
            String clusterName = c.getClusterName();
            List<MySnapshotDescription> snapshotDescriptionList = snapShotService.list(clusterName);
            for(MySnapshotDescription desc:snapshotDescriptionList){
                snapshotInClusterMap.put(clusterName+":"+desc.getName(),"");
            }
        }
        log.info("集群快照清单："+JSON.toJSONString(snapshotInClusterMap));

        //5.删除快照
        log.info("开始清理快照，待清理快照数 {}",timeToDeleteTaskLogList.size());
        for(SnapshotTaskLog taskLog : timeToDeleteTaskLogList){
            log.info("清理快照 {},{}",taskLog.getCluster(),taskLog.getSnapshotName());
            if(snapshotInClusterMap.containsKey(taskLog.getCluster()+":"+taskLog.getSnapshotName())){
                snapShotService.delSnapshot(taskLog.getCluster(),taskLog.getSnapshotName());
            }
            snapshotTaskLogService.updateHasDeleted(taskLog.getId(),1);
        }
    }

    @Override
    public void deploySnapshotTask(SnapshotTask task) {
        TriggerKey triggerKey = getTriggerKey(task);
        JobKey jobKey =  getJobKey(task);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            //删除JOB
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);

            JobDataMap map =  getJobDataMap(task);
            String desc = task.getTableName();//TODO 待进一步补充
            JobDetail jobDetail = geJobDetail(jobKey,desc , map);

            if (task.getCustomCronOn() == 1) {
                scheduler.scheduleJob(jobDetail,getTrigger(task));
                log.info("Refresh Job : " + task.getId() + "\t table: " + task.getTableName() + "\t cron: "+task.getCustomCron()+" success !");
            } else {
                log.warn("Refresh Job : " + task.getId() + "\t table: " + task.getTableName() + " failed ! , " +
                        "Because the Job CustomCronOn is " + task.getCustomCronOn() );
            }

        } catch (SchedulerException e) {
            log.info("Error while Refresh " + e.getMessage());
        }
    }

    @Override
    public void unDeploySnapshotTask(Integer id) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if(repository.existsById(id)){
            SnapshotTask task = repository.findById(id).get();
            JobKey jobKey = getJobKey(task);
            scheduler.deleteJob(jobKey);
            log.info("scheduler deleteJob :name={},group={}",jobKey.getName(),jobKey.getGroup());
        }else{
            log.error("Snapshot Task not exist in scheduler ,id={}",id);
        }
    }

    private Trigger getTrigger(SnapshotTask task) {
            return TriggerBuilder.newTrigger()
                    .withIdentity("SNAPSHOT_"+task.getId(),"SNAPSHOT_"+task.getId())
                    .withSchedule(CronScheduleBuilder.cronSchedule(task.getCustomCron()))
                    .build();
    }

    private JobDetail geJobDetail(JobKey jobKey, String desc, JobDataMap map) {
        return JobBuilder.newJob(SnapshotJob.class)
                .withIdentity(jobKey)
                .withDescription(desc)
                .setJobData(map)
                .storeDurably()
                .build();
    }

    private JobDataMap getJobDataMap(SnapshotTask task) {
        JobDataMap map = new JobDataMap();
        map.put("SNAPSHOT_ID", task.getId());
        return map;
    }

    private JobKey getJobKey(SnapshotTask task) {
        return JobKey.jobKey("SNAPSHOT", "SNAPSHOT_"+task.getId());
    }

    private TriggerKey getTriggerKey(SnapshotTask task) {
        return TriggerKey.triggerKey("SNAPSHOT_"+task.getId(), "SNAPSHOT_"+task.getId());
    }

    @Override
    public void cleanSnapshotTask() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());
        for (JobKey jobKey : set) {
            if(jobKey.getName().equals("SNAPSHOT")){
                scheduler.deleteJob(jobKey);
                log.info("scheduler deleteJob :name={},group={}",jobKey.getName(),jobKey.getGroup());
            }
        }
    }

    @Override
    public List<ScheduleJob> listScheduleJob() throws SchedulerException {
        return null;
    }

}
