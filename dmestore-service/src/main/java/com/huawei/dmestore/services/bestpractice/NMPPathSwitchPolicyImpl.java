package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.services.DmeAccessService;
import com.huawei.dmestore.services.VCenterInfoService;
import com.huawei.dmestore.utils.VCSDKUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * NMPPathSwitchPolicyImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class NMPPathSwitchPolicyImpl extends BaseBestPracticeService implements BestPracticeService {
    private ThreadPoolTaskExecutor threadPoolExecutor;

    private DmeAccessService dmeAccessService;

    public ThreadPoolTaskExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolTaskExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    private VCenterInfoService vcenterinfoservice;

    public void setvCenterInfoService(VCenterInfoService vcenterService) {
        this.vcenterinfoservice = vcenterService;
    }

    @Override
    public String getHostSetting() {
        return "NMP path switch policy";
    }

    @Override
    public Object getRecommendValue() {
        return new StringBuilder("VMW_SATP_ALUA/VMW_PSP_RR").append("$$").append("VMW_SATP_DEFAULT_AA/VMW_PSP_RR").toString();
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        Map<String, Map<String, String>> satpRules = vcsdkUtils.satpRuleList(objectId, vcenterinfoservice.getVcenterInfo());
        if(satpRules.size() > 0){
            for(Map.Entry<String, Map<String, String>> entry : satpRules.entrySet()){
                Map<String, String> satpRuleMap = entry.getValue();
                return satpRuleMap.get("satp") + "/" + satpRuleMap.get("psp");
            }
        }
        return "";
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
        Map<String, Map<String, String>> satpRules = vcsdkUtils.satpRuleList(objectId, vcenterinfoservice.getVcenterInfo());
        return satpRules.size() == 2;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        vcsdkUtils.satpRuleAdd(objectId, vcenterinfoservice.getVcenterInfo());
    }


    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }
}
