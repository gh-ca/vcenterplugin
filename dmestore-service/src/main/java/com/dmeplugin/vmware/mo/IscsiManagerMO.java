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
import com.vmware.vim25.IscsiMigrationDependency;
import com.vmware.vim25.IscsiPortInfo;
import com.vmware.vim25.IscsiStatus;
import com.vmware.vim25.ManagedObjectReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class IscsiManagerMO extends BaseMO {
    public IscsiManagerMO(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    public IscsiManagerMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public void bindVnic(String iScsiHbaName,String vnicDevice ) throws Exception {
          context.getService().bindVnic(mor, iScsiHbaName,vnicDevice);
    }


    public List<IscsiPortInfo> queryBoundVnics(String iScsiHbaName ) throws Exception {
         return context.getService().queryBoundVnics(mor, iScsiHbaName);
    }

    public List<IscsiPortInfo> queryCandidateNics(String iScsiHbaName ) throws Exception {
        return context.getService().queryCandidateNics(mor, iScsiHbaName);
    }

    public IscsiMigrationDependency queryMigrationDependencies(List<String> pnicDevice  ) throws Exception {
        return context.getService().queryMigrationDependencies(mor, pnicDevice );
    }

    public IscsiStatus queryPnicStatus(String pnicDevice ) throws Exception {
        return context.getService().queryPnicStatus(mor, pnicDevice);
    }

    public IscsiStatus queryVnicStatus(String vnicDevice  ) throws Exception {
        return context.getService().queryVnicStatus(mor, vnicDevice );
    }

    public void unbindVnic(String iScsiHbaName,String vnicDevice ,boolean force   ) throws Exception {
          context.getService().unbindVnic(mor, iScsiHbaName ,vnicDevice,force  );
    }
}
