package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.mo.HostAdvanceOptionMO;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.util.DatastoreMOFactory;
import com.dmeplugin.vmware.util.HostMOFactory;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OptionValue;

import java.util.List;

/**
 * 最佳实践检查
 * * @author wangxiangyong
 *
 * @since 2020-11-30
 **/
public class BaseBestPracticeService {
    protected boolean check(final VCSDKUtils vcsdkUtils, final String objectId,
                            final String hostSetting, final Object recommendValue) throws Exception {
        Object v = getCurrentValue(vcsdkUtils, objectId, hostSetting);
        if (String.valueOf(v).equals(String.valueOf(recommendValue))) {
            return true;
        }

        return false;
    }

    protected void update(final VCSDKUtils vcsdkUtils, final String objectId,
                          final String hostSetting, final Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        if (check(vcsdkUtils, objectId, hostSetting, recommendValue)) {
            return;
        }
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        List<OptionValue> values = hostMo.getHostAdvanceOptionMo().queryOptions(hostSetting);
        for (OptionValue value : values) {
            value.setValue(recommendValue);
        }
        hostMo.getHostAdvanceOptionMo().updateOptions(values);
    }

    protected Object getCurrentValue(final VCSDKUtils vcsdkUtils, final String objectId, final String hostSetting) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        HostAdvanceOptionMO hostAdvanceOptionMo = hostMo.getHostAdvanceOptionMo();
        List<OptionValue> values = hostAdvanceOptionMo.queryOptions(hostSetting);
        for (OptionValue value : values) {
            return value.getValue();
        }
        return "--";
    }


    protected boolean checkModuleOption(final VCSDKUtils vcsdkUtils, final String objectId,
                                        final String optionName, final Object recommendValue) throws Exception {
        String currentValue = getCurrentModuleOption(vcsdkUtils, objectId, optionName);
        if (currentValue.equals(String.valueOf(recommendValue))) {
            return true;
        }
        return false;
    }

    protected String getCurrentModuleOption(final VCSDKUtils vcsdkUtils, final String objectId, final String optionName)
        throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        String modlueOption = hostMo.getHostKernelModuleSystemMo().queryConfiguredModuleOptionString(optionName);
        //拆分值
        String[] s = modlueOption.split("=");
        return s[1];
    }

    protected void updateModuleOption(final VCSDKUtils vcsdkUtils, final String objectId,
                                      final String optionName, final Object recommendValue) throws Exception {
        if (checkModuleOption(vcsdkUtils, objectId, optionName, recommendValue)) {
            return;
        }
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        String v = optionName + "=" + String.valueOf(recommendValue);
        hostMo.getHostKernelModuleSystemMo().updateModuleOptionString(optionName, v);
    }

    public HostMOFactory getHostMoFactory() {
        return HostMOFactory.getInstance();
    }

    public DatastoreMOFactory getDatastoreMoFactory() {
        return DatastoreMOFactory.getInstance();
    }

}
