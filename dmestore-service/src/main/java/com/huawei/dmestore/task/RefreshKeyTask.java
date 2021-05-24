package com.huawei.dmestore.task;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.services.RefreshKeyService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RefreshKeyTask implements StatefulJob {
    private static final Logger LOG = LoggerFactory.getLogger(RefreshKeyTask.class);
    //@Scheduled(cron = "0 0 0 1 * ?")
	//@Scheduled(cron = "0 0/5 * * * ?")


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("RefreshKeyTask start");
        try {
            Object obj = ApplicationContextHelper.getBean("RefreshKeyServiceImpl");
            ((RefreshKeyService) obj).refreshKey();
        } catch (DmeException e) {
            LOG.error("updateVmwarePolicy error", e);
        }
        LOG.info("RefreshKeyTask end");
    }
}
