package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.dao.H2DataBaseDao;
import com.dmeplugin.dmestore.utils.FileUtils;
import com.dmeplugin.dmestore.utils.VCClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class InstantiationBeanServiceImpl implements
     InstantiationBeanService {

  private static final Logger LOGGER = LoggerFactory.getLogger(InstantiationBeanServiceImpl.class);

  private SystemService systemService;


  @Autowired
  private VCenterInfoService vCenterInfoService;



  public void onApplicationEvent(ContextRefreshedEvent event) {
    init();
  }

  public SystemService getSystemService() {
    return systemService;
  }

  public void setSystemService(SystemService systemService) {
    this.systemService = systemService;
  }

  @Override
  public void init() {
    try {


      LOGGER.info("OS: " + System.getProperty("os.name"));
      LOGGER.info("Current web client: " + VCClientUtils.getWebClient());
      LOGGER.info("Is HTML5 client: " + VCClientUtils.isHtml5Client());
      LOGGER.info("Is Flash client: " + VCClientUtils.isFlashClient());

      // (linux only) MV files
      if (!FileUtils.isWindows()) { // Linux
        try {
          File newDbFile = new File(FileUtils.getPath(true) + "/" + H2DataBaseDao.getDbFileName());
          String oldDbFile = FileUtils.getOldDbFolder() + "/" + H2DataBaseDao.getDbFileName();
          if (new File(oldDbFile).exists() && !newDbFile.exists()) { // no DB file in new path
            // move db file
            LOGGER.info("Copying DB file from " + H2DataBaseDao.getDbFileName() + " to " + newDbFile
                .getName());
            Files.copy(Paths.get(oldDbFile),
                Paths.get(newDbFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            // move key files
            String oldFolder = FileUtils.getOldFolder();
            String newFolder = FileUtils.getPath();
            LOGGER.info("Copying key files...");
            Files.copy(Paths.get(oldFolder + "/" + FileUtils.BASE_FILE_NAME),
                Paths.get(newFolder + "/" + FileUtils.BASE_FILE_NAME),
                StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(oldFolder + "/" + FileUtils.WORK_FILE_NAME),
                Paths.get(newFolder + "/" + FileUtils.WORK_FILE_NAME),
                StandardCopyOption.REPLACE_EXISTING);
            FileUtils.setFilePermission(new File(newFolder + "/" + FileUtils.BASE_FILE_NAME));
            FileUtils.setFilePermission(new File(newFolder + "/" + FileUtils.WORK_FILE_NAME));
          }
        } catch (Exception e) {
          LOGGER.warn("Cannot move file");
        }
      }

      systemService.initDb();

      //ThumbprintsUtils.updateContextTrustThumbprints(vCenterInfoService.getThumbprints());



    } catch (Exception e) {
      LOGGER.warn( e.getMessage());
    }
  }
}
