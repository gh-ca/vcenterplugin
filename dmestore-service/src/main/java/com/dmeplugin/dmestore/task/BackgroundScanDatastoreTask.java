package com.dmeplugin.dmestore.task;


import com.dmeplugin.dmestore.services.BestPracticeProcessService;
import com.dmeplugin.dmestore.services.DmeNFSAccessService;
import com.dmeplugin.dmestore.services.VmfsAccessService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @ClassName: BackgroundScanDatastoreTask
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
@Component
public class BackgroundScanDatastoreTask implements StatefulJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundScanDatastoreTask.class);

  /**
   * 后台定时
   */
  public void scanDatastore(){
    LOGGER.info("scanDatastore start");
    //扫描vmfs
    try {
      Object obj=ApplicationContextHelper.getBean("VmfsAccessServiceImpl");
      ((VmfsAccessService)obj).scanVmfs();
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("scanDatastore vmfs error",e);
    }
    //扫描nfs
    try {
      Object obj=ApplicationContextHelper.getBean("DmeNFSAccessServiceImpl");
      ((DmeNFSAccessService)obj).scanNfs();
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("scanDatastore nfs error",e);
    }
    LOGGER.info("scanDatastore end");
  }

  public void execute() {
    LOGGER.info("scanDatastore rrr start");
  }

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    scanDatastore();
  }
}
