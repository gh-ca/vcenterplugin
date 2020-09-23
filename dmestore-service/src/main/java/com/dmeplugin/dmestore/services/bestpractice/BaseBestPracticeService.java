package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.util.VmwareContext;
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

    protected boolean check(VmwareContext context, String hostName,
                            String hostSetting, Object recommendValue) throws Exception {
        Object v = getCurrentValue(context, hostName, hostSetting);
        if (String.valueOf(v).equals(String.valueOf(recommendValue))) {
            return true;
        }

        return false;
    }

    protected void update(VmwareContext context, String hostName,
                          String hostSetting, Object recommendValue) throws Exception {
        HostMO hostMo = new HostMO(context, hostName);
        List<OptionValue> values = hostMo.getHostAdvanceOptionMO().queryOptions(hostSetting);
        for (OptionValue value : values) {
            value.setValue(recommendValue);
        }
        hostMo.getHostAdvanceOptionMO().UpdateOptions(values);
    }

    protected Object getCurrentValue(VmwareContext context,
                                     String hostName, String hostSetting) throws Exception {
        HostMO hostMo = new HostMO(context, hostName);
        List<OptionValue> values = hostMo.getHostAdvanceOptionMO().queryOptions(hostSetting);
        for (OptionValue value : values) {
            return value.getValue();
        }
        return null;
    }

    protected boolean checkModuleOption(VmwareContext context, String hostName,
                                        String optionName, Object recommendValue) throws Exception {
        String currentValue = getCurrentModuleOption(context, hostName, optionName);
        if (currentValue.equals(String.valueOf(recommendValue))) {
            return true;
        }
        return false;
    }

    protected String getCurrentModuleOption(VmwareContext context, String hostName, String optionName) throws Exception {
        HostMO hostMo = new HostMO(context, hostName);
        String modlueOption = hostMo.getHostKernelModuleSystemMO().queryConfiguredModuleOptionString(optionName);
        //拆分值
        String[] s = modlueOption.split("=");
        return s[1];
    }

    protected void updateModuleOption(VmwareContext context, String hostName,
                                      String optionName, Object recommendValue) throws Exception {
        HostMO hostMo = new HostMO(context, hostName);
        String v = optionName + "=" + String.valueOf(recommendValue);
        hostMo.getHostKernelModuleSystemMO().updateModuleOptionString(optionName, v);
    }
}
