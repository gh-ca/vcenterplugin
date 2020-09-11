package com.dmeplugin.vmware;

import com.dmeplugin.vmware.util.VmwareContext;
import com.dmeplugin.vmware.util.VmwarePluginContextFactory;
import com.vmware.vim25.ManagedObjectReference;

public class VmwarePluginConnectionHelper extends VCConnectionHelper{
    @Override
    public VmwareContext getServerContext(ManagedObjectReference mor) throws Exception {
        return VmwarePluginContextFactory.getServerContext(getUserSessionService(),getVimObjectReferenceService(),mor);
    }

    @Override
    public VmwareContext[] getAllContext() throws Exception {
        return VmwarePluginContextFactory.getAllContext(getUserSessionService(),getVimObjectReferenceService());
    }
}
