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

package com.dmeplugin.vmware.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestVmwareContextFactory {

    private static final Logger s_logger = LoggerFactory.getLogger(TestVmwareContextFactory.class);

    private static volatile int s_seq = 1;
    private static VmwareContextPool s_pool;

    static {
        // skip certificate check
        System.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
        s_pool = new VmwareContextPool();
    }

    public static VmwareContext create(String vCenterAddress, int vCenterPort, String vCenterUserName, String vCenterPassword) throws Exception {
        assert (vCenterAddress != null);
        assert (vCenterUserName != null);
        assert (vCenterPassword != null);

        String serviceUrl = "https://" + vCenterAddress + ":" + vCenterPort + "/sdk/vimService";

        if (s_logger.isDebugEnabled()) {
            s_logger.debug("initialize VmwareContext. url: " + serviceUrl + ", username: " + vCenterUserName + ", password: " +
                    vCenterPassword);
        }

        VmwareClient vimClient = new VmwareClient(vCenterAddress + "-" + s_seq++);
        vimClient.setVcenterSessionTimeout(1200000);
        vimClient.connect(serviceUrl, vCenterUserName, vCenterPassword);

        VmwareContext context = new VmwareContext(vimClient, vimClient.getServiceContent().getAbout().getInstanceUuid());
        return context;
    }

    public static VmwareContext getContext(String vCenterAddress, int vCenterPort, String vCenterUserName, String vCenterPassword) throws Exception {
        VmwareContext context = s_pool.getContext(vCenterAddress, vCenterUserName);
        if (context == null) {
            context = create(vCenterAddress, vCenterPort, vCenterUserName, vCenterPassword);
        }

        if (context != null) {
            context.setPoolInfo(s_pool, VmwareContextPool.composePoolKey(vCenterAddress, vCenterUserName));
            s_pool.registerContext(context);
        }

        return context;
    }
}
