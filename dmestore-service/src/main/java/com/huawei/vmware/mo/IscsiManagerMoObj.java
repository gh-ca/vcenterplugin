package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.IscsiPortInfo;
import com.vmware.vim25.ManagedObjectReference;

import java.util.List;

/**
 * IscsiManagerMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class IscsiManagerMoObj extends BaseMoObj {
    /**
     * IscsiManagerMO
     *
     * @param context context
     * @param morFirewallSystem morFirewallSystem
     */
    public IscsiManagerMoObj(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    /**
     * bindVnic
     *
     * @param isCsiHbaName isCsiHbaName
     * @param vnicDevice vnicDevice
     * @throws Exception Exception
     */
    public void bindVnic(String isCsiHbaName, String vnicDevice) throws Exception {
        context.getService().bindVnic(mor, isCsiHbaName, vnicDevice);
    }

    /**
     * queryBoundVnics
     *
     * @param isCsiHbaName isCsiHbaName
     * @return List
     * @throws Exception Exception
     */
    public List<IscsiPortInfo> queryBoundVnics(String isCsiHbaName) throws Exception {
        return context.getService().queryBoundVnics(mor, isCsiHbaName);
    }
}
