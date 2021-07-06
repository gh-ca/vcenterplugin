package com.huawei.dmestore.services;

import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.ClusterTree;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VmwareAccessServiceImpl
 *
 * @author yy
 * @since 2020-09-02
 **/
public class VmwareAccessServiceImpl implements VmwareAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(VmwareAccessServiceImpl.class);

    private static final String OBJECT_ID = "objectId";

    private static final String HOST_ID = "hostId";

    private static final String HOST_NAME = "hostName";

    private Gson gson = new Gson();

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private VCSDKUtils vcsdkUtils;

    @Autowired
    private VmfsAccessService vmfsAccessService;


    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    @Override
    public List<Map<String, String>> listHosts() throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            // 取得vcenter中的所有host。
            String listStr = vcsdkUtils.getAllHosts();
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("list hosts error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getHostsByDsObjectId(String dataStoreObjectId) throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getHostsByDsObjectId(dataStoreObjectId);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("get Hosts By DsObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> listClusters() throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getAllClusters();
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("list listClusters error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getClustersByDsObjectId(String dataStoreObjectId) throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getClustersByDsObjectId(dataStoreObjectId);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("get Clusters By DsObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getDataStoresByHostObjectId(String hostObjectId, String dataStoreType)
        throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getDataStoresByHostObjectId(hostObjectId, dataStoreType);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() { }.getType());

                // 根据dataStoreType取得数据库对应存储信息，然后以数据库信息为准过滤存储 objectId
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                if (dvrlist != null && dvrlist.size() > 0) {
                    for (Map<String, String> dsmap : tmplists) {
                        if (dsmap != null && dsmap.get(OBJECT_ID) != null && dvrlist.contains(
                            ToolUtils.getStr(dsmap.get(OBJECT_ID)))) {
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (DmeSqlException e) {
            LOG.error("get DataStores By HostObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType)
        throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            // 根据存储类型，取得vcenter中的所有存储。
            String listStr = vcsdkUtils.getDataStoresByClusterObjectId(clusterObjectId, dataStoreType);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() { }.getType());

                // 根据dataStoreType取得数据库对应存储信息，然后以数据库信息为准过滤存储objectId
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                if (dvrlist != null && dvrlist.size() > 0) {
                    for (Map<String, String> dsmap : tmplists) {
                        if (dsmap != null && dsmap.get(OBJECT_ID) != null && dvrlist.contains(
                            ToolUtils.getStr(dsmap.get(OBJECT_ID)))) {
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (VcenterException e) {
            LOG.error("get DataStores By ClusterObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getVmKernelIpByHostObjectId(String hostObjectId) throws DmeException {
        List<Map<String, String>> lists = null;
        //todo 20210705 此接口适配集群入参
        if(StringUtils.isEmpty(hostObjectId)){
            throw new DmeException("param is empty");
        }
        boolean hostFlag= true;
        try {
            String[] temRes = hostObjectId.split(":");

            String tempString = temRes[2];
            if (tempString.contains("Host")) {
                hostFlag = true;
            } else if (tempString.contains("Cluster")) {
                hostFlag = false;
            } else {
                throw new DmeException("param is error");
            }
        }catch (Exception e){
            throw new DmeException("param is error");
        }
     if(hostFlag) {

         try {
             // 根据存储类型，取得vcenter中的所有存储。
             String listStr = vcsdkUtils.getVmKernelIpByHostObjectId(hostObjectId);
             if (!StringUtils.isEmpty(listStr)) {
                 lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                 }.getType());
             }
         } catch (VcenterException e) {
             LOG.error("get vmkernel ip by hostobjectid error:", e);
             throw new DmeException(e.getMessage());
         }
     }else {
         try {
             // 根据存储类型，取得vcenter中的所有存储。
             String listStr = vcsdkUtils.getVmKernelIpByClusterObjectId(hostObjectId);
             if (!StringUtils.isEmpty(listStr)) {
                 lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() {
                 }.getType());
             }
         } catch (VcenterException e) {
             LOG.error("get vmkernel ip by clusterobjectid error:", e);
             throw new DmeException(e.getMessage());
         }
     }
        return lists;
    }

    @Override
    public List<Map<String, String>> getMountDataStoresByHostObjectId(String hostObjectId, String dataStoreType)
        throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            // 根据存储类型，取得vcenter中的所有存储
            String listStr = vcsdkUtils.getMountDataStoresByHostObjectId(hostObjectId, dataStoreType);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() { }.getType());

                // 根据dataStoreType取得数据库对应存储信息，然后以数据库信息为准过滤存储objectId
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                if (dvrlist != null && dvrlist.size() > 0) {
                    for (Map<String, String> dsmap : tmplists) {
                        if (dsmap != null && dsmap.get(OBJECT_ID) != null && dvrlist.contains(
                            ToolUtils.getStr(dsmap.get(OBJECT_ID)))) {
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get mount DataStores By HostObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getMountDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType)
        throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getMountDataStoresByClusterObjectId(clusterObjectId, dataStoreType);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() { }.getType());
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                if (dvrlist != null && dvrlist.size() > 0) {
                    for (Map<String, String> dsmap : tmplists) {
                        if (dsmap != null && dsmap.get(OBJECT_ID) != null && dvrlist.contains(
                            ToolUtils.getStr(dsmap.get(OBJECT_ID)))) {
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get mount DataStores By ClusterObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public DmeVmwareRelation getDmeVmwareRelationByDsId(String storeId) throws DmeSqlException {
        return dmeVmwareRalationDao.getDmeVmwareRelationByDsId(storeId);
    }

    /**
      * @Description:  获取集群和集群下的主机集合以树的形式返回
      * @Param @param null
      * @return @return
      * @throws DmeException
      * @author yc
      * @Date 2021/5/11 15:59
     */
    @Override
    public List<ClusterTree> listclustersReturnTree() throws DmeException {
        List<ClusterTree> clusterTreeList = new ArrayList<>();
        List<Map<String, String>> lists = new ArrayList<>();
        try {
            String listStr = vcsdkUtils.getAllClusters();
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
            if(!CollectionUtils.isEmpty(lists)){
                for (Map<String, String> map : lists) {
                    ClusterTree clusterTree = new ClusterTree();
                    String vmwarehosts = vcsdkUtils.getHostsOnCluster(map.get("clusterId"));
                    List<Map<String, String>> vmwarehostlists = new ArrayList<>();
                    if (!StringUtils.isEmpty(vmwarehosts)) {
                        vmwarehostlists = gson.fromJson(vmwarehosts,
                                new TypeToken<List<Map<String, String>>>() {
                                }.getType());
                    }
                    clusterTree.setClusterId(map.get("clusterId"));
                    clusterTree.setClusterName(map.get("clusterName"));
                    if(!CollectionUtils.isEmpty(getHostList(vmwarehostlists))) {
                        clusterTree.setChildren(getHostList(vmwarehostlists));
                    }
                    //clusterTree.setChildren(Collections.singletonList(vmwarehostlists));
                    clusterTreeList.add(clusterTree);
                }
            //根据获取到的集群列表取得集群下的所有主机

            }
            } catch (VcenterException e) {
            LOG.error("list listClusters error:", e);
            throw new DmeException(e.getMessage());
        }
        return clusterTreeList;
    }

    /**
      * @Description: 将主机集合循环添加至树中
      * @Param @param null
      * @return @return
      * @author yc
      * @Date 2021/5/11 15:58
     */
    public List<ClusterTree> getHostList(List<Map<String, String>> vmwarehostlists) {
        List<ClusterTree> clusterTreeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(vmwarehostlists)){
            for (Map<String, String> map : vmwarehostlists) {
                ClusterTree clusterTree = new ClusterTree();
                clusterTree.setClusterId(map.get("hostId"));
                clusterTree.setClusterName(map.get("hostName"));
                clusterTreeList.add(clusterTree);
            }
        }
        return clusterTreeList;
    }

    public List<ClusterTree> getMountableHostList(List<Map<String, String>> vmwarehostlists,List<String> mountedHostId ) {
        List<ClusterTree> clusterTreeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(vmwarehostlists)){
            for (Map<String, String> map : vmwarehostlists) {
                ClusterTree clusterTree = new ClusterTree();
                clusterTree.setClusterId(map.get("hostId"));
                clusterTree.setClusterName(map.get("hostName"));
                if (!CollectionUtils.isEmpty(mountedHostId) && mountedHostId.contains(map.get("hostId"))) {
                    clusterTree.setFlag(false);
                }else {
                    clusterTree.setFlag(true);
                }
                clusterTreeList.add(clusterTree);
            }
        }
        return clusterTreeList;
    }
    /**
     * @Description: 挂载页面查询可挂载的主机和集群
     * @Param @param null
     * @return @return
     * @throws
     * @author yc
     * @Date 2021/5/14 9:47
     */
    @Override
    public List<ClusterTree> getClustersAndHostsByDsObjectIdNew(String dataStoreObjectId) throws DmeException {
        List<String> mountedHostId = null;
        List<String> clusteridList = new ArrayList<>();
        List<Map<String, String>> hostList = new ArrayList<>();
        List<Map<String, String>> clusterList = new ArrayList<>();
        List<ClusterTree> treeList = new ArrayList<>();
        try {
            //1.根据存储查询已经挂载的主机
            mountedHostId = vcsdkUtils.getAllMountedHostId(dataStoreObjectId);
            //2.获取所有主机
            //3.获取所有集群
            // 取得vcenter中的所有host。
            String hostListStr = vcsdkUtils.getAllHosts();
            if (!StringUtils.isEmpty(hostListStr)) {
                hostList = gson.fromJson(hostListStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
            String clusterListStr = vcsdkUtils.getAllClusters();
            if (!StringUtils.isEmpty(clusterListStr)) {
                clusterList = gson.fromJson(clusterListStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
                if (!CollectionUtils.isEmpty(clusterList)){
                    for (Map<String, String> map : clusterList) {
                        clusteridList.add(map.get("clusterId"));
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get all mountable hosts and clusters by  datastoreobjectid error:", e);
            throw new DmeException("get all mountable hosts and clusters by  datastoreobjectid error:" + e.getMessage());
        }
        //4.循环获取的主机，剔除已经挂载的主机
        Map<String, Map<String, String>> temp = new HashMap<>();
        if (!CollectionUtils.isEmpty(hostList)) {
            temp = getHostTempList(hostList);
            //todo 查询主机是否属于集群，如果属于集群就不返回,或者主机在已挂载的主机集合中就不返回
            for (Map<String, String> hostidMap : hostList) {
                if (!CollectionUtils.isEmpty(clusteridList)) {
                    for (String clusterid : clusteridList) {
                        List<String> hosts = vcsdkUtils.getHostidsOnCluster(clusterid);
                        if (!CollectionUtils.isEmpty(hosts) && hosts.contains(hostidMap.get(HOST_ID))){
                               // || (!CollectionUtils.isEmpty(mountedHostId) && mountedHostId.contains(hostidMap.get(HOST_ID)))) {
                            temp.remove(hostidMap.get(HOST_ID));
                        }
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(temp)) {
            for (String key : temp.keySet()) {
                ClusterTree clusterTree = new ClusterTree();
                clusterTree.setClusterId(temp.get(key).get(HOST_ID));
                clusterTree.setClusterName(temp.get(key).get(HOST_NAME));
                if(!CollectionUtils.isEmpty(mountedHostId) && mountedHostId.contains(temp.get(key).get(HOST_ID))) {
                    clusterTree.setFlag(false);
                }else {
                    clusterTree.setFlag(true);
                }
                treeList.add(clusterTree);
            }
        }
        //处理集群,从集群的主机中剔除已经挂载的主机
        if (!CollectionUtils.isEmpty(clusterList)) {
            for (Map<String, String> map : clusterList) {
                ClusterTree clusterTree = new ClusterTree();
                String vmwarehosts = vcsdkUtils.getHostsOnCluster(map.get("clusterId"));
                List<Map<String, String>> vmwarehostlists = new ArrayList<>();
                if (!StringUtils.isEmpty(vmwarehosts)) {
                    vmwarehostlists = gson.fromJson(vmwarehosts,
                            new TypeToken<List<Map<String, String>>>() {
                            }.getType());
                }
                if (!CollectionUtils.isEmpty(vmwarehostlists)) {
                    temp = getHostTempList(vmwarehostlists);
                   /* for (Map<String, String> hostMap : vmwarehostlists) {
                        if (!CollectionUtils.isEmpty(mountedHostId) && mountedHostId.contains(hostMap.get(HOST_ID))) {
                            temp.remove(hostMap.get(HOST_ID));
                        }
                    }*/
                    List<Map<String, String>> tempHostInfo = new ArrayList<>(temp.values());
                    List<ClusterTree> hostListTmp = getMountableHostList(tempHostInfo,mountedHostId);
                    if (!CollectionUtils.isEmpty(hostListTmp)) {
                        clusterTree.setClusterId(map.get("clusterId"));
                        clusterTree.setClusterName(map.get("clusterName"));
                        clusterTree.setChildren(CollectionUtils.isEmpty(hostListTmp) ? null : hostListTmp);
                        boolean flag = getFlag(hostListTmp);
                        clusterTree.setFlag(flag);

                    }
                    if (!StringUtils.isEmpty(clusterTree.getClusterId())) {
                        treeList.add(clusterTree);
                    }
                }
            }
        }

        return treeList;
    }

    private boolean getFlag(List<ClusterTree> hostListTmp) {
        boolean flag = false;
        if (!CollectionUtils.isEmpty(hostListTmp)){
            for (ClusterTree clusterTree : hostListTmp) {
               if( clusterTree.isFlag()){
                    return  true;
                }
            }
        }
        return flag;
    }

    /**
      * @Description: 获取可挂载的主机数据
      * @Param @param null
      * @return @return
      * @throws
      * @author yc
      * @Date 2021/6/8 10:21
     */
    private List<ClusterTree> getMountableHostsByDsObjectId (String dataStoreObjectId) throws DmeException {
        List<ClusterTree> clusterTreeList = new ArrayList<>();
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getHostsByDsObjectId(dataStoreObjectId);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("get Hosts By DsObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(lists)){
            throw new DmeException("get Hosts By DsObjectId error");
        }
        for (Map<String,String> hostMap :  lists) {
            ClusterTree clusterTree = new ClusterTree();
            clusterTree.setClusterId(hostMap.get(HOST_ID));
            clusterTree.setClusterName(hostMap.get(HOST_NAME));
            if (!StringUtils.isEmpty(clusterTree.getClusterId())){
                clusterTreeList.add(clusterTree);
            }
        }
        return clusterTreeList;
    }
    /**
      * @Description: 创建vmfs时，以树的方式返回可用的主机和集群
      * @Param @param null
      * @return @return 
      * @throws 
      * @author yc
      * @Date 2021/6/7 17:32
     */
    @Override
    public List<ClusterTree> listHostsAndClusterReturnTree() throws DmeException {
        List<ClusterTree> treeList = new ArrayList<>();
        List<Map<String, String>> hostList = new ArrayList<>();
        List<Map<String, String>> clusterList = new ArrayList<>();
        List<String> clusteridList = null;
        try {
            // 取得vcenter中的所有host。
            String hostListStr = vcsdkUtils.getAllHosts();
            clusteridList = vcsdkUtils.getAllClusterIds();
            if (!StringUtils.isEmpty(hostListStr)) {
                hostList = gson.fromJson(hostListStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
            String clusterListStr = vcsdkUtils.getAllClusters();
            if (!StringUtils.isEmpty(clusterListStr)) {
                clusterList = gson.fromJson(clusterListStr, new TypeToken<List<Map<String, String>>>() {
                }.getType());
            }
            //获取独立主机
            //准备暂存集合
            Map<String, Map<String, String>> temp = new HashMap<>();
            if (!CollectionUtils.isEmpty(hostList)) {
                temp = getHostTempList(hostList);
                //todo 查询主机是否属于集群，如果属于集群就不返回
                for (Map<String, String> hostidMap : hostList) {
                    if (!CollectionUtils.isEmpty(clusteridList)) {
                        for (String clusterid : clusteridList) {
                            List<String> hosts = vcsdkUtils.getHostidsOnCluster(clusterid);
                            if (!CollectionUtils.isEmpty(hosts) && hosts.contains(hostidMap.get(HOST_ID))) {
                                temp.remove(hostidMap.get(HOST_ID));
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(temp)) {
                for (String key : temp.keySet()) {
                    ClusterTree clusterTree = new ClusterTree();
                    clusterTree.setClusterId(temp.get(key).get(HOST_ID));
                    clusterTree.setClusterName(temp.get(key).get(HOST_NAME));
                    treeList.add(clusterTree);
                }
            }
            //处理集群
            if (!CollectionUtils.isEmpty(clusterList)) {
                for (Map<String, String> map : clusterList) {
                    ClusterTree clusterTree = new ClusterTree();
                    String vmwarehosts = vcsdkUtils.getHostsOnCluster(map.get("clusterId"));
                    List<Map<String, String>> vmwarehostlists = new ArrayList<>();
                    if (!StringUtils.isEmpty(vmwarehosts)) {
                        vmwarehostlists = gson.fromJson(vmwarehosts,
                                new TypeToken<List<Map<String, String>>>() {
                                }.getType());
                    }
                    List<ClusterTree> hostListTmp = getHostList(vmwarehostlists);
                    if (!CollectionUtils.isEmpty(hostListTmp)) {
                        clusterTree.setClusterId(map.get("clusterId"));
                        clusterTree.setClusterName(map.get("clusterName"));
                        clusterTree.setChildren(hostListTmp);
                    }
                    if (!StringUtils.isEmpty(clusterTree.getClusterId())) {
                        treeList.add(clusterTree);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("list hosts And clusters return tree error:", e);
            throw new DmeException(e.getMessage());
        }
        return treeList;
    }
    /**
      * @Description: 获取暂存主机集合对象
      * @Param @param null
      * @return @return 
      * @throws 
      * @author yc
      * @Date 2021/6/7 18:44
     */
    private Map<String, Map<String, String>> getHostTempList(List<Map<String, String>> hostList) {
        Map<String,Map<String,String>> temp = new HashMap<>();
        if (!CollectionUtils.isEmpty(hostList)) {
            for (Map<String, String> hostidMap : hostList) {
                String hostId = hostidMap.get(HOST_ID);
                temp.put(hostId,hostidMap);
            }
        }
        return temp;
    }
        
}