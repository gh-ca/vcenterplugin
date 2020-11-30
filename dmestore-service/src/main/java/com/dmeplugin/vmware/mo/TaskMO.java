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
import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TaskMO extends BaseMO {
    private static final Logger s_logger = LoggerFactory.getLogger(TaskMO.class);
    public TaskMO(VmwareContext context, ManagedObjectReference morTask) {
        super(context, morTask);
    }

    public static String getTaskFailureInfo(VmwareContext context, ManagedObjectReference morTask) {
        StringBuffer sb = new StringBuffer();

        try {
            TaskInfo info = context.getVimClient().getDynamicProperty(morTask, "info");
            if (info != null) {
                LocalizedMethodFault fault = info.getError();
                if (fault != null) {
                    sb.append(fault.getLocalizedMessage()).append(" ");
                    if (fault.getFault() != null) {
                        sb.append(fault.getFault().getClass().getName());
                    }
                }
            }
        } catch (Exception e) {
            s_logger.info("[ignored]"
                    + "error retrieving failure info for task : " + e.getLocalizedMessage());
        }

        return sb.toString();
    }
}
