package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostAdvanceOptionMo;
import com.huawei.vmware.mo.HostMo;
import com.huawei.vmware.util.DatastoreVmwareMoFactory;
import com.huawei.vmware.util.HostVmwareFactory;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OptionValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * BaseBestPracticeService 最佳实践实现基类
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class BaseBestPracticeService {
    protected final Logger logger = LoggerFactory.getLogger(BaseBestPracticeService.class);

    /**
     * check Best Practice
     *
     * @param vcsdkUtils vcsdkUtils vim25 class
     * @param objectId objectId
     * @param hostSetting hostSetting
     * @param recommendValue recommendValue
     * @return boolean
     * @throws Exception Exception
     */
    protected boolean check(final VCSDKUtils vcsdkUtils, final String objectId, final String hostSetting,
        final Object recommendValue) throws Exception {
        Object currentValue = getCurrentValue(vcsdkUtils, objectId, hostSetting);
        if (String.valueOf(currentValue).equals(String.valueOf(recommendValue))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * update Best Practice
     *
     * @param vcsdkUtils vcsdkUtils vim25 class
     * @param objectId objectId
     * @param hostSetting hostSetting
     * @param recommendValue recommendValue
     * @throws Exception Exception
     */
    protected void update(final VCSDKUtils vcsdkUtils, final String objectId, final String hostSetting,
        final Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        if (check(vcsdkUtils, objectId, hostSetting, recommendValue)) {
            return;
        }
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        List<OptionValue> values = hostMo.getHostAdvanceOptionMo().queryOptions(hostSetting);
        for (OptionValue value : values) {
            value.setValue(recommendValue);
        }
        hostMo.getHostAdvanceOptionMo().updateOptions(values);
        hostMo.getHostStorageSystemMo().refreshStorageSystem();
    }

    /**
     * update Best Practice
     *
     * @param vcsdkUtils vcsdkUtils vim25 class
     * @param objectId objectId
     * @param hostSetting hostSetting
     * @return Object
     * @throws Exception Exception
     */
    protected Object getCurrentValue(final VCSDKUtils vcsdkUtils, final String objectId,
                                     final String hostSetting) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        HostAdvanceOptionMo hostAdvanceOptionMo = hostMo.getHostAdvanceOptionMo();
        List<OptionValue> values = hostAdvanceOptionMo.queryOptions(hostSetting);
        for (OptionValue value : values) {
            return value.getValue();
        }
        return "--";
    }

    /**
     * check module option
     *
     * @param vcsdkUtils vcsdkUtils vim25 class
     * @param objectId objectId
     * @param moduleName moduleName
     * @param recommendValue recommendValue
     * @return boolean
     * @throws Exception Exception
     */
    protected boolean checkModuleOption(final VCSDKUtils vcsdkUtils, final String objectId, final String moduleName,
        final Object recommendValue) throws Exception {
        String currentValue = getCurrentModuleOption(vcsdkUtils, objectId, moduleName);
        if (currentValue.equals(String.valueOf(recommendValue))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get current module option
     *
     * @param vcsdkUtils vcsdkUtils
     * @param objectId objectId
     * @param moduleName moduleName
     * @return String
     * @throws Exception Exception
     */
    protected String getCurrentModuleOption(final VCSDKUtils vcsdkUtils, final String objectId, final String moduleName)
        throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        String modlueOption = hostMo.getHostKernelModuleSystemMo().queryConfiguredModuleOptionString(moduleName);
        String[] modlues = modlueOption.split("=");
        return modlues[1];
    }

    /**
     * update module option
     *
     * @param vcsdkUtils vcsdkUtils
     * @param objectId objectId
     * @param moduleName moduleName
     * @param optionName optionName
     * @param recommendValue recommendValue
     * @throws Exception Exception
     */
    protected void updateModuleOption(final VCSDKUtils vcsdkUtils, final String objectId, final String moduleName,
        final String optionName, final Object recommendValue) throws Exception {
        if (checkModuleOption(vcsdkUtils, objectId, moduleName, recommendValue)) {
            return;
        }
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        String options = optionName + "=" + recommendValue;
        hostMo.getHostKernelModuleSystemMo().updateModuleOptionString(moduleName, options);
        hostMo.getHostStorageSystemMo().refreshStorageSystem();
    }

    public HostVmwareFactory getHostMoFactory() {
        return HostVmwareFactory.getInstance();
    }

    public DatastoreVmwareMoFactory getDatastoreMoFactory() {
        return DatastoreVmwareMoFactory.getInstance();
    }
}
