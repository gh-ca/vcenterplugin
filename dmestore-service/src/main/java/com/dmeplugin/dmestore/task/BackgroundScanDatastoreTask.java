package com.dmeplugin.dmestore.task;


import com.dmeplugin.dmestore.services.DmeNFSAccessService;
import com.dmeplugin.dmestore.services.VmfsAccessService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackgroundScanDatastoreTask implements StatefulJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundScanDatastoreTask.class);

  @Autowired
  private VmfsAccessService vmfsAccessService;

  @Autowired
  private DmeNFSAccessService dmeNFSAccessService;

  /**
   * 后台定时
   */
  public void scanDatastore(){
    LOGGER.info("scanDatastore start");
    //扫描vmfs
    try {
      vmfsAccessService.scanVmfs();
    }catch (Exception e){
      e.printStackTrace();
    }
    //扫描nfs
    try {
      dmeNFSAccessService.scanNfs();
    }catch (Exception e){
      e.printStackTrace();
    }
    LOGGER.info("scanDatastore end");
  }

  public void execute() {
    LOGGER.info("scanDatastore rrr start");
  }

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    LOGGER.info("scanDatastore rrr start");
  }
}
