package com.meiyou.hbase.manager.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.meiyou.hbase.manager.constant.Constant;
import com.meiyou.hbase.manager.entity.MigrationTask;
import com.meiyou.hbase.manager.service.MigrationTaskService;

import lombok.extern.slf4j.Slf4j;

@DisallowConcurrentExecution
@Component
@Slf4j
public class MigrationJob extends BaseJob {
	private static final long serialVersionUID = -1265544216863292742L;

	@Override
	public void action(JobExecutionContext context) {
		try {
			SchedulerContext schedulerContext = context.getScheduler().getContext();
			ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get(APPLICATION_CONTEXT_KEY);
			// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
			JobDataMap map = context.getMergedJobDataMap();
			int migrationId = map.getInt("MIG_ID");
			MigrationTaskService service = applicationContext.getBean("migrationTaskServiceImpl",
					MigrationTaskService.class);
			MigrationTask task = service.findById(migrationId);
			int logId = service.createTaskLog(task, context.getFireTime(), Constant.FIRE_TYPE_AUTO);
			log.info("run Migration task:{},logId:{}", migrationId, logId);
			service.runMigration(logId, task, context.getFireTime());

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}