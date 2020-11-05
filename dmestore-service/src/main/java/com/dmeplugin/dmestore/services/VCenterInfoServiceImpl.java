package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.VCenterInfoDao;
import com.dmeplugin.dmestore.entity.VCenterInfo;
import com.dmeplugin.dmestore.exception.DmeSqlException;
import org.springframework.beans.factory.annotation.Autowired;

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
  public int addVcenterInfo(VCenterInfo vCenterInfo) throws DmeSqlException {
    return vCenterInfoDao.addVcenterInfo(vCenterInfo);
  }



  @Override
  public int saveVcenterInfo(final VCenterInfo vCenterInfo) throws DmeSqlException {
    VCenterInfo vCenterInfo1 = vCenterInfoDao.getVcenterInfo();
    int returnValue = 0;
    boolean supportHa = true;
    boolean isAlarmNewEnabled = false;
    boolean isHaNewEnabled = false;


      if (vCenterInfo1 != null) {
        isHaNewEnabled = (vCenterInfo.isState() && !vCenterInfo1.isState());
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

        }

        returnValue = vCenterInfoDao.updateVcenterInfo(vCenterInfo1);
      } else {

        returnValue = addVcenterInfo(vCenterInfo);
      }



    return returnValue;
  }

  @Override
  public Map<String, Object> findVcenterInfo() throws DmeSqlException {
    Map<String, Object> returnMap = new HashMap<>();
    VCenterInfo vCenterInfo = vCenterInfoDao.getVcenterInfo();
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
  public VCenterInfo getVcenterInfo() throws DmeSqlException {
    return vCenterInfoDao.getVcenterInfo();
  }



}
