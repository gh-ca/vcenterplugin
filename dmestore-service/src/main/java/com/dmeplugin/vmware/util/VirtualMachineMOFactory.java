package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.ClusterMO;
import com.dmeplugin.vmware.mo.VirtualMachineMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * @author lianq
 * @className VirtualMachineMOFactory
 * @description TODO
 * @date 2020/11/30 14:45
 */
public class VirtualMachineMOFactory {

    private static VirtualMachineMOFactory virtualMachineMOFactory;

    private VirtualMachineMOFactory() {
    }

    public static VirtualMachineMOFactory getInstance() {
        if (virtualMachineMOFactory == null) {
            synchronized (VirtualMachineMOFactory.class) {
                if (virtualMachineMOFactory == null) {
                    virtualMachineMOFactory = new VirtualMachineMOFactory();
                }
            }
        }
        return virtualMachineMOFactory;
    }

    public VirtualMachineMO build(VmwareContext context, ManagedObjectReference morVm) throws Exception {
        return new VirtualMachineMO(context, morVm);
    }
}
