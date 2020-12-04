package com.dmeplugin.dmestore.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that support basic dme API
 */
public class DMEOpenApiService {
    protected static final int FAIL_CODE = -99999;

    protected static final int RESULT_SUCCESS_CODE = 0;

    protected static final double RESULT_SUCCESS_CODE_DOUBLE = 0.0;

    protected static final int RESULT_ERROR_CODE = 10000;

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected static final String CODE_SUCCESS = String.valueOf(RESULT_SUCCESS_CODE);

    protected static final String CODE_SUCCESS_DOUBLE = String.valueOf(RESULT_SUCCESS_CODE_DOUBLE);

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
