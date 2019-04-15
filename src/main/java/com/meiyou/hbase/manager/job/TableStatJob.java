package com.meiyou.hbase.manager.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.meiyou.hbase.manager.entity.TableStatTask;
import com.meiyou.hbase.manager.service.TableStatTaskService;

import lombok.extern.slf4j.Slf4j;

@DisallowConcurrentExecution
@Component
@Slf4j
public class TableStatJob extends BaseJob {
	private static final long serialVersionUID = -7552839982422590753L;

	@Override
	public void action(JobExecutionContext context) {
		try {
			SchedulerContext schedulerContext = context.getScheduler().getContext();
			ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get(APPLICATION_CONTEXT_KEY);
			// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
			JobDataMap map = context.getMergedJobDataMap();
			int statId = map.getInt("STAT_ID");
			TableStatTaskService tableStatTaskService = applicationContext.getBean("tableStatTaskServiceImpl",
					TableStatTaskService.class);
			TableStatTask task = tableStatTaskService.findById(statId);
			log.info("Run stat task: {}", statId);
			tableStatTaskService.executeStatTask(task.getClusterName(), task.getTableName());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}