package com.dmeplugin.dmestore.utils;

import com.vmware.vim25.HostListSummary;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HostSystemVimBase   {
    private ConnectedVimServiceBase connectedVimServiceBase;

    public ConnectedVimServiceBase getConnectedVimServiceBase() {
        return connectedVimServiceBase;
    }

    public void setConnectedVimServiceBase(ConnectedVimServiceBase connectedVimServiceBase) {
        this.connectedVimServiceBase = connectedVimServiceBase;
    }

    public HostSystemVimBase(ConnectedVimServiceBase connectedVimServiceBase) {
        this.connectedVimServiceBase = connectedVimServiceBase;
    }

    private static final String DATA_CENTER = "Datacenter";
    /**
     * 获取host主机列表
     */
    public Map<String, ManagedObjectReference> getHosts()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找hosts
        Map<String, ManagedObjectReference> hosts = connectedVimServiceBase.getMOREFs.inFolderByType(connectedVimServiceBase.serviceContent.getRootFolder(),
                "HostSystem");
        return hosts;
    }

    /**
     * 获取所有host的summary
     */
    public List<HostListSummary> getAllHostSummary() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<HostListSummary> hostsummarylist=new ArrayList<>();
        Map<String, ManagedObjectReference> hosts=getHosts();

        Collection<Map<String, Object>> restcollection=connectedVimServiceBase.getMOREFs.entityProps(new ArrayList<>(hosts.values()),new String[]{"summary"}).values();

        for (Map<String, Object> hostmap: restcollection){
            HostListSummary hostsummary= (HostListSummary) hostmap.get("summary");
            hostsummarylist.add(hostsummary);
        }
        return hostsummarylist;
    }
}
