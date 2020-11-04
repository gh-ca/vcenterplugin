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

public enum VmdkAdapterType {
    /**
     * ide
     **/
    ide,
    lsilogic,
    buslogic,
    none;

    public static VmdkAdapterType getAdapterType(DiskControllerType diskControllerType) {
        if (diskControllerType == DiskControllerType.ide) {
            return VmdkAdapterType.ide;
        } else if (diskControllerType == DiskControllerType.buslogic) {
            return VmdkAdapterType.buslogic;
        } else if (diskControllerType == DiskControllerType.lsilogic || diskControllerType == DiskControllerType.pvscsi || diskControllerType == DiskControllerType.lsisas1068) {
            return VmdkAdapterType.lsilogic;
        } else {
            return VmdkAdapterType.none;
        }
    }

    public static VmdkAdapterType getType(String vmdkAdapterType) {
        if ("ide".equalsIgnoreCase(vmdkAdapterType)) {
            return VmdkAdapterType.ide;
        } else if ("lsilogic".equalsIgnoreCase(vmdkAdapterType)) {
            return VmdkAdapterType.lsilogic;
        } else if ("buslogic".equalsIgnoreCase(vmdkAdapterType)) {
            return VmdkAdapterType.buslogic;
        } else {
            return VmdkAdapterType.none;
        }
    }
}
