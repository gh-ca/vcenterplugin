package com.dmeplugin.dmestore.task;

import com.dmeplugin.dmestore.dao.ScheduleDao;
import com.dmeplugin.dmestore.entity.ScheduleConfig;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class ScheduleSetting   {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleSetting.class);

    @Autowired
    private ScheduleDao scheduleDao;


    @Autowired
    private QuartzConfig quartzConfig;


    private static List<ScheduleConfig> scheduleList;



    public void reconfigureTasks() {

        // 获取所有任务
        scheduleList = scheduleDao.getScheduleList();
        LOGGER.info("schedule size="+scheduleList.size());
        for (ScheduleConfig s : scheduleList){
            try {
                Class clazz;
                clazz = Class.forName(s.getClassName());
                JobDetail job = newJob(clazz)
                        .withIdentity(s.getClassName(), "group1")
                        .build();
                quartzConfig.getScheduler().scheduleJob(job, getTrigger(s));
            }catch (Exception e){
                LOGGER.error("job error",e);
            }
        }
    }

    //刷新任务
    public void refreshTasks(Integer taskId,String cron){
        // 获取所有任务
        if(scheduleList!=null) {
            LOGGER.info("schedule size=" + scheduleList.size());
            for (ScheduleConfig s : scheduleList) {
                if (s.getId() == taskId) {
                    s.setCron(cron);
                }
            }
        }
        try {
            quartzConfig.getScheduler().clear();
        } catch (SchedulerException e) {
            LOGGER.error("quartzConfig job error",e);
        }
        reconfigureTasks();
    }


    /**
     * 转换首字母小写
     *
     * @param str
     * @return
     */
    public static String lowerFirstCapse(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * runnable
     * @param scheduleConfig
     * @return
     */
    private Job getRunnable(ScheduleConfig scheduleConfig){
        Class<?> clazz;
        Object bean = null;
        try {
            clazz = Class.forName(scheduleConfig.getClassName());
            String className = lowerFirstCapse(clazz.getSimpleName());
              bean = ApplicationContextHelper.getBean(className);

        }catch (ClassNotFoundException e){
            LOGGER.error("ClassNotFoundException",e);
        }

        return (Job) bean;
    }

    /**
     * Trigger
     * @param scheduleConfig
     * @return
     */
    private CronTrigger getTrigger(ScheduleConfig scheduleConfig){
        return newTrigger()
                .withIdentity(scheduleConfig.getClassName(), "group1")
                .startNow()
                .withSchedule(cronSchedule(scheduleConfig.getCron()))
                .build();


    }
}
