package com.dmeplugin.dmestore.task;


import com.dmeplugin.dmestore.services.BestPracticeProcessService;
import com.dmeplugin.dmestore.services.ServiceLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackgroundSyncTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundSyncTask.class);

  @Autowired
  private ServiceLevelService serviceLevelService;

  @Autowired
  private BestPracticeProcessService bestPracticeProcessService;

  /**
   * 后台定时
   */
  public void syncServiceLevel(){
    LOGGER.info("updateVmwarePolicy start");
    serviceLevelService.updateVmwarePolicy();
    LOGGER.info("updateVmwarePolicy end");
  }

  public void syncCheckBestPractise(){
    LOGGER.info("CheckBestPractise start");
    try {
      bestPracticeProcessService.check(null);
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("CheckBestPractise error");
    }
    LOGGER.info("CheckBestPractise end");
  }

}
