package com.dmeplugin.dmestore.services;


import com.dmeplugin.dmestore.dao.DmeVmwareRalationDao;
import com.dmeplugin.dmestore.entity.DmeVmwareRelation;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.model.VmfsDataInfo;
import com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.*;

public class VmfsAccessServiceImpl implements VmfsAccessService {

    private static final Logger LOG = LoggerFactory.getLogger(VmfsAccessServiceImpl.class);

    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private DmeVmwareRalationDao dmeVmwareRalationDao;
    @Autowired
    private DmeAccessService dmeAccessService;
    @Autowired
    private DataStoreStatisticHistoryService dataStoreStatisticHistoryService;
    @Autowired
    private DmeStorageService dmeStorageService;


    private final String LIST_VOLUME_URL = "/rest/blockservice/v1/volumes";
    private final String HOST_UNMAPAPING = "/rest/blockservice/v1/volumes/host-unmapping";
    private final String HOSTGROUP_UNMAPPING = "/rest/blockservice/v1/volumes/hostgroup-unmapping";
    private final String VOLUME_DELETE = "/rest/blockservice/v1/volumes/delete";


    @Override
    public List<VmfsDataInfo> listVmfs() throws Exception {
        List<VmfsDataInfo> relists = null;
        try {
            //从关系表中取得DME卷与vcenter存储的对应关系
            List<DmeVmwareRelation> dvrlist = dmeVmwareRalationDao.getDmeVmwareRelation(ToolUtils.STORE_TYPE_VMFS);
            LOG.info("dvrlist==" + gson.toJson(dvrlist));
            if(dvrlist!=null && dvrlist.size()>0) {
                //整理数据
                Map<String, DmeVmwareRelation> dvrMap = getDvrMap(dvrlist);
                //取得所有的存储设备
                Map<String, Object> storagemap = dmeStorageService.getStorages(null);
                //整理数据
                Map<String,String> stoNameMap = getStorNameMap(storagemap);
                LOG.info("stoNameMap==="+gson.toJson(stoNameMap));
                //取得vcenter中的所有vmfs存储。
                String listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_VMFS);
                LOG.info("Vmfs listStr==" + listStr);
                if (!StringUtils.isEmpty(listStr)) {
                    JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
                    if (jsonArray != null && jsonArray.size() > 0) {
                        relists = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jo = jsonArray.get(i).getAsJsonObject();
                            //LOG.info("jo==" + jo.toString());
                            String vmwareStoreName = ToolUtils.jsonToStr(jo.get("name"));
                            if (!StringUtils.isEmpty(vmwareStoreName)) {
                                //对比数据库关系表中的数据，只显示关系表中的数据
                                if (dvrMap != null && dvrMap.get(vmwareStoreName) != null) {
                                    VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                                    double capacity = ToolUtils.getDouble(jo.get("capacity")) / ToolUtils.Gi;
                                    double freeSpace = ToolUtils.getDouble(jo.get("freeSpace")) / ToolUtils.Gi;
                                    double uncommitted = ToolUtils.getDouble(jo.get("uncommitted")) / ToolUtils.Gi;

                                    vmfsDataInfo.setName(vmwareStoreName);

                                    vmfsDataInfo.setCapacity(capacity);
                                    vmfsDataInfo.setFreeSpace(freeSpace);
                                    vmfsDataInfo.setReserveCapacity(capacity + uncommitted - freeSpace);

                                    String wwn = jo.get("url").getAsString();
                                    LOG.info("wwn==" + wwn);
                                    //然后通过vmfs中的url值去DME系统中查询对应wwn的卷信息。
                                    ///rest/blockservice/v1/volumes?volume_wwn=wwn
                                    //这里由于DME系统中的卷太多。是分页查询，所以需要vmfs一个个的去查DME系统中的卷。
                                    //而每次查询DME中的卷都需要调用两次，分别是查卷列表接口，查卷详细接口。
                                    String volumeUrlByWwn = LIST_VOLUME_URL + "?volume_wwn=" + wwn;
                                    try {
                                        ResponseEntity responseEntity = dmeAccessService.access(volumeUrlByWwn, HttpMethod.GET, null);
                                        LOG.info("listVmfs responseEntity==" + responseEntity.toString());
                                        if (responseEntity.getStatusCodeValue() == 200) {
                                            JsonObject jsonObject = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                                            //LOG.info("listVmfs jsonObject==" + jsonObject.toString());
                                            JsonObject vjson = jsonObject.getAsJsonArray("volumes").get(0).getAsJsonObject();

                                            vmfsDataInfo.setVolumeId(ToolUtils.jsonToStr(vjson.get("id")));
                                            vmfsDataInfo.setStatus(ToolUtils.jsonToStr(vjson.get("status")));
                                            vmfsDataInfo.setServiceLevelName(ToolUtils.jsonToStr(vjson.get("service_level_name")));
                                            vmfsDataInfo.setVmfsProtected(ToolUtils.jsonToBoo(vjson.get("protected")));

                                            String storage_id = ToolUtils.jsonToStr(vjson.get("storage_id"));
                                            vmfsDataInfo.setDeviceId(storage_id);
                                            vmfsDataInfo.setDevice(stoNameMap==null?"":stoNameMap.get(storage_id));

                                            String volid = ToolUtils.jsonToStr(vjson.get("id"));
                                            //通过卷ID再调卷详细接口
                                            String detailedVolumeUrl = LIST_VOLUME_URL + "/" + volid;
                                            try {
                                                responseEntity = dmeAccessService.access(detailedVolumeUrl, HttpMethod.GET, null);
                                                LOG.info("volid responseEntity==" + responseEntity.toString());
                                                if (responseEntity.getStatusCodeValue() == 200) {
                                                    JsonObject voljson = new JsonParser().parse(responseEntity.getBody().toString()).getAsJsonObject();
                                                    //LOG.info("volid voljson==" + voljson.toString());
                                                    JsonObject vjson2 = voljson.getAsJsonObject("volume");
                                                    if (vjson2 != null && vjson2.get("tuning") != null) {
                                                        JsonObject tuning = vjson2.getAsJsonObject("tuning");
                                                        if (tuning != null && tuning.get("smartqos") != null) {
                                                            JsonObject smartqos = tuning.getAsJsonObject("smartqos");
                                                            if (smartqos != null) {
                                                                vmfsDataInfo.setMaxIops(ToolUtils.jsonToInt(smartqos.get("maxiops"), null));
                                                                vmfsDataInfo.setMinIops(ToolUtils.jsonToInt(smartqos.get("miniops"), null));
                                                                vmfsDataInfo.setMaxBandwidth(ToolUtils.jsonToInt(smartqos.get("maxbandwidth"), null));
                                                                ;
                                                                vmfsDataInfo.setMinBandwidth(ToolUtils.jsonToInt(smartqos.get("minbandwidth"), null));
                                                                vmfsDataInfo.setLatency(ToolUtils.jsonToInt(smartqos.get("latency"), null));
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (Exception ex) {
                                                LOG.error("DME link error url:" + detailedVolumeUrl + ",error:" + ex.getMessage());
                                            }

                                            relists.add(vmfsDataInfo);
                                        }
                                    } catch (Exception e) {
                                        LOG.error("DME link error url:" + volumeUrlByWwn + ",error:" + e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list vmfs error:", e);
            throw e;
        }
        LOG.info("relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public List<VmfsDataInfo> listVmfsPerformance(List<String> volumeIds) throws Exception {
        List<VmfsDataInfo> relists = null;
        try {
            if (volumeIds != null && volumeIds.size() > 0) {
                Map<String, Object> params = new HashMap<>();
                params.put("obj_ids", volumeIds);
                Map<String, Object> remap = dataStoreStatisticHistoryService.queryVmfsStatisticCurrent(params);
                LOG.info("remap===" + gson.toJson(remap));
                if (remap != null && remap.get("data") != null) {
                    JsonObject dataJson = (JsonObject) remap.get("data");
                    if (dataJson != null) {
                        relists = new ArrayList<>();
                        for (String volumeId : volumeIds) {
                            JsonObject statisticObject = dataJson.getAsJsonObject(volumeId);
                            if (statisticObject != null) {
                                VmfsDataInfo vmfsDataInfo = new VmfsDataInfo();
                                vmfsDataInfo.setVolumeId(volumeId);
                                vmfsDataInfo.setIops(ToolUtils.jsonToInt(statisticObject.get(DataStoreStatisticHistoryServiceImpl.COUNTER_NAME_IOPS), null));
                                vmfsDataInfo.setBandwidth(ToolUtils.jsonToDou(statisticObject.get(DataStoreStatisticHistoryServiceImpl.COUNTER_NAME_BANDWIDTH), null));
                                vmfsDataInfo.setReadResponseTime(ToolUtils.jsonToInt(statisticObject.get(DataStoreStatisticHistoryServiceImpl.COUNTER_NAME_READPESPONSETIME), null));
                                vmfsDataInfo.setWriteResponseTime(ToolUtils.jsonToInt(statisticObject.get(DataStoreStatisticHistoryServiceImpl.COUNTER_NAME_WRITERESPONSETIME), null));
                                relists.add(vmfsDataInfo);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list vmfs performance error:", e);
            throw e;
        }
        LOG.info("listVmfsPerformance relists===" + (relists == null ? "null" : (relists.size() + "==" + gson.toJson(relists))));
        return relists;
    }

    @Override
    public void createVmfs(Map<String, Object> params) throws Exception {
        //param str host: 主机  param str cluster: 集群
        String objhostid = "";
        //判断主机或主机组在DME中是否存在
        //如果主机或主机不存在就创建并得到主机或主机组ID
        objhostid = checkOrcreateToHostorHostGroup(params);
        LOG.info("objhostid===="+objhostid);
        if(!StringUtils.isEmpty(objhostid)) {
            //创建DME卷
            //判断服务等级是否存在
//            if()
            //查询看创建任务是否完成。
            //创建vmware中的vmfs存储。
        }
    }

    private String checkOrcreateToHost(String hostIp){
        String objId = "";
        try{
            //param str host: 主机  param str cluster: 集群
            if(!StringUtils.isEmpty(hostIp)) {
                List<Map<String, Object>> hostlist = dmeAccessService.getDmeHosts(hostIp);
                if (hostlist != null && hostlist.size() > 0) {
                    for (Map<String, Object> hostmap : hostlist) {
                        if (hostmap != null && hostmap.get("ip") != null) {
                            if (hostIp.equals(hostmap.get("ip").toString())) {
                                objId = hostmap.get("id").toString();
                                break;
                            }
                        }
                    }
                }
                LOG.info("objhostid==" + objId);
                //判断主机或主机组在DME中是否存在
                if (StringUtils.isEmpty(objId)) {
                    Map<String,Object> params = new HashMap<>();
                    params.put("host",hostIp);
                    Map<String, Object> hostmap = dmeAccessService.createHost(params);
                    if (hostmap != null && hostmap.get("id") != null) {
                        objId = hostmap.get("id").toString();
                    }
                }
                LOG.info("objhostid==" + objId);
            }
            //如果主机或主机不存在就创建并得到主机或主机组ID
        }catch (Exception e){
            LOG.error("checkOrcreateToHost error:",e);
        }
        return objId;
    }

    private String checkOrcreateToHostGroup(String clusterName){
        String objId = "";
        try{
            //param str host: 主机  param str cluster: 集群
            if(!StringUtils.isEmpty(clusterName)) {
                //检查集群对应的主机组在DME中是否存在
                List<Map<String, Object>> hostgrouplist = dmeAccessService.getDmeHostGroups(clusterName);
                if (hostgrouplist != null && hostgrouplist.size() > 0) {
                    for (Map<String, Object> hostgroupmap : hostgrouplist) {
                        if (hostgroupmap != null && hostgroupmap.get("name") != null) {
                            if (clusterName.equals(hostgroupmap.get("name").toString())) {
                                objId = hostgroupmap.get("id").toString();
                                break;
                            }
                        }
                    }
                }
                LOG.info("objhostid==" + objId);
                //如果主机组不存在就需要创建,创建前要检查集群下的所有主机是否在DME中存在
                if(StringUtils.isEmpty(objId)) {
                    //取得集群下的所有主机
                    String vmwarehosts = VCSDKUtils.getHostsOnCluster(clusterName);
                    LOG.info("vmwarehosts==" + vmwarehosts);
                    if (!StringUtils.isEmpty(vmwarehosts)) {
                        List<Map<String, String>> vmwarehostlists = gson.fromJson(vmwarehosts, new TypeToken<List<Map<String, String>>>() {}.getType());
                        if(vmwarehostlists!=null && vmwarehostlists.size()>0){
                            //分别检查每一个主机是否存在，如果不存在就创建
                            List<String> hostlists = new ArrayList<>();
                            for(Map<String, String> hostmap:vmwarehostlists){
                                LOG.info("checkOrcreateToHost===="+hostmap.get("hostName"));
                                String tmpHostId = checkOrcreateToHost(hostmap.get("hostName").toString());
                                if(!StringUtils.isEmpty(tmpHostId)){
                                    hostlists.add(tmpHostId);
                                }
                            }
                            LOG.info("hostlists===="+hostlists);
                            //在DME中创建主机组
                            if(hostlists.size()>0){
                                Map<String, Object> params = new HashMap<>();
                                params.put("cluster",clusterName);
                                params.put("hostids",hostlists);
                                Map<String, Object> hostmap = dmeAccessService.createHostGroup(params);
                                if (hostmap != null && hostmap.get("id") != null) {
                                    objId = hostmap.get("id").toString();
                                }
                            }
                        }
                    }
                }
            }
            //如果主机或主机不存在就创建并得到主机或主机组ID
        }catch (Exception e){
            LOG.error("checkOrcreateToHostGroup error:",e);
        }
        return objId;
    }

    private String checkOrcreateToHostorHostGroup(Map<String, Object> params){
        String objId = "";
        try{
            //param str host: 主机  param str cluster: 集群
            if(params!=null && params.get("host")!=null) {
                objId = checkOrcreateToHost(params.get("host").toString());
            }else if(params!=null && params.get("cluster")!=null) {
                objId = checkOrcreateToHostGroup(params.get("cluster").toString());
            }
        }catch (Exception e){
            LOG.error("checkOrcreateToHostorHostGroup error:",e);
        }
        return objId;
    }

    @Override
    public void mountVmfs(Map<String, Object> params) throws Exception {

    }

    /**
     * @Author wangxiangyong
     * @Description /vmfs datastore 卷详情查询
     * @Date 14:46 2020/9/3
     * @Param [volume_id]
     * @Return com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail
     **/
    @Override
    public VmfsDatastoreVolumeDetail volumeDetail(String volume_id) throws Exception {
        //调用DME接口获取卷详情
        String url = LIST_VOLUME_URL + "/" + volume_id;
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.error("查询卷信息失败！错误信息:{}", responseEntity.getBody());
                return null;
            }
        } catch (Exception ex) {
            LOG.error("查询卷信息异常", ex);
            return null;
        }

        String responseBody = responseEntity.getBody();
        JsonObject volume = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject("volume");

        VmfsDatastoreVolumeDetail volumeDetail = new VmfsDatastoreVolumeDetail();
        //basic info
        volumeDetail.setWwn(volume.get("volume_wwn").getAsString());
        volumeDetail.setName(volume.get("name").getAsString());
        volumeDetail.setServiceLevel(volume.get("service_level_name").getAsString());
        //TODO
        volumeDetail.setStorage(volume.get("storage_id").getAsString());
        volumeDetail.setStoragePool(volume.get("pool_raw_id").getAsString());

        JsonObject tuning = volume.getAsJsonObject("tuning");
        //SmartTier
        volumeDetail.setSmartTier(tuning.get("smarttier").getAsString());
        //Tunning
        volumeDetail.setDudeplication(tuning.get("dedupe_enabled").getAsBoolean());
        volumeDetail.setProvisionType(tuning.get("alloctype").getAsString());
        volumeDetail.setCompression(tuning.get("compression_enabled").getAsBoolean());
        //TODO
        volumeDetail.setApplicationType("--");

        JsonObject smartqos = tuning.getAsJsonObject("smartqos");
        //Qos Policy
        if (null != smartqos) {
            volumeDetail.setControlPolicy(smartqos.get("control_policy").getAsString());
            //TODO
            volumeDetail.setTrafficControl("--");
        }
        return volumeDetail;
    }

    /**
     * @Author Administrator
     * @Description 扫描vmfs datastore并和卷建立关系
     * @Date 16:49 2020/9/7
     * @Param []
     * @Return boolean
     **/
    public boolean vmfsDatastoreVolumeRelation() throws Exception {
        String listStr = VCSDKUtils.getAllVmfsDataStores(ToolUtils.STORE_TYPE_VMFS);
        LOG.info("===list vmfs datastore success====\n{}", listStr);
        if (StringUtils.isEmpty(listStr)) {
            return false;
        }
        //TODO
        JsonArray jsonArray = new JsonParser().parse(listStr).getAsJsonArray();
        List<DmeVmwareRelation> relationList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject vmfsDatastore = jsonArray.get(i).getAsJsonObject();
            String store_type = ToolUtils.STORE_TYPE_VMFS;
            //TODO 暂时认为是从url中获取到wwn信息
            String vmfsDatastoreUrl = vmfsDatastore.get("url").getAsString();
            String vmfsDatastoreId = vmfsDatastore.get("id").getAsString();
            String vmfsDatastoreName = vmfsDatastore.get("name").getAsString();
            //拆分wwn
            String wwn = vmfsDatastoreUrl.split("volumes/")[1];
            //根据wwn从DME中查询卷信息
            String volumeUrlByWwn = LIST_VOLUME_URL + "?volume_wwn=" + wwn;
            ResponseEntity<String> responseEntity = dmeAccessService.access(volumeUrlByWwn, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.info(" Query DME volume failed! errorMsg:{}", responseEntity.toString());
                continue;
            }
            JsonObject jsonObject = gson.fromJson(responseEntity.getBody(), JsonObject.class);
            JsonObject volume = jsonObject.getAsJsonArray("volumes").get(0).getAsJsonObject();
            String volumeId = volume.get("id").getAsString();
            String volumeName = volume.get("name").getAsString();
            String volumeWwn = volume.get("volume_wwn").getAsString();

            DmeVmwareRelation relation = new DmeVmwareRelation();
            relation.setStoreId(vmfsDatastoreId);
            relation.setStoreName(vmfsDatastoreName);
            relation.setVolumeId(volumeId);
            relation.setVolumeName(volumeName);
            relation.setVolumeWwn(volumeWwn);
            relation.setStoreType(store_type);
            relation.setState(1);

            relationList.add(relation);
        }

        if (relationList.size() > 0) {
            //数据库处理
            return dmeVmwareRelationDBProcess(relationList, ToolUtils.STORE_TYPE_VMFS);
        }
        return true;
    }

    private boolean dmeVmwareRelationDBProcess(List<DmeVmwareRelation> relationList, String storeType) throws Exception {
        //本地全量查询
        List<String> localWwns = dmeVmwareRalationDao.getAllWwnByType(storeType);
        //新增
        List<DmeVmwareRelation> newList = new ArrayList<>();
        List<DmeVmwareRelation> upList = new ArrayList<>();
        for (DmeVmwareRelation o : relationList) {
            String wwn = o.getVolumeWwn();
            if (localWwns.contains(wwn)) {
                upList.add(o);
                localWwns.remove(wwn);
            } else {
                newList.add(o);
            }
        }

        //更新
        if (!upList.isEmpty()) {
            dmeVmwareRalationDao.update(upList);
        }

        //新增
        if (!newList.isEmpty()) {
            dmeVmwareRalationDao.save(newList);
        }
        //删除
        if (!localWwns.isEmpty()) {
            dmeVmwareRalationDao.deleteByWwn(localWwns);
        }
        return true;
    }


    @Override
    public void unmountVmfs(Map<String, Object> params) throws Exception {
        //unmount前的效验 wether attached is ture
        String volume_id = params.get("{volume_id").toString();
        ResponseEntity responseVmfs = queryVmfsById(volume_id);
        if (null != responseVmfs) {
            Object body = responseVmfs.getBody();
            JsonObject bodyJson = new JsonParser().parse(body.toString()).getAsJsonObject();
            JsonObject volumJson = bodyJson.get("volume").getAsJsonObject();
            boolean attached = volumJson.get("attached").getAsBoolean();
            if (attached) {
                JsonArray attachments = volumJson.getAsJsonArray("attachments");
                for (JsonElement attachment : attachments) {
                    //String volume_id = attachment.getAsJsonObject().get("volume_id").getAsString();//attachment中的volume_id和param中的volume_id应该是一致的
                    String host_id = attachment.getAsJsonObject().get("host_id").getAsString();
                    String hostgroup_id = attachment.getAsJsonObject().get("attached_host_group").getAsString();
                    List<String> volume_ids = Arrays.asList(volume_id);

                    params.put("volume_ids", volume_ids);
                    if (!StringUtils.isEmpty(host_id)) {
                        params.put("host_id", host_id);
                    }
                    if (!StringUtils.isEmpty(hostgroup_id)) {
                        params.put("hostgroup_id", hostgroup_id);
                    }
                    break;// break? volume 与host 及hostgroup的关系应该是1:1
                }
            }
            //按需分别卸载host 和 hostgroup
            boolean unmappingHostFlag = true;
            boolean unmappingHostgroupFlag = true;
            if (null != params.get("hostgroup_id")) {
                ResponseEntity responseHostGroupUnmaaping = hostGroupUnmapping(params);
                if (202 != responseHostGroupUnmaaping.getStatusCodeValue()) {
                    unmappingHostgroupFlag = false;
                }
            }
            if (null != params.get("host_id")) {
                ResponseEntity responseHostUnmapping = hostUnmapping(params);
                if (202 != responseHostUnmapping.getStatusCodeValue()) {
                    unmappingHostFlag = false;
                }
            }
            //判断是否卸载成功 ，卸载失败 抛出错误提示
            if (!(unmappingHostFlag && unmappingHostgroupFlag)) {
                throw new Exception("unmount volume precondition unmount host and hostGroup error!");
            }
        }

        //vcenter侧卸载
    }

    @Override
    public void deleteVmfs(Map<String, Object> params) throws Exception {
        //先调卸载的接口
        try {
            unmountVmfs(params);
        } catch (Exception e) {
            LOG.error("delete volume precondition unmapping host and hostGroup error!");
            throw new Exception("delete volume precondition unmapping host and hostGroup error!");
        }
        //删除vmfs
        Object volume_ids = params.get("volume_ids");
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("volume_ids", volume_ids);
        ResponseEntity responseEntity = dmeAccessService.access(VOLUME_DELETE, HttpMethod.POST, requestbody.toString());
        if (202 != responseEntity.getStatusCodeValue()) {
            throw new Exception("delete volume error!");
        }

        //vcenter侧删除
    }

    //查询指定vmfs
    private ResponseEntity queryVmfsById(String volume_id) throws Exception {
        String url = LIST_VOLUME_URL + "/" + volume_id;
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.error("查询指定卷信息失败!错误信息:{}", responseEntity.getBody());
                return null;
            }
        } catch (Exception ex) {
            LOG.error("查询卷信息异常", ex);
            return null;
        }
        return responseEntity;
    }


    private ResponseEntity hostUnmapping(Map<String, Object> params) throws Exception {
        String hostId = params.get("hostId").toString();
        Object volumeIds = params.get("volumeIds");

        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("host_id", hostId);
        requestbody.put("volume_ids", volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(HOST_UNMAPAPING, HttpMethod.POST, requestbody.toString());
        return responseEntity;
    }

    private ResponseEntity hostGroupUnmapping(Map<String, Object> params) throws Exception {
        String hostGroupId = params.get("hostGroupId").toString();
        Object volumeIds = params.get("volumeIds");
        Map<String, Object> requestbody = new HashMap<>();
        requestbody.put("host_id", hostGroupId);
        requestbody.put("volume_ids", volumeIds);
        ResponseEntity responseEntity = dmeAccessService.access(HOSTGROUP_UNMAPPING, HttpMethod.POST, requestbody.toString());
        return responseEntity;

    }

    //整理关系表数据
    private Map<String, DmeVmwareRelation> getDvrMap(List<DmeVmwareRelation> dvrlist) {
        Map<String, DmeVmwareRelation> remap = null;
        try{
            if (dvrlist != null && dvrlist.size() > 0) {
                remap = new HashMap<>();
                for (DmeVmwareRelation dvr : dvrlist) {
                    remap.put(dvr.getStoreName(), dvr);
                }
            }
        }catch (Exception e){
            LOG.error("error:",e);
        }
        return remap;
    }

    //整理存储信息
    private Map<String,String> getStorNameMap(Map<String, Object> storagemap){
        Map<String,String> remap = null;
        try {
            if (storagemap != null && storagemap.get("data") != null) {
                List<Storage> list = (List<Storage>) storagemap.get("data");
                if (list != null && list.size() > 0) {
                    remap = new HashMap<>();
                    for (Storage sr : list) {
                        remap.put(sr.getId(), sr.getName());
                    }
                }
            }
        }catch (Exception e){
            LOG.error("error:",e);
        }
        return remap;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }
}
