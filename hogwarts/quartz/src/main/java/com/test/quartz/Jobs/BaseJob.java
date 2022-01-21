package com.test.quartz.Jobs;

import com.test.quartz.service.JobService;
import org.quartz.*;

import javax.annotation.PostConstruct;

public abstract class BaseJob implements Job{

    @PostConstruct
    public void init(){
        JobService.jobList.add(this);
    }

    public abstract String getCron();
    public abstract String getJobName();
    public abstract String getJobGroup();
    public abstract String getTriggerName();
    public abstract String getTriggerGroup();

    public void startJob(Scheduler scheduler) throws SchedulerException{
        JobDetail jobDetail = JobBuilder.newJob(this.getClass())
                .withIdentity(getJobName(), getJobGroup())
                .build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(getCron());
        Trigger jobTrigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerName(), getTriggerGroup())
                .withSchedule(cronScheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail,jobTrigger);
    };
}
