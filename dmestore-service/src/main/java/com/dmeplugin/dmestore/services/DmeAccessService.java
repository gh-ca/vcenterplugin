package com.dmeplugin.dmestore.services;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
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

    /**
     * Rest public method
     */
    ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws Exception;

    /**
     * Get WorkLoads
     */
    List<Map<String, Object>> getWorkLoads(String storage_id) throws Exception;

    /**
     * Query host
     */
    List<Map<String, Object>> getDmeHosts(String hostIp) throws Exception;

}
