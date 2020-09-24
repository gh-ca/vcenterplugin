package com.dmeplugin.dmestore.task;

import com.dmeplugin.dmestore.dao.ScheduleDao;
import com.dmeplugin.dmestore.entity.ScheduleConfig;
import com.dmeplugin.dmestore.services.InstantiationBeanService;
import com.dmeplugin.dmestore.services.SystemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleSetting implements SchedulingConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleSetting.class);
    private ScheduledTaskRegistrar taskRegistrar;
    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private InstantiationBeanService instantiationBeanService;

    private static List<ScheduleConfig> scheduleList;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //初始化表
        instantiationBeanService.init();
        // 获取所有任务
        this.taskRegistrar = scheduledTaskRegistrar;
        reconfigureTasks();
    }

    public void reconfigureTasks() {
        taskRegistrar.getTriggerTaskList().clear();

        // 获取所有任务
        scheduleList = scheduleDao.getScheduleList();
        LOGGER.info("schedule size="+scheduleList.size());
        for (ScheduleConfig s : scheduleList){
            taskRegistrar.addTriggerTask(getRunnable(s), getTrigger(s));
        }
    }

    //刷新任务
    public void refreshTasks(int taskId,String cron){
        // 获取所有任务
        if(scheduleList!=null) {
            LOGGER.info("schedule size=" + scheduleList.size());
            for (ScheduleConfig s : scheduleList) {
                if (s.getId() == taskId) {
                    s.setCron(cron);
                }
            }
        }
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
    private Runnable getRunnable(ScheduleConfig scheduleConfig){
        return new Runnable() {
            @Override
            public void run() {
                Class<?> clazz;
                try {
                    clazz = Class.forName(scheduleConfig.getClassName());
                    String className = lowerFirstCapse(clazz.getSimpleName());
                    Object bean = (Object) ApplicationContextHelper.getBean(className);
                    Method method = ReflectionUtils.findMethod(bean.getClass(), scheduleConfig.getMethod());
                    ReflectionUtils.invokeMethod(method, bean);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Trigger
     * @param scheduleConfig
     * @return
     */
    private Trigger getTrigger(ScheduleConfig scheduleConfig){
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(scheduleConfig.getCron());
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        };

    }
}
