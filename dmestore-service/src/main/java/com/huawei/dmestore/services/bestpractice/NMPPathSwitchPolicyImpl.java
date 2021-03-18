package com.huawei.dmestore.services.bestpractice;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.services.DmeAccessService;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMo;
import com.huawei.vmware.mo.HostStorageSystemMo;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NMPPathSwitchPolicyImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class NMPPathSwitchPolicyImpl extends BaseBestPracticeService implements BestPracticeService {
    private DmeAccessService dmeAccessService;

    private static final int DIVISOR_100 = 100;

    private static final int HTTP_SUCCESS = 2;

    @Override
    public String getHostSetting() {
        return "NMP path switch policy";
    }

    @Override
    public Object getRecommendValue() {
        return "VMW_PSP_RR";
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        List<HostMultipathInfoLogicalUnit> lunList = getLuns(vcsdkUtils, objectId);
        JsonArray array = new JsonArray();
        for (HostMultipathInfoLogicalUnit lun : lunList) {
            HostMultipathInfoLogicalUnitPolicy policy = lun.getPolicy();
            String policyStr = policy.getPolicy();
            if (!policyStr.equals(getRecommendValue())) {
                JsonObject object = new JsonObject();
                object.addProperty("name", "naa." + lun.getId().substring(10, 42));
                object.addProperty("value", policyStr);
                array.add(object);
            }
        }

        return array.toString();
    }

    @Override
    public String getLevel() {
        return "Major";
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
        List<HostMultipathInfoLogicalUnit> lunList = getLuns(vcsdkUtils, objectId);
        for (HostMultipathInfoLogicalUnit lun : lunList) {
            HostMultipathInfoLogicalUnitPolicy psp = lun.getPolicy();
            String pspPolicy = psp.getPolicy();

            // 多路径选路策略，集中式存储选择VMW_SATP_ALUA, VMW_PSP_RR
            if (!pspPolicy.equals(getRecommendValue())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        if (check(vcsdkUtils, objectId)) {
            return;
        }
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        HostStorageSystemMo hostStorageSystemMo = hostMo.getHostStorageSystemMo();
        List<HostMultipathInfoLogicalUnit> lunList = getLuns(vcsdkUtils, objectId);
        if (lunList != null && lunList.size() > 0) {
            ExecutorService executor = Executors.newFixedThreadPool(lunList.size());
            for (HostMultipathInfoLogicalUnit lun : lunList) {
                executor.execute(() -> {
                    HostMultipathInfoLogicalUnitPolicy policy = lun.getPolicy();
                    String pspPolicy = policy.getPolicy();
                    // 多路径选路策略，集中式存储选择VMW_SATP_ALUA, VMW_PSP_RR
                    if (!pspPolicy.equals(getRecommendValue())) {
                        policy.setPolicy((String) getRecommendValue());
                        try {
                            hostStorageSystemMo.setMultipathLunPolicy(lun.getId(), policy);
                        } catch (Exception exception) {
                            logger.error("setMultipathLunPolicy error!lun_id={}", lun.getId());
                        }
                    }
                });
            }
        }
    }

    private List<HostMultipathInfoLogicalUnit> getLuns(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        HostStorageSystemMo hostStorageSystemMo = hostMo.getHostStorageSystemMo();
        HostStorageDeviceInfo deviceInfo = hostStorageSystemMo.getStorageDeviceInfo();
        HostMultipathInfo hostMultipathInfo = deviceInfo.getMultipathInfo();
        List<HostMultipathInfoLogicalUnit> lunList = hostMultipathInfo.getLun();
        return pickupDmeLun(lunList);
    }

    private List<HostMultipathInfoLogicalUnit> pickupDmeLun(List<HostMultipathInfoLogicalUnit> lunList) {
        List<HostMultipathInfoLogicalUnit> targetList = new ArrayList<>();
        for (int index = 0; index < lunList.size(); index++) {
            HostMultipathInfoLogicalUnit logicalUnit = lunList.get(index);
            String id = logicalUnit.getId();
            if(id.length() < 42){
                logger.debug("hostMultipathInfoLogicalUnit is not dme volume!id={}", id);
                continue;
            }
            String wwn = id.substring(10, 42);
            // 根据wwn从DME中查询卷信息,如果查找到则说明是华为存储。
            String volumeUrlByName = DmeConstants.DME_VOLUME_BASE_URL + "?volume_wwn=" + wwn;
            try {
                ResponseEntity<String> responseEntity = dmeAccessService.access(volumeUrlByName, HttpMethod.GET, null);
                if (responseEntity.getStatusCodeValue() / DIVISOR_100 != HTTP_SUCCESS) {
                    continue;
                }
                JsonObject jsonObject = new Gson().fromJson(responseEntity.getBody(), JsonObject.class);
                if (jsonObject.get("volumes").getAsJsonArray().size() > 0) {
                    targetList.add(logicalUnit);
                } else {
                    logger.debug("not found the volume！wwn={}", wwn);
                }
            } catch (DmeException e) {
                logger.error("get dme volume error！wwn={}", wwn);
               continue;
            }
        }
        return targetList;
    }

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }
}
