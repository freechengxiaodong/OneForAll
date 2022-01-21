package com.test.quartz.Jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestJob extends BaseJob {
    @Override
    public String getCron() {
        return "0/3 * * * * ?";
    }

    @Override
    public String getJobName() {
        return "test-job";
    }

    @Override
    public String getJobGroup() {
        return "test-group";
    }

    @Override
    public String getTriggerName() {
        return "test-trigger";
    }

    @Override
    public String getTriggerGroup() {
        return "test-trigger";
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Test job is running");
    }
}
