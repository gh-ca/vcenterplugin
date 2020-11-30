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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.DVPortgroupConfigSpec;
import com.vmware.vim25.DVSConfigInfo;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VMwareDVSConfigInfo;
import com.vmware.vim25.VMwareDVSConfigSpec;
import com.vmware.vim25.VMwareDVSPvlanMapEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributedVirtualSwitchMO extends BaseMO {
    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(DistributedVirtualSwitchMO.class);
    private static ConcurrentHashMap<String, List<String>> s_dvPortGroupCacheMap = null;

    public DistributedVirtualSwitchMO(VmwareContext context, ManagedObjectReference morDvs) {
        super(context, morDvs);
        s_dvPortGroupCacheMap = new ConcurrentHashMap<String, List<String>>();
    }

    public DistributedVirtualSwitchMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
        s_dvPortGroupCacheMap = new ConcurrentHashMap<>();
    }


    public TaskInfo updateVmWareDvSwitchGetTask(ManagedObjectReference dvSwitchMor, VMwareDVSConfigSpec dvsSpec) throws Exception {
        ManagedObjectReference task = context.getService().reconfigureDvsTask(dvSwitchMor, dvsSpec);
        TaskInfo info = (TaskInfo)(context.getVimClient().getDynamicProperty(task, "info"));
        context.getVimClient().waitForTask(task);
        return info;
    }

    public String getDvsConfigVersion(ManagedObjectReference dvSwitchMor) throws Exception {
        assert (dvSwitchMor != null);
        DVSConfigInfo dvsConfigInfo = context.getVimClient().getDynamicProperty(dvSwitchMor, "config");
        return dvsConfigInfo.getConfigVersion();
    }

    public Map<Integer, HypervisorHostHelper.PvlanType> retrieveVlanPvlan(int vlanid, int secondaryvlanid, ManagedObjectReference dvSwitchMor) throws Exception {
        assert (dvSwitchMor != null);

        Map<Integer, HypervisorHostHelper.PvlanType> result = new HashMap<Integer, HypervisorHostHelper.PvlanType>();

        VMwareDVSConfigInfo configinfo = context.getVimClient().getDynamicProperty(dvSwitchMor, "config");
        List<VMwareDVSPvlanMapEntry> pvlanconfig;
        pvlanconfig = configinfo.getPvlanConfig();

        if (null == pvlanconfig || 0 == pvlanconfig.size()) {
            return result;
        }
        for (VMwareDVSPvlanMapEntry mapEntry : pvlanconfig) {
            int entryVlanid = mapEntry.getPrimaryVlanId();
            int entryPvlanid = mapEntry.getSecondaryVlanId();
            if (entryVlanid == entryPvlanid) {
                if (vlanid == entryVlanid) {
                    result.put(vlanid, HypervisorHostHelper.PvlanType.valueOf(mapEntry.getPvlanType()));
                } else if ((vlanid != secondaryvlanid) && secondaryvlanid == entryVlanid) {
                    result.put(secondaryvlanid, HypervisorHostHelper.PvlanType.valueOf(mapEntry.getPvlanType()));
                }
            } else {
                if (vlanid == entryVlanid) {
                    result.put(vlanid, HypervisorHostHelper.PvlanType.promiscuous);
                } else if (vlanid == entryPvlanid) {
                    result.put(vlanid, HypervisorHostHelper.PvlanType.valueOf(mapEntry.getPvlanType()));
                }
                if ((vlanid != secondaryvlanid) && secondaryvlanid == entryVlanid) {
                    result.put(secondaryvlanid, HypervisorHostHelper.PvlanType.promiscuous);
                } else if (secondaryvlanid == entryPvlanid) {
                    result.put(secondaryvlanid, HypervisorHostHelper.PvlanType.valueOf(mapEntry.getPvlanType()));
                }

            }
            if (result.containsKey(vlanid) && result.get(vlanid) != HypervisorHostHelper.PvlanType.promiscuous) {
                return result;
            }

            if (result.containsKey(vlanid) && result.containsKey(secondaryvlanid)) {
                return result;
            }
        }
        return result;
    }

    public Pair<Integer, HypervisorHostHelper.PvlanType> retrieveVlanFromPvlan(int pvlanid, ManagedObjectReference dvSwitchMor) throws Exception {
        assert (dvSwitchMor != null);

        Pair<Integer, HypervisorHostHelper.PvlanType> result = null;

        VMwareDVSConfigInfo configinfo = (VMwareDVSConfigInfo) context.getVimClient().getDynamicProperty(dvSwitchMor, "config");
        List<VMwareDVSPvlanMapEntry> pvlanConfig = null;
        pvlanConfig = configinfo.getPvlanConfig();

        if (null == pvlanConfig || 0 == pvlanConfig.size()) {
            return result;
        }

        for (VMwareDVSPvlanMapEntry mapEntry : pvlanConfig) {
            int entryVlanid = mapEntry.getPrimaryVlanId();
            int entryPvlanid = mapEntry.getSecondaryVlanId();
            if (pvlanid == entryPvlanid) {
                result = new Pair<>(entryVlanid, HypervisorHostHelper.PvlanType.valueOf(mapEntry.getPvlanType()));
                break;
            }
        }
        return result;
    }
}
