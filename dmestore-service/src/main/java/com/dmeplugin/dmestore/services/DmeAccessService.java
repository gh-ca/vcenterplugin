package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
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
    void accessDme(Map<String, Object> params) throws DMEException;

    /**
     * Refresh connection status
     *
     * @return ResponseBodyBean
     */
    Map<String, Object> refreshDme() throws DMEException;

    /**
     * Public method access
     *
     * @param url url
     * @param method Http Method
     * @param requestBody request Body
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws DMEException;

    /**
     * Public method access
     *
     * @param url url
     * @param method Http Method
     * @param jsonBody request Body
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    ResponseEntity<String> accessByJson(String url, HttpMethod method, String jsonBody) throws DMEException;

    /**
     * Access workload info
     *
     * @param storageId storage id
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    List<Map<String, Object>> getWorkLoads(String storageId) throws DMEException;

    /**
     * Query Dme Hosts
     *
     * @param hostIp host ip
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    List<Map<String, Object>> getDmeHosts(String hostIp) throws DMEException;

    /**
     * Query Dme Host's initiators
     *
     * @param hostId host id
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    List<Map<String, Object>> getDmeHostInitiators(String hostId) throws DMEException;

    /**
     * Query Dme hostgroup
     *
     * @param hostGroupName hostGroup Name
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    List<Map<String, Object>> getDmeHostGroups(String hostGroupName) throws DMEException;

    /**
     * create host
     *
     * @param params create host's param
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    Map<String, Object> createHost(Map<String, Object> params) throws DMEException;

    /**
     * create hostgroup
     *
     * @param params create hostgroup's param
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    Map<String, Object> createHostGroup(Map<String, Object> params) throws DMEException;

    /**
     * get host's detailed
     *
     * @param hostId host id
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    Map<String, Object> getDmeHost(String hostId) throws DMEException;

    /**
     * delete Volume
     *
     * @param ids host ids
     * @throws Exception when error
     */
    void deleteVolumes(List<String> ids) throws DMEException;

    /**
     * unMap Host
     *
     * @param hostId host id
     * @param ids datastore ids
     * @throws Exception when error
     */
    void unMapHost(String hostId, List<String> ids) throws DMEException;

    /**
     * scan Datastore
     *
     * @param storageType storage type:VMFS,NFS,ALL
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    void scanDatastore(String storageType) throws DMEException;

    /**
     * Configure task time
     *
     * @param taskId task Id
     * @param taskCron task cron
     * @return ResponseBodyBean
     * @throws Exception when error
     */
    void configureTaskTime(Integer taskId,String taskCron) throws DMEException;

    /**
     * get hostGroup's detail
     * @param hsotGroupId hostGroups id
     * @return
     * @throws Exception
     */
    Map<String,Object> getDmeHostGroup(String hsotGroupId) throws DMEException;

    /**
     * get hostGroup's host
     * @param hostGroupId hostGroups id
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> getDmeHostInHostGroup(String hostGroupId) throws DMEException;


    void hostMapping(String hostId, List<String> volumeIds) throws DMEException;
}
