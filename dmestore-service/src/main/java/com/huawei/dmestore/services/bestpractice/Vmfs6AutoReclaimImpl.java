package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.DatastoreMO;
import com.huawei.vmware.mo.HostMO;
import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareContext;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.HostVmfsVolume;
import com.vmware.vim25.HostVmfsVolumeUnmapPriority;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VmfsDatastoreInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Vmfs6AutoReclaimImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class Vmfs6AutoReclaimImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "VMFS-6 Auto Reclaim";
    }

    @Override
    public Object getRecommendValue() {
        return HostVmfsVolumeUnmapPriority.LOW.value();
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        List<VmfsDatastoreInfo> list = getVmfs6DatastoreInfo(vcsdkUtils, objectId);
        if (list.size() > 0) {
            JsonArray array = new JsonArray();
            for (VmfsDatastoreInfo vmfsDatastoreInfo : list) {
                HostVmfsVolume hostVmfsVolume = vmfsDatastoreInfo.getVmfs();
                String unmapPriority = hostVmfsVolume.getUnmapPriority();
                if (unmapPriority == null || !unmapPriority.equals(getRecommendValue())) {
                    JsonObject object = new JsonObject();
                    object.addProperty("name", vmfsDatastoreInfo.getName());
                    object.addProperty("value", unmapPriority == null ? "--" : unmapPriority);
                    array.add(object);
                }
            }
            return array.toString();
        }
        return getRecommendValue();
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
                if (null == unmapPriority || !unmapPriority.equals(getRecommendValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<VmfsDatastoreInfo> getVmfs6DatastoreInfo(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        List<VmfsDatastoreInfo> list = new ArrayList<>();
        List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = hostMo.getDatastoreMountsOnHost();
        for (Pair<ManagedObjectReference, String> pair : datastoreMountsOnHost) {
            ManagedObjectReference dsMor = pair.first();
            DatastoreMO datastoreMo = this.getDatastoreMoFactory().build(context, dsMor);
            DatastoreSummary summary = datastoreMo.getSummary();
            if (summary.getType().equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS)) {
                VmfsDatastoreInfo vmfsDatastoreInfo = datastoreMo.getVmfsDatastoreInfo();
                HostVmfsVolume hostVmfsVolume = vmfsDatastoreInfo.getVmfs();
                // 只对VMFS6进行处理
                String version = hostVmfsVolume.getVersion();
                if (version.startsWith("6")) {
                    list.add(vmfsDatastoreInfo);
                }
            }
        }
        return list;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        List<VmfsDatastoreInfo> list = getVmfs6DatastoreInfo(vcsdkUtils, objectId);
        if (list.size() > 0) {
            ExecutorService executor = Executors.newFixedThreadPool(list.size());
            for (VmfsDatastoreInfo vmfsDatastoreInfo : list) {
                HostVmfsVolume hostVmfsVolume = vmfsDatastoreInfo.getVmfs();
                String uuid = hostVmfsVolume.getUuid();
                String unmapPriority = hostVmfsVolume.getUnmapPriority();
                if (null == unmapPriority || !unmapPriority.equals(String.valueOf(getRecommendValue()))) {
                    executor.execute(() -> {
                        try {
                            hostMo.getHostStorageSystemMo().updateVmfsUnmapPriority(uuid, (String) getRecommendValue());
                        } catch (Exception exception) {
                            LOGGER.error("updateVmfsUnmapPriority error!hostObjectId={},vmfsName={}", objectId,
                                hostVmfsVolume.getName());
                            exception.printStackTrace();
                        }
                    });
                }
            }
        }
    }
}
