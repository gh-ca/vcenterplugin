//Licensed to the Apache Software Foundation (ASF) under one
//or more contributor license agreements.  See the NOTICE file
//distributed with this work for additional information
//regarding copyright ownership.  The ASF licenses this file
//to you under the Apache License, Version 2.0 (the
//"License"); you may not use this file except in compliance
//with the License.  You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing,
//software distributed under the License is distributed on an
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//KIND, either express or implied.  See the License for the
//specific language governing permissions and limitations
//under the License.
package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.*;

import java.util.List;

public class LicenseAssignmentManagerMO extends BaseMO {

    private static final String LICENSE_INFO_FEATURE = "feature";

    public LicenseAssignmentManagerMO(VmwareContext context, ManagedObjectReference mor) {
        super(context, mor);
    }

    public LicenseAssignmentManagerLicenseAssignment getAssignedLicenseToHost(ManagedObjectReference hostMor) throws Exception {
        List<LicenseAssignmentManagerLicenseAssignment> licenses = context.getVimClient().getService().queryAssignedLicenses(mor, hostMor.getValue());
        return licenses.get(0);
    }

    public boolean isFeatureSupported(String featureKey, ManagedObjectReference hostMor) throws Exception {
        boolean featureSupported = false;
        List<KeyAnyValue> props = getHostLicenseProperties(hostMor);
        for (KeyAnyValue prop : props) {
            String key = prop.getKey();
            if (key.equalsIgnoreCase(LICENSE_INFO_FEATURE)) {
                KeyValue propValue = (KeyValue)prop.getValue();
                if (propValue.getKey().equalsIgnoreCase(featureKey)) {
                    featureSupported = true;
                    break;
                }
            }
        }

        return featureSupported;
    }

    public LicenseManagerLicenseInfo getHostLicenseInfo(ManagedObjectReference hostMor) throws Exception {
        LicenseAssignmentManagerLicenseAssignment license = getAssignedLicenseToHost(hostMor);
        return license.getAssignedLicense();
    }

    public List<KeyAnyValue> getHostLicenseProperties(ManagedObjectReference hostMor) throws Exception {
        return getHostLicenseInfo(hostMor).getProperties();
    }

    public String getHostLicenseName(ManagedObjectReference hostMor) throws Exception {
        return getHostLicenseInfo(hostMor).getName();
    }

}
