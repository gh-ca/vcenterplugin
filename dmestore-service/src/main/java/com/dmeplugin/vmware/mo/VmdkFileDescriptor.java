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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class VmdkFileDescriptor {
    private static final Logger s_logger = LoggerFactory.getLogger(VmdkFileDescriptor.class);
    private Properties properties = new Properties();
    private String baseFileName;

    public VmdkFileDescriptor() {
    }

    public void parse(byte[] vmdkFileContent) throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(vmdkFileContent), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.charAt(0) == '#') {
                    continue;
                }

                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    String name = tokens[0].trim();
                    String value = tokens[1].trim();
                    if (value.charAt(0) == '\"') {
                        value = value.substring(1, value.length() - 1);
                    }

                    properties.put(name, value);
                } else {
                    if (line.startsWith("RW")) {
                        int startPos = line.indexOf('\"');
                        int endPos = line.lastIndexOf('\"');
                        assert (startPos > 0);
                        assert (endPos > 0);

                        baseFileName = line.substring(startPos + 1, endPos);
                    } else {
                        s_logger.warn("Unrecognized vmdk line content: " + line);
                    }
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public String getBaseFileName() {
        return baseFileName;
    }

    public String getParentFileName() {
        return properties.getProperty("parentFileNameHint");
    }
}
