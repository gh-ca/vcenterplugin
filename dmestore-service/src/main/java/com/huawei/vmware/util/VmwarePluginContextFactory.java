//Licensed to the Apache Software Foundation (ASF) under one
//or more contributor license agreements.  See the NOTICE file
//distributed with this work for additional information
//regarding copyright ownership.  The ASF licenses this file
//to you under the Apache License, Version 2.0 (the
//"License"); you may not use this file except in compliance
//with the License.  You may obtain copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing,
//software distributed under the License is distributed on an
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//KIND, either express or implied.  See the License for the
//specific language governing permissions and limitations
//under the License.

package com.huawei.vmware.util;


import com.vmware.vise.usersession.ServerInfo;
import com.vmware.vise.usersession.UserSessionService;
import com.vmware.vise.vim.data.VimObjectReferenceService;

import java.util.ArrayList;
import java.util.List;

/**
 * VmwarePluginContextFactory
 *
 * @author Administrator 
 * @since 2020-12-10
 */
public class VmwarePluginContextFactory {
    private static VmwareContextPool pool;
    private static final int TIMEOUT = 1200000;

    static {
        System.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
        pool = new VmwareContextPool();
    }

    private VmwarePluginContextFactory() { }

    private static VmwareContext create(UserSessionService userSessionService,
                                        VimObjectReferenceService vimObjectReferenceService,
                                        String serverguid) throws Exception {
        VmwareContext context = null;
        ServerInfo[] serverInfoList = userSessionService.getUserSession().serversInfo;
        for (ServerInfo serverInfo : serverInfoList) {
            VmwareClient vimClient = new VmwareClient(serverInfo.serviceGuid);
            vimClient.setVcenterSessionTimeout(TIMEOUT);
            vimClient.connect(serverInfo);
            if (serverguid.equalsIgnoreCase(serverInfo.serviceGuid)) {
                context = new VmwareContext(vimClient, serverInfo.serviceGuid);
            }
        }
        return context;
    }

    /**
     * getServerContext
     *
     * @param userSessionService userSessionService
     * @param vimObjectReferenceService vimObjectReferenceService
     * @param serverguid serverguid
     * @return VmwareContext
     * @throws Exception Exception
     */
    public static VmwareContext getServerContext(UserSessionService userSessionService,
                                                 VimObjectReferenceService vimObjectReferenceService,
                                                 String serverguid) throws Exception {
        VmwareContext context = pool.getContext(serverguid, "");
        if (context == null) {
            context = create(userSessionService, vimObjectReferenceService, serverguid);
        }
        if (context != null) {
            context.setPoolInfo(pool, VmwareContextPool.composePoolKey(serverguid, ""));
            pool.registerContext(context);
        }
        return context;
    }

    /**
     * getAllContext
     *
     * @param userSessionService userSessionService
     * @param vimObjectReferenceService vimObjectReferenceService
     * @return VmwareContext[]
     * @throws Exception Exception
     */
    public static VmwareContext[] getAllContext(UserSessionService userSessionService,
                                                VimObjectReferenceService vimObjectReferenceService) throws Exception {
        ServerInfo[] serverInfoList = userSessionService.getUserSession().serversInfo;
        List<VmwareContext> vmwareContexts = new ArrayList<>();
        for (ServerInfo serverInfo : serverInfoList) {
            vmwareContexts.add(getServerContext(userSessionService, vimObjectReferenceService, serverInfo.serviceGuid));
        }
        return vmwareContexts.toArray(new VmwareContext[0]);
    }

    /**
     * closeAll
     */
    public static void closeAll() {
        pool.closeAll();
    }
}
