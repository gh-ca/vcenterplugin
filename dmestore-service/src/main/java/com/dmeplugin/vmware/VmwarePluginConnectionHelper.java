package com.dmeplugin.vmware;

import com.dmeplugin.vmware.util.VmwareContext;
import com.dmeplugin.vmware.util.VmwarePluginContextFactory;

public class VmwarePluginConnectionHelper extends VCConnectionHelper {
    @Override
    public VmwareContext getServerContext(String serverguid) throws Exception {
        return VmwarePluginContextFactory
            .getServerContext(getUserSessionService(), getVimObjectReferenceService(), serverguid);
    }

    @Override
    public VmwareContext[] getAllContext() throws Exception {
        return VmwarePluginContextFactory.getAllContext(getUserSessionService(), getVimObjectReferenceService());
    }
}
