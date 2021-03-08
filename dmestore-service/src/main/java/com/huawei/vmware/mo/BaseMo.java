package com.huawei.vmware.mo;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.ManagedObjectReference;

/**
 * BaseMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class BaseMo {
    protected VmwareContext context;
    protected ManagedObjectReference mor;

    private String name;

    /**
     * BaseMO
     *
     * @param context context
     * @param mor mor
     */
    public BaseMo(VmwareContext context, ManagedObjectReference mor) {
        assert context != null;

        this.context = context;
        this.mor = mor;
    }

    /**
     * BaseMO
     *
     * @param context context
     * @param morType morType
     * @param morValue morValue
     */
    public BaseMo(VmwareContext context, String morType, String morValue) {
        assert context != null;
        assert morType != null;
        assert morValue != null;

        this.context = context;
        mor = new ManagedObjectReference();
        mor.setType(morType);
        mor.setValue(morValue);
    }

    public VmwareContext getContext() {
        return context;
    }

    /**
     * getMor
     *
     * @return ManagedObjectReference
     */
    public ManagedObjectReference getMor() {
        assert mor != null;
        return mor;
    }

    /**
     * getParentMor
     *
     * @return ManagedObjectReference
     * @throws Exception Exception
     */
    public ManagedObjectReference getParentMor() throws Exception {
        return (ManagedObjectReference) context.getVimClient().getDynamicProperty(mor, "parent");
    }

    /**
     * getName
     *
     * @return String
     * @throws Exception Exception
     */
    public String getName() throws Exception {
        if (name == null) {
            name = (String) context.getVimClient().getDynamicProperty(mor, "name");
        }

        return name;
    }

    /**
     * setCustomFieldValue
     *
     * @param fieldName fieldName
     * @param value value
     * @throws Exception Exception
     */
    public void setCustomFieldValue(String fieldName, String value) throws Exception {
        CustomFieldsManagerMo cfmMo = new CustomFieldsManagerMo(context,
            context.getServiceContent().getCustomFieldsManager());
        int key = getCustomFieldKey(fieldName);
        if (key == 0) {
            try {
                CustomFieldDef field = cfmMo.addCustomerFieldDef(fieldName, getMor().getType(), null, null);
                key = field.getKey();
            } catch (DmeException e) {
                key = getCustomFieldKey(fieldName);
            }
        }

        if (key == 0) {
            throw new Exception("Unable to setup custom field facility");
        }

        cfmMo.setField(getMor(), key, value);
    }

    /**
     * getCustomFieldKey
     *
     * @param fieldName fieldName
     * @return int
     * @throws Exception Exception
     */
    public int getCustomFieldKey(String fieldName) throws Exception {
        return getCustomFieldKey(getMor().getType(), fieldName);
    }

    /**
     * getCustomFieldKey
     *
     * @param morType morType
     * @param fieldName fieldName
     * @return int
     * @throws Exception Exception
     */
    public int getCustomFieldKey(String morType, String fieldName) throws Exception {
        assert morType != null;

        CustomFieldsManagerMo cfmMo = new CustomFieldsManagerMo(context,
            context.getServiceContent().getCustomFieldsManager());

        return cfmMo.getCustomFieldKey(morType, fieldName);
    }
}
