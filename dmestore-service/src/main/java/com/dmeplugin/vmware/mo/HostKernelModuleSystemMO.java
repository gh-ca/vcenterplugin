package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;

/**
 * HostKernelModuleSystemMO
 *
 * @author Administrator
 * @since 2020-12-02
 */
public class HostKernelModuleSystemMO extends BaseMO {
    public HostKernelModuleSystemMO(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    public String queryConfiguredModuleOptionString(String name) throws Exception {
        return context.getService().queryConfiguredModuleOptionString(mor, name);
    }

    public void updateModuleOptionString(String name, String options) throws Exception {
        context.getService().updateModuleOptionString(mor, name, options);
    }
}
