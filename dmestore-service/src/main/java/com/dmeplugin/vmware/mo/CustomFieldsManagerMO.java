// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.dmeplugin.vmware.mo;

import java.util.List;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.PrivilegePolicyDef;

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
