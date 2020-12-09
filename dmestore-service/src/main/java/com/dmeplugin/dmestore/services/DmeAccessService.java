package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * DmeAccessService
 *
 * @author yy
 * @since 2020-09-02
 **/
public interface DmeAccessService {
    /**
     * Access DME
     *
     * @author yy
     * @param params key required: obj_ids, indicator_ids, range
     * @throws DMEException 异常
     */
    void accessDme(Map<String, Object> params) throws DMEException;

    /**
     * refreshDme
     *
     * @author wangxy
     * @return Map
     * @throws DMEException 异常
     */
    Map<String, Object> refreshDme() throws DMEException;

    /**
     * Public method access
     *
     * @param url url
     * @param method Http Method
     * @param requestBody request Body
     * @return ResponseBodyBean
     * @throws DMEException when error
     */
    ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws DMEException;

    /**
     * Public method access
     *
     * @param url url
     * @param method Http Method
     * @param jsonBody request Body
     * @return ResponseBodyBean
     * @throws DMEException when error
     */
    ResponseEntity<String> accessByJson(String url, HttpMethod method, String jsonBody) throws DMEException;

    /**
     * Access workload info
     *
     * @param storageId storage id
     * @return ResponseBodyBean
     * @throws DMEException when error
     */
    List<Map<String, Object>> getWorkLoads(String storageId) throws DMEException;

    /**
     * Query Dme Hosts
     *
     * @param hostIp host ip
     * @return ResponseBodyBean
     * @throws DMEException when error
     */
    List<Map<String, Object>> getDmeHosts(String hostIp) throws DMEException;

    /**
     * Query Dme Host's initiators
     *
     * @param hostId host id
     * @return ResponseBodyBean
     * @throws DMEException when error
     */
    List<Map<String, Object>> getDmeHostInitiators(String hostId) throws DMEException;

    /**
     * Query Dme hostgroup
     *
     * @param hostGroupName hostGroup Name
     * @return ResponseBodyBean
     * @throws DMEException when error
     */
    List<Map<String, Object>> getDmeHostGroups(String hostGroupName) throws DMEException;

    /**
     * create host
     *
     * @param params create host's param
     * @return ResponseBodyBean
     * @throws DMEException when error
     */
    Map<String, Object> createHost(Map<String, Object> params) throws DMEException;

    /**
     * create hostgroup
     *
     * @param params create hostgroup's param
     * @return ResponseBodyBean
     * @throws DMEException when error
     */
    Map<String, Object> createHostGroup(Map<String, Object> params) throws DMEException;

    /**
     * get host's detailed
     *
     * @param hostId host id
     * @return ResponseBodyBean
     * @throws DMEException when error
     */
    Map<String, Object> getDmeHost(String hostId) throws DMEException;

    /**
     * delete Volume
     *
     * @param ids host ids
     * @throws DMEException when error
     */
    void deleteVolumes(List<String> ids) throws DMEException;

    /**
     * unMap Host
     *
     * @param hostId host id
     * @param ids datastore ids
     * @throws DMEException when error
     */
    void unMapHost(String hostId, List<String> ids) throws DMEException;

    /**
     * scan Datastore
     *
     * @param storageType storage type:VMFS,NFS,ALL
     * @throws DMEException when error
     */
    void scanDatastore(String storageType) throws DMEException;

    /**
     * Configure task time
     *
     * @param taskId task Id
     * @param taskCron task cron
     * @throws DMEException when error
     */
    void configureTaskTime(Integer taskId,String taskCron) throws DMEException;

    /**
     * get hostGroup's detail
     *
     * @param hsotGroupId hostGroups id
     * @return Map
     * @throws DMEException when error
     */
    Map<String,Object> getDmeHostGroup(String hsotGroupId) throws DMEException;

    /**
     * get hostGroup's host
     *
     * @param hostGroupId hostGroups id
     * @return List
     * @throws DMEException when error
     */
    List<Map<String,Object>> getDmeHostInHostGroup(String hostGroupId) throws DMEException;

    /**
     * Configure task time
     *
     * @param hostId host Id
     * @param volumeIds task cron
     * @throws DMEException when error
     */
    void hostMapping(String hostId, List<String> volumeIds) throws DMEException;
}
