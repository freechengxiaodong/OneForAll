package com.test.quartz.service;

import com.test.quartz.Jobs.BaseJob;
import org.quartz.Scheduler;

import java.util.ArrayList;
import java.util.List;

public interface JobService {

    public static List<BaseJob> jobList = new ArrayList<>();

    public void executeJobs();
}
