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


import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vise.usersession.ServerInfo;
import com.vmware.vise.usersession.UserSessionService;
import com.vmware.vise.vim.data.VimObjectReferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class VmwarePluginContextFactory {



    private static final Logger s_logger = LoggerFactory.getLogger(VmwarePluginContextFactory.class);

    private static volatile int s_seq = 1;
    private static VmwareContextPool s_pool;

    static {
        // skip certificate check
        System.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
        s_pool = new VmwareContextPool();
    }

    private static VmwareContext create(UserSessionService userSessionService, VimObjectReferenceService vimObjectReferenceService, String serverguid) throws Exception {

        VmwareContext context=null;
        ServerInfo[] serverInfoList=userSessionService.getUserSession().serversInfo;

        for (ServerInfo serverInfo:serverInfoList){
            VmwareClient vimClient = new VmwareClient(serverInfo.serviceGuid);
            vimClient.setVcenterSessionTimeout(1200000);
            vimClient.connect(serverInfo);
            if (serverguid.equalsIgnoreCase(serverInfo.serviceGuid))
                context = new VmwareContext(vimClient, serverInfo.serviceGuid);
        }

        return context;
    }

    public static VmwareContext getServerContext(UserSessionService userSessionService, VimObjectReferenceService vimObjectReferenceService, String serverguid) throws Exception {
        VmwareContext context = s_pool.getContext(serverguid,"");
        if (context == null)
            context = create(userSessionService, vimObjectReferenceService, serverguid);

        if (context != null) {
            context.setPoolInfo(s_pool, VmwareContextPool.composePoolKey(serverguid, "vCenterUserName"));
            s_pool.registerContext(context);
        }

        return context;
    }

    public static VmwareContext getServerContext(UserSessionService userSessionService, VimObjectReferenceService vimObjectReferenceService, ManagedObjectReference mor) throws Exception {
        String serverguid=vimObjectReferenceService.getServerGuid(mor);

        return getServerContext(userSessionService,vimObjectReferenceService,serverguid);
    }

    public static VmwareContext[] getAllContext(UserSessionService userSessionService, VimObjectReferenceService vimObjectReferenceService) throws Exception {
        ServerInfo[] serverInfoList=userSessionService.getUserSession().serversInfo;
        List<VmwareContext> vmwareContexts=new ArrayList<>();


        for (ServerInfo serverInfo:serverInfoList){
            vmwareContexts.add(getServerContext(userSessionService,vimObjectReferenceService, serverInfo.serviceGuid));
        }



        return vmwareContexts.toArray(new VmwareContext[0]);
    }
}
