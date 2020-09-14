package com.dmeplugin.vcenter.utils;

import com.vmware.common.Main;
import com.vmware.common.ssl.TrustAll;
import com.vmware.connection.BasicConnection;
import com.vmware.connection.ConnectedVimServiceBase;
import com.vmware.connection.Connection;
import com.vmware.vim25.AlarmExpression;
import com.vmware.vim25.AlarmInfo;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.EventAlarmExpression;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.OrAlarmExpression;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RetrieveResult;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConnectedVim extends ConnectedVimServiceBase {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectedVim.class);

  public ConnectedVim() {
    super();
  }

  /**
   * 连接vim
   */
  private void connect(String host, String username, String password, String port) {
    Connection basicConnection = new BasicConnection();
    URL sdkUrl = null;
    try {
      sdkUrl = new URL("https", host, Integer.parseInt(port), "/sdk");
    } catch (MalformedURLException e) {
      throw new RuntimeException("connect vim fail.");
    }
    basicConnection.setPassword(password);
    basicConnection.setUrl(sdkUrl.toString());
    basicConnection.setUsername(username);
    this.setIgnoreCert();
    this.setHostConnection(true);
    this.setConnection(basicConnection);
    //LOGGER.info("host: " + host + " username: " + username + " password: ******");
    this.connect();
//    if (connect == null || !connect.isConnected()) {
//      throw new RuntimeException("connect vim fail.");
//    }
    //LOGGER.info("connect vim success.");
  }

  @Override
  public Connection disconnect() {
    try {
      if (connection != null) {
        return super.disconnect();
      }
    } catch (Exception e) {
      //LOGGER.warn("Failed to disconnect vcenter, " + e.getMessage());
    }
    return connection;
  }



  private void setIgnoreCert() {
    System.setProperty(Main.Properties.TRUST_ALL, Boolean.TRUE.toString());
    try {
      TrustAll.trust();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      //LOGGER.error(e.getMessage());
    }
  }

}
