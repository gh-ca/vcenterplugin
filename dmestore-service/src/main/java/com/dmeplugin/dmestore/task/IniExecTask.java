package com.dmeplugin.dmestore.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private BackgroundScanDatastoreTask backgroundScanDatastoreTask;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //重启插件时执行一次
        if (event.getApplicationContext().getParent() == null) {
            iniScanDatastoreTask();
        }
    }

    private void iniScanDatastoreTask() {
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

    private void test() {

//        System.out.println("spring容易初始化完毕================================================");
//        backgroundScanDatastoreTask.scanDatastore();
    }
}

