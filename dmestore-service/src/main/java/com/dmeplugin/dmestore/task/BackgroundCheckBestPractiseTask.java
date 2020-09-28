package com.dmeplugin.dmestore.task;


import com.dmeplugin.dmestore.services.BestPracticeProcessService;
import com.dmeplugin.dmestore.services.ServiceLevelService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackgroundCheckBestPractiseTask implements StatefulJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundCheckBestPractiseTask.class);



  public void execute() {
    LOGGER.info("CheckBestPractise start");
  }

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    LOGGER.info("CheckBestPractise start");
    try {
      Object obj=ApplicationContextHelper.getBean("BestPracticeProcessServiceImpl");
      ((BestPracticeProcessService)obj).check(null);
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("CheckBestPractise error",e);
    }
    LOGGER.info("CheckBestPractise end");
  }
}
