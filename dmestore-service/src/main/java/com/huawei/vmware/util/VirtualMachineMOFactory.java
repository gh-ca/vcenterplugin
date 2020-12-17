package com.huawei.vmware.util;

import com.huawei.vmware.mo.VirtualMachineMO;
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
     * @param morVm morVm
     * @return VirtualMachineMO
     */
    public VirtualMachineMO build(VmwareContext context, ManagedObjectReference morVm) {
        return new VirtualMachineMO(context, morVm);
    }
}
