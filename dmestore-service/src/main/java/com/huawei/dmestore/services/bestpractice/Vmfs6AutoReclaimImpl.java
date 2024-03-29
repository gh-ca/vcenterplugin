package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.DatastoreMo;
import com.huawei.vmware.mo.HostMo;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Vmfs6AutoReclaimImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class Vmfs6AutoReclaimImpl extends BaseBestPracticeService implements BestPracticeService {
    protected final Logger logger = LoggerFactory.getLogger(BaseBestPracticeService.class);
    private ThreadPoolTaskExecutor threadPoolExecutor;

    public ThreadPoolTaskExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolTaskExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public String getHostSetting() {
        return "VMFS-6 Auto-Space Reclamation";
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
                if (unmapPriority != null && !unmapPriority.equals((String) getRecommendValue())) {
                    JsonObject object = new JsonObject();
                    object.addProperty("name", vmfsDatastoreInfo.getName());
                    object.addProperty("value", unmapPriority);

                    object.addProperty("dataStoreName", vmfsDatastoreInfo.getName());
                    object.addProperty("unmapPriority", unmapPriority);
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
                if (null != unmapPriority && !unmapPriority.equals((String) getRecommendValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<VmfsDatastoreInfo> getVmfs6DatastoreInfo(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        List<VmfsDatastoreInfo> list = new ArrayList<>();
        List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = hostMo.getDatastoreMountsOnHost();
        for (Pair<ManagedObjectReference, String> pair : datastoreMountsOnHost) {
            ManagedObjectReference dsMor = pair.first();
            DatastoreMo datastoreMo = this.getDatastoreMoFactory().build(context, dsMor);
            DatastoreSummary summary = datastoreMo.getSummary();

            // 连通性。false为不可访问的数据库存储，应该过滤不检查和更新。
            boolean accessible = summary.isAccessible();
            if (accessible && summary.getType().equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS)) {
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
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        List<VmfsDatastoreInfo> list = getVmfs6DatastoreInfo(vcsdkUtils, objectId);
        if (list.size() > 0) {
            //ExecutorService executor = Executors.newFixedThreadPool(list.size());
            for (VmfsDatastoreInfo vmfsDatastoreInfo : list) {
                HostVmfsVolume hostVmfsVolume = vmfsDatastoreInfo.getVmfs();
                String uuid = hostVmfsVolume.getUuid();
                String unmapPriority = hostVmfsVolume.getUnmapPriority();
                if (null != unmapPriority && !unmapPriority.equals((String)getRecommendValue())) {
                    threadPoolExecutor.execute(() -> {
                        try {
                            hostMo.getHostStorageSystemMo().updateVmfsUnmapPriority(uuid, (String) getRecommendValue());
                        } catch (Exception exception) {
                            logger.error("updateVmfsUnmapPriority error!hostObjectId={},vmfsName={}", objectId,
                                hostVmfsVolume.getName());
                            exception.printStackTrace();
                        }
                    });
                }
            }
        }
    }
}
