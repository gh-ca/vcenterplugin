package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;

/**
 * HostKernelModuleSystemMO
 *
 * @author Administrator
 * @since 2020-12-02
 */
public class HostKernelModuleSystemMo extends BaseMo {
    /**
     * HostKernelModuleSystemMO
     *
     * @param context context
     * @param morFirewallSystem morFirewallSystem
     */
    public HostKernelModuleSystemMo(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    /**
     * queryConfiguredModuleOptionString
     *
     * @param moduleName moduleName
     * @return String
     * @throws Exception Exception
     */
    public String queryConfiguredModuleOptionString(String moduleName) throws Exception {
        return context.getService().queryConfiguredModuleOptionString(mor, moduleName);
    }

    /**
     * updateModuleOptionString
     *
     * @param moduleName moduleName
     * @param options options
     * @throws Exception Exception
     */
    public void updateModuleOptionString(String moduleName, String options) throws Exception {
        context.getService().updateModuleOptionString(mor, moduleName, options);
    }
}
