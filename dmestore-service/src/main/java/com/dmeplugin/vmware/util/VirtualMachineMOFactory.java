package com.dmeplugin.vmware.util;

import com.dmeplugin.vmware.mo.VirtualMachineMO;
import com.vmware.vim25.ManagedObjectReference;

/**
 * VirtualMachineMOFactory
 *
 * @author lianq
 * @ClassName: VirtualMachineMOFactory
 * @since 2020-12-10
 */
public class VirtualMachineMOFactory {
    private static VirtualMachineMOFactory virtualMachineMOFactory;

    private VirtualMachineMOFactory() {
    }

    /**
     * VirtualMachineMOFactory\
     *
     * @return VirtualMachineMOFactory
     */
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

    /**
     * build
     *
     * @param context context
     * @param morVm   morVm
     * @return VirtualMachineMO
     * @throws Exception Exception
     */
    public VirtualMachineMO build(VmwareContext context, ManagedObjectReference morVm) throws Exception {
        return new VirtualMachineMO(context, morVm);
    }
}
