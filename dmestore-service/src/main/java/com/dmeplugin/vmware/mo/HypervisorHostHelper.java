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

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldStringValue;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ObjectContent;

import java.util.List;

public class HypervisorHostHelper {

    public static VirtualMachineMO findVmFromObjectContent(VmwareContext context, ObjectContent[] ocs, String name,
        String instanceNameCustomField) {
        if (ocs != null && ocs.length > 0) {
            for (ObjectContent oc : ocs) {
                String vmNameInvCenter = null;
                String vmInternalCsName = null;
                List<DynamicProperty> objProps = oc.getPropSet();
                if (objProps != null) {
                    for (DynamicProperty objProp : objProps) {
                        if ("name".equals(objProp.getName())) {
                            vmNameInvCenter = (String) objProp.getVal();
                        } else if (objProp.getName().contains(instanceNameCustomField)) {
                            if (objProp.getVal() != null) {
                                vmInternalCsName = ((CustomFieldStringValue) objProp.getVal()).getValue();
                            }
                        }

                        if ((vmNameInvCenter != null && name.equalsIgnoreCase(vmNameInvCenter)) || (
                            vmInternalCsName != null && name.equalsIgnoreCase(vmInternalCsName))) {
                            VirtualMachineMO vmMo = new VirtualMachineMO(context, oc.getObj());
                            return vmMo;
                        }
                    }
                }
            }
        }
        return null;
    }
}
