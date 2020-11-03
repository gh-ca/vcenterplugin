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

public class DatastoreFile {
    private String path;

    public DatastoreFile(String path) {
        assert (path != null);
        this.path = path;
    }

    public DatastoreFile(String datastoreName, String pathWithoutDatastoreName) {
        path = String.format("[%s] %s", datastoreName, pathWithoutDatastoreName);
    }

    public DatastoreFile(String datastoreName, String dir, String fileName) {
        if (dir == null || dir.isEmpty()) {
            path = String.format("[%s] %s", datastoreName, fileName);
        } else {
            path = String.format("[%s] %s/%s", datastoreName, dir, fileName);
        }
    }

    public String getDatastoreName() {
        return getDatastoreNameFromPath(path);
    }

    public String getPath() {
        return path;
    }

    public String getRelativePath() {
        int pos = path.indexOf(']');
        if (pos < 0) {
            pos = 0;
        } else {
            pos++;
        }

        return path.substring(pos).trim();
    }

    public String getDir() {
        int startPos = path.indexOf("]");
        if (startPos < 0) {
            startPos = 0;
        }

        int endPos = path.lastIndexOf('/');
        if (endPos < 0) {
            endPos = 0;
        }

        if (endPos > startPos) {
            return path.substring(startPos + 1, endPos).trim();
        }

        return "";
    }

    public String getFileName() {
        int startPos = path.indexOf("]");
        if (startPos < 0) {
            startPos = 0;
        } else {
            startPos++;
        }

        int endPos = path.lastIndexOf('/');
        if (endPos < 0) {
            return path.substring(startPos).trim();
        } else {
            return path.substring(endPos + 1);
        }
    }

    public String getFileBaseName() {
        String name = getFileName();
        int endPos = name.lastIndexOf('.');
        if (endPos < 0) {
            return name;
        }
        return name.substring(0, endPos);
    }

    public String getFileExtName() {
        String name = getFileName();
        int endPos = name.lastIndexOf('.');
        if (endPos < 0) {
            return "";
        }

        return name.substring(endPos);
    }

    public String getCompanionPath(String companionFileName) {
        return getCompanionDatastorePath(path, companionFileName);
    }

    public static boolean isFullDatastorePath(String path) {
        return path.matches("^\\[.*\\].*");
    }

    public static String getDatastoreNameFromPath(String path) {
        if (isFullDatastorePath(path)) {
            int endPos = path.indexOf("]");
            return path.substring(1, endPos).trim();
        }
        return null;
    }

    public static String getCompanionDatastorePath(String path, String companionFileName) {
        if (isFullDatastorePath(path)) {
            int endPos = path.indexOf("]");
            String dsName = path.substring(1, endPos);
            String dsRelativePath = path.substring(endPos + 1).trim();

            int fileNamePos = dsRelativePath.lastIndexOf('/');
            if (fileNamePos < 0) {
                return String.format("[%s] %s", dsName, companionFileName);
            } else {
                return String.format("[%s] %s/%s", dsName, dsRelativePath.substring(0, fileNamePos), companionFileName);
            }
        }
        return companionFileName;
    }
}
