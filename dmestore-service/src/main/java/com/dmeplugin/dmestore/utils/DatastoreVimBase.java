package com.dmeplugin.dmestore.utils;


import com.vmware.vim25.DatastoreInfo;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;

import java.util.*;

public class DatastoreVimBase   {
    private ConnectedVimServiceBase connectedVimServiceBase;

    public ConnectedVimServiceBase getConnectedVimServiceBase() {
        return connectedVimServiceBase;
    }

    public void setConnectedVimServiceBase(ConnectedVimServiceBase connectedVimServiceBase) {
        this.connectedVimServiceBase = connectedVimServiceBase;
    }

    public DatastoreVimBase(ConnectedVimServiceBase connectedVimServiceBase) {
        this.connectedVimServiceBase = connectedVimServiceBase;
    }

    private static final String DATA_CENTER = "Datacenter";
    /**
     * 获取数据中心列表
     */
    public Map<String, ManagedObjectReference> getDataCenters()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return connectedVimServiceBase.getMOREFs.inContainerByType(connectedVimServiceBase.serviceContent.getRootFolder(), DATA_CENTER);
    }

    /**
     * 获取datastore列表
     */
    public Map<String, ManagedObjectReference> getDatastores()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        Map<String, ManagedObjectReference> returndatastores=new HashMap<>();
        //查找datacenter
       /* Map<String, ManagedObjectReference> dataCenters = this.getMOREFs.inContainerByType(this.serviceContent.getRootFolder(), DATA_CENTER);
        for (ManagedObjectReference dataCenter : dataCenters.values()) {
            if (dataCenter != null) {
                //查找datacenter下的datastore
                Map<String, ManagedObjectReference> datastores =
                        getMOREFs.inContainerByType(dataCenter, "Datastore");

                returndatastores.putAll(datastores);
            }
        }*/

          returndatastores = connectedVimServiceBase.getMOREFs.inFolderByType(connectedVimServiceBase.serviceContent.getRootFolder(),"Datastore");
        return returndatastores;
    }

    /**
     * 获取所有datastore列表详情,返回datastoreinfo接口，可以根据instance of VmfsDatastoreInfo,NasDatastoreInfo来判断是否是vmfs,或者nfs datstore
     */
    public List<DatastoreInfo> getAllDatastoreInfo() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<DatastoreInfo> returndatastoreinfos=new ArrayList<>();
        Map<String, ManagedObjectReference> datastores=getDatastores();
        /*for (ManagedObjectReference datastore:datastores.values()){
            this.getMOREFs.entityProps(mor)
        }*/
        Collection<Map<String, Object>> restcollection=connectedVimServiceBase.getMOREFs.entityProps(new ArrayList<>(datastores.values()),new String[]{"info"}).values();

        for (Map<String, Object> dsmap: restcollection){
            DatastoreInfo dsinfo= (DatastoreInfo) dsmap.get("info");
            returndatastoreinfos.add(dsinfo);
        }
        return returndatastoreinfos;
    }
}
