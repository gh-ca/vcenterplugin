package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OptionValue;

import java.util.List;

public class HostAdvanceOptionMO extends BaseMO {
    public HostAdvanceOptionMO(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    public List<OptionValue> queryOptions(String name) throws Exception {
        return context.getService().queryOptions(mor, name);
    }

    public void updateOptions(List<OptionValue> optionValues) throws Exception {
        context.getService().updateOptions(mor, optionValues);
    }
}
