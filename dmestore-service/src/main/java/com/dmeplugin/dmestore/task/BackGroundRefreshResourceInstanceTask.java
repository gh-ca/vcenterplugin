package com.dmeplugin.dmestore.task;

import com.dmeplugin.dmestore.services.DmeRelationInstanceService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @ClassName: BackGroundRefreshResourceInstanceTask
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-11-02
 **/
@Component
public class BackGroundRefreshResourceInstanceTask implements StatefulJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackGroundRefreshResourceInstanceTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("refreshResourceInstance start");
        long consume = 0;
        try {
            Object obj = ApplicationContextHelper.getBean("DmeRelationInstanceServiceImpl");
            long start = System.currentTimeMillis();
            ((DmeRelationInstanceService) obj).refreshResourceInstance();
            long end = System.currentTimeMillis();
            consume = end - start;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("refreshResourceInstance error", e);
        }
        LOGGER.info("refreshResourceInstance end comsum:{}ms.", consume);
    }
}

