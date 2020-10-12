package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.VCenterInfoDao;
import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.utils.CipherUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
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

    try {
      vCenterInfo.setPassword(CipherUtils.encryptString(vCenterInfo.getPassword()));
    } catch (Exception e) {
      e.printStackTrace();
    }

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



}
