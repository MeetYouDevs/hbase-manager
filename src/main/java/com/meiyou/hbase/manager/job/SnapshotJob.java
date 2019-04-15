package com.meiyou.hbase.manager.job;

import com.meiyou.hbase.manager.service.SnapshotTaskService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

public class SnapshotJob extends BaseJob {
	private static final long serialVersionUID = -993636178814626382L;

	@Override
    public void action(JobExecutionContext context) {
        try {
            SchedulerContext schedulerContext  = context.getScheduler().getContext();
            ApplicationContext applicationContext = (ApplicationContext) schedulerContext.get(APPLICATION_CONTEXT_KEY);

            //JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
            JobDataMap map = context.getMergedJobDataMap();
            Integer snapshotId = map.getInt("SNAPSHOT_ID");
            SnapshotTaskService snapshotTaskService = applicationContext.getBean("snapshotTaskServiceImpl", SnapshotTaskService.class);
            snapshotTaskService.runCreateSnapshot(snapshotId,context.getFireTime());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
