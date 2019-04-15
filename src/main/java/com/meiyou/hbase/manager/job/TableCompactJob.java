package com.meiyou.hbase.manager.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.meiyou.hbase.manager.entity.TableCompactTask;
import com.meiyou.hbase.manager.service.TableCompactTaskService;
import com.meiyou.hbase.manager.service.TableService;

import lombok.extern.slf4j.Slf4j;

@DisallowConcurrentExecution
@Component
@Slf4j
public class TableCompactJob extends BaseJob {
	private static final long serialVersionUID = -3355150297485576727L;

	@Override
	public void action(JobExecutionContext context) {
		try {
			SchedulerContext schedulerContext = context.getScheduler().getContext();
			ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get(APPLICATION_CONTEXT_KEY);
			// JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
			JobDataMap map = context.getMergedJobDataMap();
			int compactId = map.getInt("COMPACT_ID");
			TableCompactTaskService tableCompactTaskService = applicationContext.getBean("tableCompactTaskServiceImpl",
					TableCompactTaskService.class);
			TableCompactTask task = tableCompactTaskService.findById(compactId);
			log.info("Run compact task: {}", compactId);
			TableService tableService = applicationContext.getBean("tableServiceImpl", TableService.class);
			tableService.compactTable(task.getClusterName(), task.getTableName());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}