package com.dmeplugin.dmestore.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * @ClassName: DmeInfo
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class DmeInfo implements Serializable {

  private int id;
  private String hostIp;
  private int hostPort;
  private String userName;
  private String password;
  private Date createTime;
  private Date updateTime;
  private int state; 

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

  public int getHostPort() {
    return hostPort;
  }

  public void setHostPort(int hostPort) {
    this.hostPort = hostPort;
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

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }
}
