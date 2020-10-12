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
import com.vmware.vim25.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class HostNetworkSystemMO extends BaseMO {
    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(HostNetworkSystemMO.class);

    public HostNetworkSystemMO(VmwareContext context, ManagedObjectReference morNetworkSystem) {
        super(context, morNetworkSystem);
    }

    public HostNetworkSystemMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public void addPortGroup(HostPortGroupSpec spec) throws Exception {
        _context.getService().addPortGroup(_mor, spec);
    }

    public void updatePortGroup(String portGroupName, HostPortGroupSpec spec) throws Exception {
        _context.getService().updatePortGroup(_mor, portGroupName, spec);
    }

    public void removePortGroup(String portGroupName) throws Exception {
        _context.getService().removePortGroup(_mor, portGroupName);
    }

    public void addVirtualSwitch(String vSwitchName, HostVirtualSwitchSpec spec) throws Exception {
        _context.getService().addVirtualSwitch(_mor, vSwitchName, spec);
    }

    public void updateVirtualSwitch(String vSwitchName, HostVirtualSwitchSpec spec) throws Exception {
        _context.getService().updateVirtualSwitch(_mor, vSwitchName, spec);
    }

    public void removeVirtualSwitch(String vSwitchName) throws Exception {
        _context.getService().removeVirtualSwitch(_mor, vSwitchName);
    }

    public void refresh() throws Exception {
        _context.getService().refreshNetworkSystem(_mor);
    }

    public void updateNetworkConfig(HostNetworkConfig config, String changeMode) throws Exception {
        _context.getService().updateNetworkConfig(_mor, config, changeMode);
    }
}
