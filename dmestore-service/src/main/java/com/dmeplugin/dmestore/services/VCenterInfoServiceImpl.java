package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.DMEDao;
import com.dmeplugin.dmestore.dao.VCenterInfoDao;
import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.VcenterException;
import com.dmeplugin.dmestore.utils.CipherUtils;
import com.dmeplugin.dmestore.utils.ThumbprintsUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class VCenterInfoServiceImpl extends DMEOpenApiService implements VCenterInfoService {

  @Autowired
  private VCenterInfoDao vCenterInfoDao;

  public VCenterInfoDao getvCenterInfoDao() {
    return vCenterInfoDao;
  }

  public void setvCenterInfoDao(VCenterInfoDao vCenterInfoDao) {
    this.vCenterInfoDao = vCenterInfoDao;
  }



  @Autowired
  private VCenterInfoService vCenterInfoService;

  @Override
  public int addVCenterInfo(VCenterInfo vCenterInfo) throws SQLException {
    return vCenterInfoDao.addVCenterInfo(vCenterInfo);
  }

  private void encode(VCenterInfo vCenterInfo) {
    vCenterInfo.setPassword(CipherUtils.aesEncode(vCenterInfo.getPassword()));
  }

  @Override
  public int saveVCenterInfo(final VCenterInfo vCenterInfo, final HttpSession session)
      throws SQLException {
    VCenterInfo vCenterInfo1 = vCenterInfoDao.getVCenterInfo();
    int returnValue = 0;
    boolean supportHA = true;
    boolean isAlarmNewEnabled = false;
    boolean isHANewEnabled = false;


      if (vCenterInfo1 != null) {
        isHANewEnabled = (vCenterInfo.isState() && !vCenterInfo1.isState());
        isAlarmNewEnabled = (vCenterInfo.isPushEvent() && !vCenterInfo1.isPushEvent());
        // update
        vCenterInfo1.setUserName(vCenterInfo.getUserName());
        vCenterInfo1.setState(vCenterInfo.isState());
        vCenterInfo1.setPushEvent(vCenterInfo.isPushEvent());
        vCenterInfo1.setPushEventLevel(vCenterInfo.getPushEventLevel());
        vCenterInfo1.setHostIp(vCenterInfo.getHostIp());
        vCenterInfo1.setHostPort(vCenterInfo.getHostPort());
        if (vCenterInfo.getPassword() != null && !"".equals(vCenterInfo.getPassword())) {
          vCenterInfo1.setPassword(vCenterInfo.getPassword());
          encode(vCenterInfo1);
        }


        returnValue = vCenterInfoDao.updateVCenterInfo(vCenterInfo1);
      } else {
        // insert
        encode(vCenterInfo);



        returnValue = addVCenterInfo(vCenterInfo);
      }



    return returnValue;
  }

  @Override
  public Map<String, Object> findVCenterInfo() throws SQLException {
    Map<String, Object> returnMap = new HashMap<>();
    VCenterInfo vCenterInfo = vCenterInfoDao.getVCenterInfo();
    if (vCenterInfo != null) {
      returnMap.put("USER_NAME", vCenterInfo.getUserName());
      returnMap.put("STATE", vCenterInfo.isState());
      returnMap.put("HOST_IP", vCenterInfo.getHostIp());
      returnMap.put("HOST_PORT", vCenterInfo.getHostPort());
      returnMap.put("PUSH_EVENT", vCenterInfo.isPushEvent());
      returnMap.put("PUSH_EVENT_LEVEL", vCenterInfo.getPushEventLevel());
    }
    return returnMap;
  }

  @Override
  public VCenterInfo getVCenterInfo() throws SQLException {
    return vCenterInfoDao.getVCenterInfo();
  }

  @Override
  public boolean disableVCenterInfo() {
    try {
      return vCenterInfoDao.disableVCenterInfo();
    } catch (SQLException e) {
      LOGGER.error("Failed to disable vCenter info");
      return false;
    }
  }

  @Override
  public void deleteHAData() {
    try {
      vCenterInfoDao.deleteHAData();
    } catch (Exception e) {
      throw new VcenterException(e.getMessage());
    }
  }

  @Override
  public void deleteHASyncAndDeviceData() {
    vCenterInfoDao.deleteHASyncAndDeviceData();
  }




  @Override
  public synchronized int saveJksThumbprints(InputStream inputStream, String password) {
    try {
      String[] thumbprints = ThumbprintsUtils.getThumbprintsFromJKS(inputStream, password);
      String[] tp = vCenterInfoDao.mergeSaveAndLoadAllThumbprints(thumbprints);
      LOGGER.info("Thumbprints have been saved, new list size: " + tp.length);
      ThumbprintsUtils.updateContextTrustThumbprints(tp);
      return RESULT_SUCCESS_CODE;
    } catch (IOException e) {
      LOGGER.warn("Cannot get thumbprints from JKS " + e.getMessage());
      return RESULT_READ_CERT_ERROR;
    } catch (Exception e) {
      LOGGER.error("Cannot get/save thumbprints from JKS " + e.getMessage());
      return FAIL_CODE;
    }
  }

  @Override
  public synchronized void saveThumbprints(String[] thumbprints) {
    try {
      String[] tp = vCenterInfoDao.mergeSaveAndLoadAllThumbprints(thumbprints);
      LOGGER.info("Thumbprints have been saved, new list size: " + tp.length);
      ThumbprintsUtils.updateContextTrustThumbprints(tp);
    } catch (Exception e) {
      LOGGER.warn("Cannot save thumbprints: " + e.getMessage());
    }
  }

  @Override
  public String[] getThumbprints() throws SQLException {
    String[] thumbprints = vCenterInfoDao.loadThumbprints();
    LOGGER.info("Thumbprints have been loaded, size: " + thumbprints.length);
    return thumbprints;
  }


}
