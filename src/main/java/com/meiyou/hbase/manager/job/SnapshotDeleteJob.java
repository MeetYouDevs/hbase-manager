package com.meiyou.hbase.manager.job;

import com.meiyou.hbase.manager.service.SnapshotTaskService;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class SnapshotDeleteJob extends BaseJob{
	private static final long serialVersionUID = 7196244664190184088L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotDeleteJob.class);

	@Override
    public void action(JobExecutionContext context) {
        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();
            ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get(APPLICATION_CONTEXT_KEY);

            SnapshotTaskService snapshotTaskService = applicationContext.getBean("snapshotTaskServiceImpl", SnapshotTaskService.class);
            snapshotTaskService.runDeleteSnapshot(context.getFireTime());

        } catch (SchedulerException e) {
        	LOGGER.error(e.toString(), e);
        	e.printStackTrace();
        }
    }
}
