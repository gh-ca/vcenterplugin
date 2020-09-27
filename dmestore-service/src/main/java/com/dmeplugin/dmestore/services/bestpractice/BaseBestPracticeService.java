package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OptionValue;

import java.util.List;

/**
 * @ClassName BaseBestPracticeService
 * @Description TODO
 * @Author wangxiangyong
 * @Date 2020/9/15 11:14
 * @Version V1.0
 **/
public class BaseBestPracticeService {

    protected boolean check(VCSDKUtils vcsdkUtils, String objectId,
                            String hostSetting, Object recommendValue) throws Exception {
        Object v = getCurrentValue(vcsdkUtils, objectId, hostSetting);
        if (String.valueOf(v).equals(String.valueOf(recommendValue))) {
            return true;
        }

        return false;
    }

    protected void update(VCSDKUtils vcsdkUtils, String objectId,
                          String hostSetting, Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        if (check(vcsdkUtils, objectId, hostSetting, recommendValue)) {
            return;
        }
        HostMO hostMo = new HostMO(context, mor);
        List<OptionValue> values = hostMo.getHostAdvanceOptionMO().queryOptions(hostSetting);
        for (OptionValue value : values) {
            value.setValue(recommendValue);
        }
        hostMo.getHostAdvanceOptionMO().UpdateOptions(values);
    }

    protected Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId, String hostSetting) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<OptionValue> values = hostMo.getHostAdvanceOptionMO().queryOptions(hostSetting);
        for (OptionValue value : values) {
            return value.getValue();
        }
        return null;
    }

    protected boolean checkModuleOption(VCSDKUtils vcsdkUtils, String objectId,
                                        String optionName, Object recommendValue) throws Exception {
        String currentValue = getCurrentModuleOption(vcsdkUtils, objectId, optionName);
        if (currentValue.equals(String.valueOf(recommendValue))) {
            return true;
        }
        return false;
    }

    protected String getCurrentModuleOption(VCSDKUtils vcsdkUtils, String objectId, String optionName) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        String modlueOption = hostMo.getHostKernelModuleSystemMO().queryConfiguredModuleOptionString(optionName);
        //拆分值
        String[] s = modlueOption.split("=");
        return s[1];
    }

    protected void updateModuleOption(VCSDKUtils vcsdkUtils, String objectId,
                                      String optionName, Object recommendValue) throws Exception {
        if (checkModuleOption(vcsdkUtils, objectId, optionName, recommendValue)) {
            return;
        }
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        String v = optionName + "=" + String.valueOf(recommendValue);
        hostMo.getHostKernelModuleSystemMO().updateModuleOptionString(optionName, v);
    }

}
