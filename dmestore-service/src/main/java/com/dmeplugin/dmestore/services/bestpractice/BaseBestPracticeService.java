package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.mo.HostDatastoreSystemMO;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.mo.HostStorageSystemMO;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.HostMultipathInfoLogicalUnit;
import com.vmware.vim25.HostVirtualSwitch;
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

    protected Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId, Integer recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<HostVirtualSwitch>  hostVirtualSwitchList = hostMo.getHostVirtualSwitch();
        for(HostVirtualSwitch hostVirtualSwitch : hostVirtualSwitchList){
            if(hostVirtualSwitch.getMtu().intValue() != recommendValue.intValue()){
                return hostVirtualSwitch.getMtu();
            }
        }

        return null;
    }

    protected boolean check(VCSDKUtils vcsdkUtils, String objectId, Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<HostVirtualSwitch>  hostVirtualSwitchList = hostMo.getHostVirtualSwitch();
        for(HostVirtualSwitch hostVirtualSwitch : hostVirtualSwitchList){
            if(hostVirtualSwitch.getMtu().intValue() != ((Integer)recommendValue).intValue()){
                return false;
            }
        }

        return true;
    }

    protected void update(VCSDKUtils vcsdkUtils, String objectId, Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        if (check(vcsdkUtils, objectId, recommendValue)) {
            return;
        }
        HostMO hostMo = new HostMO(context, mor);
        List<HostVirtualSwitch>  hostVirtualSwitchList = hostMo.getHostVirtualSwitch();
        for(HostVirtualSwitch hostVirtualSwitch : hostVirtualSwitchList){
            if(hostVirtualSwitch.getMtu().intValue() != ((Integer)recommendValue).intValue()){
                hostVirtualSwitch.setMtu((Integer)recommendValue);
            }
        }
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

    protected void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        HostStorageSystemMO hostStorageSystemMO = hostMo.getHostStorageSystemMO();
        List<HostMultipathInfoLogicalUnit> lunList = hostStorageSystemMO.getStorageDeviceInfo().getMultipathInfo().getLun();
        for(HostMultipathInfoLogicalUnit hostMultipathInfoLogicalUnit : lunList){
            //多路径选路策略，集中式存储选择VMW_SATP_ALUA, VMW_PSP_RR
            hostMultipathInfoLogicalUnit.getPolicy().setPolicy("VMW_PSP_RR");
        }
    }

}
