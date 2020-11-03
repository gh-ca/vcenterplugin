package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.ToolUtils;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.mo.DatastoreMO;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.HostVmfsVolume;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VmfsDatastoreInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxiangyong
 **/
public class Vmfs6AutoReclaimImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "VMFS-6 Auto Reclaim";
    }

    @Override
    public Object getRecommendValue() {
        return "low";
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        List<VmfsDatastoreInfo> list = getVmfs6DatastoreInfo(vcsdkUtils, objectId);
        if (list.size() > 0) {
            JsonArray array = new JsonArray();
            for (VmfsDatastoreInfo vmfsDatastoreInfo : list) {
                HostVmfsVolume hostVmfsVolume = vmfsDatastoreInfo.getVmfs();
                String unmapPriority = hostVmfsVolume.getUnmapPriority();
                JsonObject object = new JsonObject();
                object.addProperty("dataStoreName", vmfsDatastoreInfo.getName());
                object.addProperty("unmapPriority", unmapPriority);
                array.add(object);
            }
            JsonObject reObject = new JsonObject();
            reObject.add("dataStores", array);
            return reObject.toString();
        }
        return "--";
    }

    @Override
    public String getLevel() {
        return "Info";
    }

    @Override
    public boolean needReboot() {
        return false;
    }

    @Override
    public boolean autoRepair() {
        return true;
    }

    @Override
    public boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        List<VmfsDatastoreInfo> list = getVmfs6DatastoreInfo(vcsdkUtils, objectId);
        if (list.size() > 0) {
            for (VmfsDatastoreInfo vmfsDatastoreInfo : list) {
                HostVmfsVolume hostVmfsVolume = vmfsDatastoreInfo.getVmfs();
                String unmapPriority = hostVmfsVolume.getUnmapPriority();
                if (null == unmapPriority || !unmapPriority.equals(String.valueOf(getRecommendValue()))) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<VmfsDatastoreInfo> getVmfs6DatastoreInfo(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<VmfsDatastoreInfo> list = new ArrayList<>();
        List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = hostMo.getDatastoreMountsOnHost();
        for (Pair<ManagedObjectReference, String> pair : datastoreMountsOnHost) {
            ManagedObjectReference dsMor = pair.first();
            DatastoreMO datastoreMo = new DatastoreMO(context, dsMor);
            DatastoreSummary summary = datastoreMo.getSummary();
            if (summary.getType().equals(ToolUtils.STORE_TYPE_VMFS)) {
                VmfsDatastoreInfo vmfsDatastoreInfo = datastoreMo.getVmfsDatastoreInfo();
                HostVmfsVolume hostVmfsVolume = vmfsDatastoreInfo.getVmfs();
                String version = hostVmfsVolume.getVersion();
                //只对VMFS6进行处理
                if (version.startsWith("6")) {
                    list.add(vmfsDatastoreInfo);
                }
            }
        }
        return list;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        List<VmfsDatastoreInfo> list = getVmfs6DatastoreInfo(vcsdkUtils, objectId);
        if (list.size() > 0) {
            for (VmfsDatastoreInfo vmfsDatastoreInfo : list) {
                HostVmfsVolume hostVmfsVolume = vmfsDatastoreInfo.getVmfs();
                String unmapPriority = hostVmfsVolume.getUnmapPriority();
                if (null == unmapPriority || !unmapPriority.equals(String.valueOf(getRecommendValue()))) {
                    hostVmfsVolume.setUnmapPriority(String.valueOf(getRecommendValue()));
                }
            }
        }
    }
}
