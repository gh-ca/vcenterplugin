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

import com.vmware.vim25.ManagedObjectReference;

public class NetworkDetails {

    private String name;
    private ManagedObjectReference morNetwork;
    private ManagedObjectReference[] morVMsOnNetwork;
    private String gcTag;

    public NetworkDetails(String name, ManagedObjectReference morNetwork, ManagedObjectReference[] morVMsOnNetwork, String gcTag) {
        this.name = name;
        this.morNetwork = morNetwork;
        this.morVMsOnNetwork = morVMsOnNetwork;
        this.gcTag = gcTag;
    }

    public String getName() {
        return name;
    }

    public ManagedObjectReference getNetworkMor() {
        return morNetwork;
    }

    public ManagedObjectReference[] getVMMorsOnNetwork() {
        return morVMsOnNetwork;
    }

    public String getGCTag() {
        return gcTag;
    }
}
