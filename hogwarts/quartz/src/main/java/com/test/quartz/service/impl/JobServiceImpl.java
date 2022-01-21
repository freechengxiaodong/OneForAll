package com.test.quartz.service.impl;

import com.test.quartz.Jobs.BaseJob;
import com.test.quartz.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    Scheduler scheduler;

    @Override
    public void executeJobs() {

        if(!CollectionUtils.isEmpty(JobService.jobList)){
            for (BaseJob job : jobList) {
                try {
                    job.startJob(scheduler);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
