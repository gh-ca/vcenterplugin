package com.dmeplugin.dmestore.task;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


public class QuartzConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzConfig.class);
    private Scheduler scheduler;
    public Scheduler getScheduler(){
        StdSchedulerFactory stdSchedulerFactory=new StdSchedulerFactory();
        try {
            if (null==scheduler)
            scheduler = stdSchedulerFactory.getScheduler();
            scheduler.start();
        }catch (Exception e){
            LOGGER.error("get scheduler error",e);
        }

        return scheduler;
    }

    public void destory(){
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOGGER.error("shutdown scheduler error",e);
        }
    }
}
