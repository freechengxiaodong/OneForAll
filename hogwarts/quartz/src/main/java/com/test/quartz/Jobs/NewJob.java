package com.test.quartz.Jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NewJob extends BaseJob {
    @Override
    public String getCron() {
        return "0/5 * * * * ?";
    }

    @Override
    public String getJobName() {
        return "new-job";
    }

    @Override
    public String getJobGroup() {
        return "test-job";
    }

    @Override
    public String getTriggerName() {
        return "new-trigger";
    }

    @Override
    public String getTriggerGroup() {
        return "test-trigger";
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("new job is running");
    }
}
