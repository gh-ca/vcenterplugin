package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.vmware.VCConnectionHelper;
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
    protected VCConnectionHelper vcConnectionHelper;

    public VCConnectionHelper getVcConnectionHelper() {
        return vcConnectionHelper;
    }

    public void setVcConnectionHelper(VCConnectionHelper vcConnectionHelper) {
        this.vcConnectionHelper = vcConnectionHelper;
    }

    protected boolean check(VmwareContext context, String objectId,
                            String hostSetting, Object recommendValue) throws Exception {
        Object v = getCurrentValue(context, objectId, hostSetting);
        if (String.valueOf(v).equals(String.valueOf(recommendValue))) {
            return true;
        }

        return false;
    }

    protected void update(VmwareContext context, String objectId,
                          String hostSetting, Object recommendValue) throws Exception {
        if (check(context, objectId, hostSetting, recommendValue)) {
            return;
        }
        ManagedObjectReference mor = vcConnectionHelper.objectID2MOR(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<OptionValue> values = hostMo.getHostAdvanceOptionMO().queryOptions(hostSetting);
        for (OptionValue value : values) {
            value.setValue(recommendValue);
        }
        hostMo.getHostAdvanceOptionMO().UpdateOptions(values);
    }

    protected Object getCurrentValue(VmwareContext context,
                                     String objectId, String hostSetting) throws Exception {
        ManagedObjectReference mor = vcConnectionHelper.objectID2MOR(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<OptionValue> values = hostMo.getHostAdvanceOptionMO().queryOptions(hostSetting);
        for (OptionValue value : values) {
            return value.getValue();
        }
        return null;
    }

    protected boolean checkModuleOption(VmwareContext context, String objectId,
                                        String optionName, Object recommendValue) throws Exception {
        String currentValue = getCurrentModuleOption(context, objectId, optionName);
        if (currentValue.equals(String.valueOf(recommendValue))) {
            return true;
        }
        return false;
    }

    protected String getCurrentModuleOption(VmwareContext context, String objectId, String optionName) throws Exception {
        ManagedObjectReference mor = vcConnectionHelper.objectID2MOR(objectId);
        HostMO hostMo = new HostMO(context, mor);
        String modlueOption = hostMo.getHostKernelModuleSystemMO().queryConfiguredModuleOptionString(optionName);
        //拆分值
        String[] s = modlueOption.split("=");
        return s[1];
    }

    protected void updateModuleOption(VmwareContext context, String objectId,
                                      String optionName, Object recommendValue) throws Exception {
        if (checkModuleOption(context, objectId, optionName, recommendValue)) {
            return;
        }
        ManagedObjectReference mor = vcConnectionHelper.objectID2MOR(objectId);
        HostMO hostMo = new HostMO(context, mor);
        String v = optionName + "=" + String.valueOf(recommendValue);
        hostMo.getHostKernelModuleSystemMO().updateModuleOptionString(optionName, v);
    }
}
