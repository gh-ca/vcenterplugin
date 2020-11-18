package com.dmeplugin.dmestore.entity;

import java.io.Serializable;
import java.util.Date;


public class VCenterInfo implements Serializable {

  private static final long serialVersionUID = 3811172759222907501L;

  private int id;
  private String hostIp;
  private int hostPort;
  private String userName;
  private String password;
  private Date createTime;
  // HA status
  private boolean state;
  // Alarm status
  private boolean pushEvent;
  // 1:crtical 2:majorandcritical 3:all
  private int pushEventLevel;

  public int getPushEventLevel() {
    return pushEventLevel;
  }

  public void setPushEventLevel(int pushEventLevel) {
    this.pushEventLevel = pushEventLevel;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getHostIp() {
    return hostIp;
  }

  public void setHostIp(String hostIp) {
    this.hostIp = hostIp;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public boolean isState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

  public boolean isPushEvent() {
    return pushEvent;
  }

  public void setPushEvent(boolean pushEvent) {
    this.pushEvent = pushEvent;
  }

  public int getHostPort() {
    return hostPort == 0 ? 443 : hostPort;
  }

  public void setHostPort(int hostPort) {
    this.hostPort = hostPort;
  }
}
