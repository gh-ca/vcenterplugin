package com.dmeplugin.vcenter.utils;

import com.dmeplugin.vcenter.Constants;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;


public class Validations {

  protected static final Logger LOGGER = LoggerFactory.getLogger(Validations.class);

  private static final String RESTURL_VCENTERACTION = "https://%s/ui/dmestore/rest/registerservice/pluginaction";

  private static final HttpHeaders HEADERS = new HttpHeaders();

  private static final JsonParser JSON_PARSER = new JacksonJsonParser();

  static {
    HEADERS.add("Content-Type", "application/x-www-form-urlencoded");
  }





  public static Map onSubmit(String packageUrl, String vcenterUsername, String vcenterPassword,
                             String vcenterIp, String vcenterPort, String version, String dmeip, String dmeport, String dmeusername, String dmepassword) {
    if (!packageUrl.startsWith("https://")) {
      return Collections.singletonMap("error", "E002");
    }
    if ((vcenterIp == null) || (vcenterIp.isEmpty())) {
      return Collections.singletonMap("error", "E003");
    }
    if ((vcenterPort == null) || (vcenterPort.isEmpty())) {
      return Collections.singletonMap("error", "E004");
    }
    String serverThumbprint;
    try {
      serverThumbprint = KeytookUtil.getKeystoreServerThumbprint();
    } catch (IOException e) {
      e.printStackTrace();
      return Collections.singletonMap("error", "E005");
    }
    String pluginKey = "com.dme.vcenterpluginui";
    String url;
    try {
      url = encodeUrlFileName(packageUrl);
    } catch (UnsupportedEncodingException e) {
      url = packageUrl;
    }

    VcenterRegisterRunner
            .run(version, url, serverThumbprint, vcenterIp, vcenterPort, vcenterUsername,
                    vcenterPassword, pluginKey);




    //安装插件完成，后台运行添加dme信息进程
    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

    executor.execute(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            Thread.sleep(20000);
          } catch (InterruptedException e) {
            LOGGER.info("thread sleep error");
          }
          String response = vcenteraction(vcenterIp, vcenterUsername, vcenterPassword, vcenterPort,
                  "install", false, dmeip, dmeport, dmeusername, dmepassword);
          if (response != null) {
            try {
              Map<String, Object> result = JSON_PARSER.parseMap(response);
              String resultCode = (String) result.get("code");
              if ("-99999".equals(resultCode)) {
                LOGGER.info((String) result.get("description"));
              }
              if ("200".equals(resultCode)) {
                LOGGER.info("add dme info to vcenter success");
              }
            } catch (Exception e) {
              LOGGER.info("Cannot add dme info in vcenter");
            }

            break;
          } else {
            LOGGER.info("service is not ready wait for ready");
          }
        }
      }
    });

    return Collections.singletonMap("info", "check log");
  }



  private static String encodeUrlFileName(String url) throws UnsupportedEncodingException {
    String path = url.substring(0, url.indexOf("/package/") + "/package/".length());
    String file = URLEncoder.encode(url.substring(path.length()), "UTF-8").replaceAll("\\+", "%20");
    return path + file;
  }

  public static Map unRegister(String packageUrl, String vcenterUsername, String vcenterPassword,
                               String vcenterIp, String vcenterPort, String keepData) {
    if ((vcenterIp == null) || (vcenterIp.isEmpty())) {
      return Collections.singletonMap("error", "E003");
    }
    if ((vcenterPort == null) || (vcenterPort.isEmpty())) {
      return Collections.singletonMap("error", "E004");
    }

    LOGGER.info("After the plugin is uninstalled, please restart the vSphere Web Client service manually");

    String pluginKey = "com.dme.vcenterpluginui";
    boolean removeData = false;
    LOGGER.info("Removing vCenter plugin data, please wait patiently...");
    if (keepData != null && !"1".equals(keepData)) {
      removeData = true;
    }
    String response = vcenteraction(vcenterIp, vcenterUsername, vcenterPassword, vcenterPort, "uninstall",
        removeData);
    if (response != null) {
      try {
        Map<String, Object> result = JSON_PARSER.parseMap(response);
        String resultCode = (String) result.get("code");
        if ("-99999".equals(resultCode)) {
         
          LOGGER.info( (String) result.get("description"));
        } else if ("-1".equals(resultCode)) {
          LOGGER.info("No HA provider can be removed.");
          VcenterRegisterRunner.unRegister(vcenterIp, vcenterPort, vcenterUsername,
              vcenterPassword, pluginKey);
        } else if ("-70001".equals(resultCode)) { // DB Exceptions
          LOGGER.info("No service to uninstall provider");
          VcenterRegisterRunner.unRegister(vcenterIp, vcenterPort, vcenterUsername,
              vcenterPassword, pluginKey);
        } else {
          LOGGER.info("HA Provider has been removed.");
          VcenterRegisterRunner.unRegister(vcenterIp, vcenterPort, vcenterUsername,
              vcenterPassword, pluginKey);
        }
      } catch (Exception e) {
        LOGGER.info("Cannot uninstall provider");
        VcenterRegisterRunner.unRegister(vcenterIp, vcenterPort, vcenterUsername,
            vcenterPassword, pluginKey);
      }
    } else {
      LOGGER.info("No service to uninstall provider");
      VcenterRegisterRunner.unRegister(vcenterIp, vcenterPort, vcenterUsername,
          vcenterPassword, pluginKey);
    }
    return Collections.singletonMap("info", "check log");
  }

  public static String vcenteraction(String vcenterIp, String vcenterUsername,
                                     String vcenterPassword, String vcenterPort, String action, boolean removeData, String dmeIp, String dmePort, String dmeUsername, String dmePassword) {
    String result = null;
    try {
      Map<String, String> bodyParamMap = new HashMap<String, String>();
      bodyParamMap.put("vcenterIP",vcenterIp);
      bodyParamMap.put("vcenterPort",vcenterPort);
      bodyParamMap.put("vcenterUsername", vcenterUsername);
      bodyParamMap.put("vcenterPassword", vcenterPassword);
      bodyParamMap.put("action", action);
      bodyParamMap.put("removeData", removeData ? "true" : "false");
      bodyParamMap.put("dmeIp",dmeIp);
      bodyParamMap.put("dmePort",dmePort);
      bodyParamMap.put("dmeUsername",dmeUsername);
      bodyParamMap.put("dmePassword",dmePassword);
      String body = HttpRequestUtil.concatParamAndEncode(bodyParamMap);

      result = HttpRequestUtil
          .requestWithBody(String.format(RESTURL_VCENTERACTION, vcenterIp + ":" + vcenterPort),
              HttpMethod.POST, HEADERS, body, String.class).getBody();
      LOGGER.debug("unsubscribe: " + result);
    } catch (Exception e) {
      LOGGER.error( e.getMessage(),e);
    }
    return result;
  }

  public static String vcenteraction(String vcenterIp, String vcenterUsername,
                                     String vcenterPassword, String vcenterPort, String action, boolean removeData) {

    return vcenteraction(vcenterIp,vcenterUsername,vcenterPassword,vcenterPort,action,removeData,"","","","");
  }

  public static Map onloadChecker(HttpServletRequest request) {
    Map<String, Object> returnMap = new HashMap<>();

    File keyFile = new File(Constants.KEYSTORE_FILE);
    if (!keyFile.exists()) {
      return Collections.singletonMap("error", "E006");
    }

    List<String> packageNameList = new ArrayList<>();
    List<String> versionList = new ArrayList<>();

    // check file
    File rootFile = new File("./");
    File[] fileList = rootFile.listFiles();
    if (fileList != null) {
      for (File file : fileList) {
        if (file.getName().lastIndexOf(".zip") >= 0) {
          // check version
          String version = getPackageVersion(file.getName());
          if ((version == null) || ("".equals(version.trim()))) {
            continue;
          }
          packageNameList.add(file.getName());
          versionList.add(version);
        }
      }
    }

    if (packageNameList.isEmpty()) {
      return Collections.singletonMap("error", "E007");
    }

    returnMap.put("packageNameList", packageNameList);
    returnMap.put("versionList", versionList);
    returnMap.put("key", keyFile.getAbsolutePath());
    returnMap.put("path",
        "https://" + request.getServerName() + ":" + request.getServerPort() + "/package/");
    return returnMap;
  }

  private static String getPackageVersion() {
    String version = null;
    try {
      version = ZipUtils.getVersionFromPackage(Constants.UPDATE_FILE);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return version;
  }

  private static String getPackageVersion(String file) {
    String version = null;
    try {
      version = ZipUtils.getVersionFromPackage(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return version;
  }
}
