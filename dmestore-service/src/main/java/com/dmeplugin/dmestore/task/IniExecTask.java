package com.dmeplugin.dmestore.task;

import com.dmeplugin.dmestore.services.BestPracticeProcessService;
import com.dmeplugin.dmestore.services.ServiceLevelService;
import com.dmeplugin.dmestore.services.SystemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

/**
 * @ClassName IniExecTask
 * @Description TODO
 * @Author yuanqi
 * @Date 2020/9/24 9:48
 * @Version V1.0
 **/

@Component
public class IniExecTask implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(BackgroundScanDatastoreTask.class);

    @Autowired
    private ScheduleSetting scheduleSetting;

    @Autowired
    private BackgroundScanDatastoreTask backgroundScanDatastoreTask;

    @Autowired
    private SystemServiceImpl systemService;



    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //重启插件时执行一次
        if (event.getApplicationContext().getParent() == null) {
            systemService.initDb();
            initTask();
            iniScheduleTask();
        }
    }

    private void initTask() {
        ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        mScheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                LOG.info("--->ini Scan Datastore Task...start");
                backgroundScanDatastoreTask.scanDatastore();
                LOG.info("--->ini Scan Datastore Task...end");
            }
        }, 10, TimeUnit.SECONDS);
    }

    private void iniScheduleTask() {

                LOG.info("--->ini iniScheduleTask Task...start");
                try {
                    scheduleSetting.reconfigureTasks();
                }catch (Exception e){
                    LOG.error("--->err",e);
                }
                LOG.info("--->ini iniScheduleTask Task...end");
            }

}

