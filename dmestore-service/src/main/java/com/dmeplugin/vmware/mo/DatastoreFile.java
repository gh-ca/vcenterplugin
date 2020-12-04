package com.dmeplugin.vmware.mo;

public class DatastoreFile {
    private String path;

    public DatastoreFile(String path) {
        assert (path != null);
        this.path = path;
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

    public void setPath(String path) {
       this.path = path;
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
