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
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OptionValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class HostAdvanceOptionMO extends BaseMO {
    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(HostAdvanceOptionMO.class);

    public HostAdvanceOptionMO(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    public HostAdvanceOptionMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public List<OptionValue> queryOptions(String name) throws Exception {
        return _context.getService().queryOptions(_mor, name);
    }


    public void updateOptions(List<OptionValue> optionValues) throws Exception {
          _context.getService().updateOptions(_mor, optionValues);
    }
}
