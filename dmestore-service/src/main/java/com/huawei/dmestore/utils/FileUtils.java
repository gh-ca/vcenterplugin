package com.huawei.dmestore.utils;


import com.huawei.dmestore.exception.VcenterRuntimeException;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntry.Builder;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static final String OS = System.getProperty("os.name").toLowerCase(Locale.US);

    private static final String VMWARE_LINUX60_DIR = "/home/vsphere-client/base";

    private static final String VMWARE_LINUX_PATH_SYS_PROP = "VMWARE_VCHA_SMALLFILES_DIR";

    private static final String VMWARE_WINDOWS_DIR = "C:/ProgramData/VMware/vCenterServer/runtime/base";

    private static final String VMWARE_LINUX60_DB_DIR = "/home/vsphere-client";

    public static final String BASE_FILE_NAME = "baseV3.txt";

    public static final String WORK_FILE_NAME = "workV3.txt";

    public static final String SALT_FILE_NAME = "saltV3.txt";

    private static String PATH = null;

    public static void setFilePermission(File file) throws IOException {
        if (isWindows()) {
            setWindowsFilePermission(file);
        } else {
            setLinuxFilePermission(file);
        }
    }

    public static void setWindowsFilePermission(File file) throws IOException {
        Path path = Paths.get(file.getCanonicalPath());

        // Read Acl
        AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);
        List<AclEntry> acl = view.getAcl();
        acl.clear();
        FileSystem fileSystem = path.getFileSystem();
        if (fileSystem != null) {
            UserPrincipalLookupService userPrincipalLookupService = fileSystem
                .getUserPrincipalLookupService();
            if (userPrincipalLookupService != null) {
                Collection<String> users = new LinkedHashSet<>();
                // current user
                users.add(System.getProperty("user.name"));
                // default flash user
                users.add("vsphere-client");
                // default h5 user
                users.add("vsphere-ui");
                for (String userName : users) {
                    try {
                        UserPrincipal user = userPrincipalLookupService
                            .lookupPrincipalByName(userName);
                        AclEntry ae = buildUserAclEntry(user);
                        if (ae != null) {
                            // insert before any DENY entries
                            acl.add(0, ae);
                        }
                    } catch (Exception e) {
                        LOGGER.warn("Cannot set file permission on user: " + userName);
                    }
                }
            }
        }
        view.setAcl(acl);
    }


    private static AclEntry buildUserAclEntry(UserPrincipal user) {
        try {
            return defaultPermissionAeBuilder().setPrincipal(user).build();
        } catch (Exception e) {
            LOGGER.warn("Cannot set AclEntry on " + user);
            return null;
        }
    }

    private static Builder defaultPermissionAeBuilder() {
        return AclEntry.newBuilder().setPermissions(EnumSet.of(
            AclEntryPermission.READ_NAMED_ATTRS,
            AclEntryPermission.WRITE_NAMED_ATTRS,
            AclEntryPermission.APPEND_DATA,
            AclEntryPermission.READ_ACL,
            AclEntryPermission.WRITE_OWNER,
            AclEntryPermission.DELETE_CHILD,
            AclEntryPermission.SYNCHRONIZE,
            AclEntryPermission.WRITE_DATA,
            AclEntryPermission.WRITE_ATTRIBUTES,
            AclEntryPermission.READ_DATA,
            AclEntryPermission.DELETE,
            AclEntryPermission.WRITE_ACL,
            AclEntryPermission.READ_ATTRIBUTES,
            AclEntryPermission.EXECUTE
        )).setType(AclEntryType.ALLOW);
    }

    public static void setLinuxFilePermission(File file) throws IOException {
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.remove(PosixFilePermission.OTHERS_READ);
        perms.remove(PosixFilePermission.OTHERS_WRITE);
        perms.remove(PosixFilePermission.OTHERS_EXECUTE);
        try {
            Path path = Paths.get(file.getCanonicalPath());
            Files.setPosixFilePermissions(path, perms);
        } catch (Exception e) {
            LOGGER.error("Change folder " + file.getName() + " permission failed.");
        }
    }

    public static Boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

    public static String getPath() {
        if (!StringUtils.hasText(PATH)) {
            PATH = getPath(false);
        }
        return PATH;
    }

    public static String getPath(boolean isDbPath) {
        if (isWindows()) {
            return VMWARE_WINDOWS_DIR;
        } else {
            String huaweiParentDir = System.getenv().get(VMWARE_LINUX_PATH_SYS_PROP);
            String linuxDir;
            if (StringUtils.hasText(huaweiParentDir)) {
                try {
                    Set<PosixFilePermission> posixFilePermissions = Files
                        .getPosixFilePermissions(Paths.get(huaweiParentDir));
                    if (posixFilePermissions.contains(PosixFilePermission.GROUP_READ)
                        && posixFilePermissions
                        .contains(PosixFilePermission.GROUP_WRITE)) {
                        linuxDir = huaweiParentDir + "/huawei";
                        createDir(linuxDir);
                    } else {
                        throw new VcenterRuntimeException("No appropriate group permission: " + huaweiParentDir);
                    }
                } catch (IOException e) {
                    LOGGER.error("Cannot get path permission");
                    throw new VcenterRuntimeException("Cannot get path permission");
                }
            } else {
                // 6.0 doesn't support H5, so return flash version path
                linuxDir = (isDbPath ? VMWARE_LINUX60_DB_DIR : VMWARE_LINUX60_DIR);
            }
            return linuxDir;
        }
    }

    // 创建目录
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        // 判断目录是否存在
        if (dir.exists()) {
            LOGGER.info("Do not to create folder. It does exist!");
            return false;
        }
        // 结尾是否以"/"结束
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        // 创建目标目录
        if (dir.mkdirs()) {
            LOGGER.info("Folder created!" + dir.getName());
            if (!isWindows()) {
                try {
                    LOGGER.info("Setting default permission on folder " + dir.getName());
                    Runtime.getRuntime().exec("setfacl -d -m group:users:rw " + destDirName);
                } catch (IOException e) {
                    LOGGER.error("Cannot set default permission on folder " + dir.getName());
                }
            }
            return true;
        } else {
            LOGGER.error("Failed to create folder: " + dir.getName());
            return false;
        }
    }

    public static String getOldDbFolder() {
        return VMWARE_LINUX60_DB_DIR;
    }

    public static String getOldFolder() {
        return VMWARE_LINUX60_DIR;
    }

    public static String getKey(String fileName) {
        File file = new File(getPath() + File.separator + fileName);
        createFile(file);
        String line = null;
        StringBuffer result = new StringBuffer();
        FileInputStream f = null;
        InputStreamReader in = null;
        BufferedReader br = null;
        try {
            f = new FileInputStream(file);
            in = new InputStreamReader(f, "utf-8");
            br = new BufferedReader(in);
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            if (result.length() < 1) {
                return null;
            }
            return result.toString();
        } catch (IOException e) {
            LOGGER.error("Failed to get key: " + fileName);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return null;
    }

    public static void saveKey(String key, String fileName) {
        File file = new File(getPath() + File.separator + fileName);
        createFile(file);
        FileOutputStream f = null;
        OutputStreamWriter out = null;
        BufferedWriter bw = null;
        try {
            f = new FileOutputStream(file, false);
            out = new OutputStreamWriter(f, "utf-8");
            bw = new BufferedWriter(out);
            bw.write(key);
        } catch (IOException e) {
            LOGGER.error("Cannot save key: " + fileName);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    private static void createFile(File file) {
        // 判断文件是否存在
        if (!file.exists()) {
            LOGGER.info("key file not exists, create it ...");
            try {
                createDir(getPath());
                boolean re = file.createNewFile();
                if (re) {
                    //设置权限
                    setFilePermission(file);
                } else {
                    LOGGER.info("create file failed");
                }
            } catch (IOException e) {
                LOGGER.error("Failed to create file " + file.getName());
            }
        }
    }

}
