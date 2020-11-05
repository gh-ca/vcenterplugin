package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that support basic dme API
 */
public class DMEOpenApiService {



  protected static final int FAIL_CODE = -99999;

  private static final int CODE_DME_CONNECT_EXCEPTION = -80010;

  protected static final int RESULT_READ_CERT_ERROR = -90008;

  protected static final int RESULT_SUCCESS_CODE = 0;

  protected static final double RESULT_SUCCESS_CODE_DOUBLE = 0.0;

  protected static final int RESULT_ERROR_CODE = 10000;

  protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  protected static final String CODE_SUCCESS = String.valueOf(RESULT_SUCCESS_CODE);

  protected static final String CODE_SUCCESS_DOUBLE = String.valueOf(RESULT_SUCCESS_CODE_DOUBLE);

  protected Map<String, Object> getNoDmeMap() {
    Map<String, Object> map = new HashMap();
    map.put("code", FAIL_CODE);
    map.put("description", "No dme data in DB");
    return map;
  }

  protected Map<String, Object> getDmeExceptionMap(DMEException e) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("code", Integer.valueOf(e.getCode()));
    map.put("description", e.getMessage());
    return map;
  }

  protected Map<String, Object> getExceptionMap() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("code", CODE_DME_CONNECT_EXCEPTION);
    map.put("description", "Failed to connect to dme");
    return map;
  }

  public static boolean isSuccessResponse(Object code) {
    if (code != null) {
      if (code instanceof Integer) {
        return (Integer) code == Integer.parseInt(CODE_SUCCESS);
      } else if (code instanceof Double) {
        return (Double) code == Double.parseDouble(CODE_SUCCESS_DOUBLE);
      } else if (code instanceof String) {
        return CODE_SUCCESS.equals(code.toString()) || CODE_SUCCESS_DOUBLE.equals(code.toString());
      }
    }
    return false;
  }

}
