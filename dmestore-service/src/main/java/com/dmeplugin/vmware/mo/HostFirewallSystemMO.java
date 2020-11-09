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
import com.vmware.vim25.HostFirewallDefaultPolicy;
import com.vmware.vim25.HostFirewallInfo;
import com.vmware.vim25.ManagedObjectReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HostFirewallSystemMO extends BaseMO {
    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(HostFirewallSystemMO.class);

    public HostFirewallSystemMO(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    public HostFirewallSystemMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public HostFirewallInfo getFirewallInfo() throws Exception {
        return (HostFirewallInfo) context.getVimClient().getDynamicProperty(mor, "firewallInfo");
    }

    public void updateDefaultPolicy(HostFirewallDefaultPolicy policy) throws Exception {
        context.getService().updateDefaultPolicy(mor, policy);
    }

    public void enableRuleset(String rulesetName) throws Exception {
        context.getService().enableRuleset(mor, rulesetName);
    }

    public void disableRuleset(String rulesetName) throws Exception {
        context.getService().disableRuleset(mor, rulesetName);
    }

    public void refreshFirewall() throws Exception {
        context.getService().refreshFirewall(mor);
    }
}
