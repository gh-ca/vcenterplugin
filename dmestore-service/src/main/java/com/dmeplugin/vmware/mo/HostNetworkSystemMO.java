package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.HostNetworkConfig;
import com.vmware.vim25.HostNetworkConfigResult;
import com.vmware.vim25.HostVirtualSwitchSpec;
import com.vmware.vim25.ManagedObjectReference;

public class HostNetworkSystemMO extends BaseMO {
    public HostNetworkSystemMO(VmwareContext context, ManagedObjectReference morNetworkSystem) {
        super(context, morNetworkSystem);
    }

    public HostNetworkConfig getNetworkConfig() throws Exception {
        return (HostNetworkConfig) context.getVimClient().getDynamicProperty(mor, "networkConfig");
    }

    public void updateVirtualSwitch(String switchName, HostVirtualSwitchSpec spec) throws Exception {
        context.getService().updateVirtualSwitch(mor, switchName, spec);
    }

    public HostNetworkConfigResult updateNetworkConfig(HostNetworkConfig config, String changeMode) throws Exception {
        return context.getService().updateNetworkConfig(mor, config, changeMode);
    }
}
