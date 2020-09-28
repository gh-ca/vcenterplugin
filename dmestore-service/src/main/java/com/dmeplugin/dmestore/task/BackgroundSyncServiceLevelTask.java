package com.dmeplugin.dmestore.task;


import com.dmeplugin.dmestore.services.BestPracticeProcessService;
import com.dmeplugin.dmestore.services.ServiceLevelService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackgroundSyncServiceLevelTask implements StatefulJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundSyncServiceLevelTask.class);


  public void execute() {
    LOGGER.info("CheckBestPractise start");
  }

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    LOGGER.info("updateVmwarePolicy start");
    try {
      Object obj=ApplicationContextHelper.getBean("ServiceLevelServiceImpl");
      ((ServiceLevelService)obj).updateVmwarePolicy();
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("updateVmwarePolicy error",e);
    }
    LOGGER.info("updateVmwarePolicy end");
  }
}
