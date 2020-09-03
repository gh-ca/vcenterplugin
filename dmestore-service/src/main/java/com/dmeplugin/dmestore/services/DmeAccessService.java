package com.dmeplugin.dmestore.services;

import com.google.gson.JsonObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by hyuan on 2017/5/10.
 */

public interface DmeAccessService {
    /**
     * Access DME
     */
    Map<String, Object> accessDme(Map<String, Object> params);

    /**
     * Refresh connection status
     */
    Map<String, Object> refreshDme();


    ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws Exception;

}
