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
     * params include hostIp: Access to the IP address of the DME service
     * hostPort: Port to access DME service
     * userName: User name to access the DME service
     * password: Password to access the DME service
     *
     * @param params: include hostIp,hostPort,userName,password
     * @return: ResponseBodyBean
     */
    Map<String, Object> accessDme(Map<String, Object> params);

    /**
     * Refresh connection status
     *
     * @return ResponseBodyBean
     */
    Map<String, Object> refreshDme();

    /**
     * Public method access
     *
     * @param url url
     * @param method Http Method
     * @param requestBody request Body
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws Exception;

    /**
     * Access workload info
     *
     * @param storageId storage id
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    List<Map<String, Object>> getWorkLoads(String storageId) throws Exception;

    /**
     * Query Dme Hosts
     *
     * @param hostIp host ip
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    List<Map<String, Object>> getDmeHosts(String hostIp) throws Exception;

    /**
     * Query Dme hostgroup
     *
     * @param hostGroupName hostGroup Name
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    List<Map<String, Object>> getDmeHostGroups(String hostGroupName) throws Exception;

    /**
     * create host
     *
     * @param params create host's param
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    Map<String, Object> createHost(Map<String, Object> params) throws Exception;

    /**
     * create hostgroup
     *
     * @param params create hostgroup's param
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    Map<String, Object> createHostGroup(Map<String, Object> params) throws Exception;

    /**
     * get host's detailed
     *
     * @param hostId host id
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    Map<String, Object> getDmeHost(String hostId) throws Exception;

    /**
     * get hostGroup's detail
     * @param hsotGroupId hostGroups id
     * @return
     * @throws Exception
     */
    Map<String,Object> getDmeHostGroup(String hsotGroupId) throws Exception;

}
