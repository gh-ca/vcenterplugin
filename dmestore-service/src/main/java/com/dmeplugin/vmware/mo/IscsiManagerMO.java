package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.IscsiPortInfo;
import com.vmware.vim25.ManagedObjectReference;

import java.util.List;

public class IscsiManagerMO extends BaseMO {
    public IscsiManagerMO(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    public void bindVnic(String isCsiHbaName, String vnicDevice) throws Exception {
        context.getService().bindVnic(mor, isCsiHbaName, vnicDevice);
    }

    public List<IscsiPortInfo> queryBoundVnics(String isCsiHbaName) throws Exception {
        return context.getService().queryBoundVnics(mor, isCsiHbaName);
    }

}
