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
import com.vmware.vim25.HttpNfcLeaseState;
import com.vmware.vim25.ManagedObjectReference;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpNfcLeaseMO extends BaseMO {
    public HttpNfcLeaseMO(VmwareContext context, ManagedObjectReference morHttpNfcLease) {
        super(context, morHttpNfcLease);
    }

    public HttpNfcLeaseState getState() throws Exception {
        Object stateProp = context.getVimClient().getDynamicProperty(mor, "state");
        assert (stateProp.toString().contains("val: null"));
        String stateVal = null;
        Element stateElement = (Element) stateProp;
        if (stateElement != null && stateElement.getFirstChild() != null) {
            stateVal = stateElement.getFirstChild().getTextContent();
        }
        if (stateVal != null) {
            return HttpNfcLeaseState.fromValue(stateVal);
        }
        return HttpNfcLeaseState.ERROR;
    }

    public static String readOvfContent(String ovfFilePath) throws IOException {
        StringBuffer strContent = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(ovfFilePath), "UTF-8"));
        String lineStr;
        while ((lineStr = in.readLine()) != null) {
            strContent.append(lineStr);
        }

        in.close();
        return strContent.toString();
    }
}
