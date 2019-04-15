package com.meiyou.hbase.manager.service.impl;

import java.util.List;

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

import com.meiyou.hbase.manager.dao.TableCompactTaskRepository;
import com.meiyou.hbase.manager.entity.TableCompactTask;
import com.meiyou.hbase.manager.job.TableCompactJob;
import com.meiyou.hbase.manager.service.TableCompactTaskService;
import com.meiyou.hbase.manager.utils.ScheduleUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TableCompactTaskServiceImpl implements TableCompactTaskService {
	@Autowired
	private TableCompactTaskRepository tableCompactTaskRepository;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Override
	public List<TableCompactTask> listCompactTask(String clusterName) {
		return tableCompactTaskRepository.findAllByClusterName(clusterName);
	}

	@Override
	public TableCompactTask findById(Integer id) {
		return tableCompactTaskRepository.findById(id).get();
	}

	@Override
	public void addCompactTask(Integer id, String clusterName, String tableName, Integer intervalPosition,
			Integer interval, Integer offsetPosition, Integer offset) {
		// 生成cron表达式
		String cron = ScheduleUtils.joinCron(interval, intervalPosition, offset, offsetPosition);
		TableCompactTask tableCompactTask = new TableCompactTask(id, clusterName, tableName, cron, intervalPosition,
				interval, offsetPosition, offset);
		tableCompactTaskRepository.save(tableCompactTask);
		deployCompact(tableCompactTask);
	}

	@Override
	public void deleteById(Integer id) {
		unDeployCompact(id);
		tableCompactTaskRepository.deleteById(id);
	}

	@Override
	public void deployCompact(TableCompactTask task) {
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
	public void unDeployCompact(Integer id) {
		if (tableCompactTaskRepository.existsById(id)) {
			TableCompactTask task = tableCompactTaskRepository.findById(id).get();
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

	private JobDataMap getJobDataMap(TableCompactTask task) {
		JobDataMap map = new JobDataMap();
		map.put("COMPACT_ID", task.getId());
		return map;
	}

	private JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map) {
		return JobBuilder.newJob(TableCompactJob.class).withIdentity(jobKey).withDescription(description)
				.setJobData(map).storeDurably().build();
	}

	private Trigger getTrigger(TableCompactTask task) {
		return TriggerBuilder.newTrigger().withIdentity("COMPACT_" + task.getId(), "COMPACT_" + task.getId())
				.withSchedule(CronScheduleBuilder.cronSchedule(task.getCron())).build();
	}

	private JobKey getJobKey(TableCompactTask task) {
		return JobKey.jobKey("COMPACT", "" + task.getId());
	}

	private TriggerKey getTriggerKey(TableCompactTask task) {
		return TriggerKey.triggerKey("COMPACT_" + task.getId(), "COMPACT_" + task.getId());
	}

}
