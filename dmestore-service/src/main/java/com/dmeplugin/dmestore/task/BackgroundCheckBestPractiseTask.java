package com.dmeplugin.dmestore.task;

import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.dmestore.services.BestPracticeProcessService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * BackgroundCheckBestPractiseTask
 *
 * @author Administrator
 * @since 2020-12-08
 */
@Component
public class BackgroundCheckBestPractiseTask implements StatefulJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundCheckBestPractiseTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("CheckBestPractise start");
        try {
            Object obj = ApplicationContextHelper.getBean("BestPracticeProcessServiceImpl");
            ((BestPracticeProcessService) obj).check(null);
        } catch (VcenterException e) {
            LOGGER.error("CheckBestPractise error", e);
        }
        LOGGER.info("CheckBestPractise end");
    }
}
