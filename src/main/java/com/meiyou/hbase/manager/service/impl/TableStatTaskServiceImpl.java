package com.meiyou.hbase.manager.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.meiyou.hbase.manager.dao.TableStatTaskRepository;
import com.meiyou.hbase.manager.entity.Cluster;
import com.meiyou.hbase.manager.entity.TableStatResult;
import com.meiyou.hbase.manager.entity.TableStatTask;
import com.meiyou.hbase.manager.job.TableStatJob;
import com.meiyou.hbase.manager.service.ClusterService;
import com.meiyou.hbase.manager.service.TableStatResultService;
import com.meiyou.hbase.manager.service.TableStatTaskService;
import com.meiyou.hbase.manager.utils.HBaseWebUICrawler;
import com.meiyou.hbase.manager.utils.ScheduleUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TableStatTaskServiceImpl implements TableStatTaskService {
	@Autowired
	private TableStatTaskRepository tableStatTaskRepository;

	@Autowired
	private TableStatResultService tableStatInfoService;

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Override
	public List<TableStatTask> listAllStatTask() {
		List<TableStatTask> tableStatTaskList = new ArrayList<TableStatTask>();
		tableStatTaskRepository.findAll().forEach(tableStatTaskList::add);
		return tableStatTaskList;
	}

	@Override
	public List<TableStatTask> listStatTask(String clusterName) {
		return tableStatTaskRepository.findAllByClusterName(clusterName);
	}

	@Override
	public TableStatTask findById(Integer id) {
		return tableStatTaskRepository.findById(id).get();
	}

	@Override
	public void addStatTask(Integer id, String clusterName, String tableName, Integer intervalPosition,
			Integer interval, Integer offsetPosition, Integer offset) {
		String cron = ScheduleUtils.joinCron(interval, intervalPosition, offset, offsetPosition);
		TableStatTask tableStatTask = new TableStatTask(id, clusterName, tableName, cron, intervalPosition, interval,
				offsetPosition, offset);
		tableStatTaskRepository.save(tableStatTask);
		deployStat(tableStatTask);
	}

	@Override
	public void deleteById(Integer id) {
		unDeployStat(id);
		tableStatTaskRepository.deleteById(id);
	}

	@Override
	public void executeStatTask(String clusterName, String tableName) {
		Cluster cluster = clusterService.getClusterByName(clusterName);
		// 解析master界面, 获取region服务列表
		Set<String> regionServerSet = HBaseWebUICrawler.getRegionServerSet(cluster.getWebConsoleUrl());
		for (String regionServer : regionServerSet) {
			List<TableStatResult> tableStatInfoList = HBaseWebUICrawler.getStatsInfoByTable(clusterName, regionServer,
					tableName);
			if(!CollectionUtils.isEmpty(tableStatInfoList)) {
				tableStatInfoService.batchSave(tableStatInfoList);
			}
		}
	}

	@Override
	public void deployStat(TableStatTask task) {
		TriggerKey triggerKey = getTriggerKey(task);
		JobKey jobKey = getJobKey(task);

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		try {
			// 删除JOB
			scheduler.unscheduleJob(triggerKey);
			scheduler.deleteJob(jobKey);

			JobDataMap map = getJobDataMap(task);
			String desc = task.getTableName();
			JobDetail jobDetail = geJobDetail(jobKey, desc, map);

			scheduler.scheduleJob(jobDetail, getTrigger(task));
			log.info("Refresh Job : " + task.getId() + "\t table: " + task.getTableName() + "\t cron: " + task.getCron()
					+ " success !");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void unDeployStat(Integer id) {
		if (tableStatTaskRepository.existsById(id)) {
			TableStatTask task = tableStatTaskRepository.findById(id).get();
			TriggerKey triggerKey = getTriggerKey(task);
			JobKey jobKey = getJobKey(task);
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			try {
				scheduler.unscheduleJob(triggerKey);
				scheduler.deleteJob(jobKey);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private JobDataMap getJobDataMap(TableStatTask task) {
		JobDataMap map = new JobDataMap();
		map.put("STAT_ID", task.getId());
		return map;
	}

	private JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map) {
		return JobBuilder.newJob(TableStatJob.class).withIdentity(jobKey).withDescription(description).setJobData(map)
				.storeDurably().build();
	}

	private Trigger getTrigger(TableStatTask task) {
		return TriggerBuilder.newTrigger().withIdentity("STAT_" + task.getId(), "STAT_" + task.getId())
				.withSchedule(CronScheduleBuilder.cronSchedule(task.getCron())).build();
	}

	private JobKey getJobKey(TableStatTask task) {
		return JobKey.jobKey("STAT", "" + task.getId());
	}

	private TriggerKey getTriggerKey(TableStatTask task) {
		return TriggerKey.triggerKey("STAT_" + task.getId(), "STAT_" + task.getId());
	}

}
