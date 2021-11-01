package com.huawei.dmestore.task;

import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.services.BestPracticeProcessService;

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
        try {
            long t1 = System.currentTimeMillis();
            LOGGER.info("CheckBestPractise Start");
            Object obj = ApplicationContextHelper.getBean("BestPracticeProcessServiceImpl");
            BestPracticeProcessService bestPracticeProcessService = (BestPracticeProcessService) obj;
            bestPracticeProcessService.check(null);
            long t2 = System.currentTimeMillis();
            LOGGER.info("CheckBestPractise End!Take {}ms", t2 -t1);

            LOGGER.info("BestPractise Up Start");
            // ������˺�ִ���Զ�����
            bestPracticeProcessService.update(null, null, true);
            long t3 = System.currentTimeMillis();
            LOGGER.info("BestPractise Up End!Take {}ms", t3 -t2);
        } catch (VcenterException e) {
            LOGGER.error("CheckBestPractise error", e);
        } catch (DmeSqlException e) {
            LOGGER.error("BestPractise Up Error", e);
        }
    }
}
