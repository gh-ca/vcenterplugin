package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.PrivilegePolicyDef;

import java.util.List;

public class CustomFieldsManagerMO extends BaseMO {

    public CustomFieldsManagerMO(VmwareContext context, ManagedObjectReference mor) {
        super(context, mor);
    }

    public CustomFieldDef addCustomerFieldDef(String fieldName, String morType, PrivilegePolicyDef fieldDefPolicy,
        PrivilegePolicyDef fieldPolicy) throws Exception {
        return context.getService().addCustomFieldDef(getMor(), fieldName, morType, fieldDefPolicy, fieldPolicy);
    }

    public void setField(ManagedObjectReference morEntity, int key, String value) throws Exception {
        context.getService().setField(getMor(), morEntity, key, value);
    }

    public List<CustomFieldDef> getFields() throws Exception {
        return context.getVimClient().getDynamicProperty(getMor(), "field");
    }

    @Override
    public int getCustomFieldKey(String morType, String fieldName) throws Exception {
        List<CustomFieldDef> fields = getFields();
        if (fields != null) {
            for (CustomFieldDef field : fields) {
                if (field.getName().equals(fieldName) && field.getManagedObjectType().equals(morType)) {
                    return field.getKey();
                }
            }
        }
        return 0;
    }
}
