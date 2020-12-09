package com.dmeplugin.dmestore.dao;

import com.dmeplugin.dmestore.exception.DataBaseException;
import com.dmeplugin.dmestore.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.sql.*;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

public class H2DataBaseDao {

    private String url;

    public final static Logger LOGGER = LoggerFactory.getLogger(H2DataBaseDao.class);

    private static final String VMWARE_RUNTIME_DATA_DIR = "VMWARE_RUNTIME_DATA_DIR";

    private static final String OS = System.getProperty("os.name").toLowerCase(Locale.US);

    private static final String URL_PREFIX = "jdbc:h2:";

    private static final String DB_FILE = "huawei-vcenter-plugin-data";

    private static final String DB_FILE_SUFFIX = ".mv.db";

    private static final String USER = "sa";

    private static String getVmwareRuntimeDataDir() {
        return System.getenv(VMWARE_RUNTIME_DATA_DIR);
    }

    private static Boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

    public Connection getConnection() throws DataBaseException {
        Connection con;

        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection(url, USER, "");
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }

        return con;
    }

    public void closeConnection(Connection con, ResultSet rs, PreparedStatement... ps) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }
        if (ps != null) {
            for (PreparedStatement ps1 : ps) {
                try {
                    if (ps1 != null) {
                        ps1.close();
                    }
                } catch (Exception var2) {
                    var2.printStackTrace();
                }
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }
    }

    public void closeConnection(Connection con, Statement ps, ResultSet rs) {
        close(rs);
        close(ps);
        close(con);
    }

    public void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public static String getDbFileName() {
        return DB_FILE + DB_FILE_SUFFIX;
    }

    public void setUrl(String url) {
        if (!StringUtils.hasText(this.url)) {
            if ("mac os x".equalsIgnoreCase(OS)) {
                this.url = "jdbc:h2:/Users/dmeuser/Downloads/huawei-vcenter-plugin-data";
                return;
            }
            String vmwareDataPath = getVmwareRuntimeDataDir();
            if (vmwareDataPath != null && !"".equals(vmwareDataPath)) {
                if (isWindows()) {
                    this.url = URL_PREFIX + "//" + vmwareDataPath + File.separator + DB_FILE;
                } else {
                    this.url = URL_PREFIX + FileUtils.getPath(true) + File.separator + DB_FILE;
                }
            } else {
                if (isWindows()) {
                    this.url = url + File.separator + DB_FILE;
                } else {
                    this.url = URL_PREFIX + FileUtils.getPath(true) + File.separator + DB_FILE;
                }
            }
        }
    }
}
