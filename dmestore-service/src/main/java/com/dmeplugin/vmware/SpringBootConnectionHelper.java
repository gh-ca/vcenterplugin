package com.dmeplugin.vmware;

import com.dmeplugin.vmware.util.TestVmwareContextFactory;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;

import java.util.ArrayList;
import java.util.List;

public class SpringBootConnectionHelper extends VCConnectionHelper{
    @Override
    public VmwareContext getServerContext(String serverguid) throws Exception {
        return TestVmwareContextFactory.getContext(getServerurl(),getUsername(),getPassword());
    }

    @Override
    public VmwareContext[] getAllContext() throws Exception {
        VmwareContext vmwareContext=TestVmwareContextFactory.getContext(getServerurl(),getUsername(),getPassword());
        List<VmwareContext> vmwareContextList=new ArrayList<>();
        vmwareContextList.add(vmwareContext);
        return vmwareContextList.toArray(new VmwareContext[0]);
    }
}
