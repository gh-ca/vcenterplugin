package com.dmeplugin.dmestore.services;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: DmeAccessService
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
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
    List<Map<String, Object>> getWorkLoads(String storageId) throws Exception;

    /**
     * Query host
     */
    List<Map<String, Object>> getDmeHosts(String hostIp) throws Exception;

    /**
     * Query hostgroup
     */
    List<Map<String, Object>> getDmeHostGroups(String hostGroupName) throws Exception;

    /**
     * create host
     */
    Map<String, Object> createHost(Map<String, Object> params) throws Exception;

    /**
     * create hostgroup
     */
    Map<String, Object> createHostGroup(Map<String, Object> params) throws Exception;

    /**
     * get host
     */
    Map<String, Object> getDmeHost(String hostId) throws Exception;

}
