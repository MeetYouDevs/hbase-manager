package com.meiyou.hbase.manager.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.Serializable;

@Slf4j
public abstract class BaseJob implements Job, Serializable {
	private static final long serialVersionUID = 7232406628140848026L;
	
	protected final String APPLICATION_CONTEXT_KEY = "applicationContext";

    public abstract void action(JobExecutionContext context);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long start = System.currentTimeMillis();
        this.action(jobExecutionContext);
        long end = System.currentTimeMillis();
        long cost = (end-start);
        if (cost > 2000) {
            log.warn("slowJob: job: {}, trigger: {}, cost: {} ms", jobExecutionContext.getJobDetail().getKey(),
                    jobExecutionContext.getTrigger().getKey(), (end - start));
        } else {
            log.info("job: {}, trigger: {}, cost: {} ms", jobExecutionContext.getJobDetail().getKey(),
                    jobExecutionContext.getTrigger().getKey(), (end - start));
        }
    }

}
