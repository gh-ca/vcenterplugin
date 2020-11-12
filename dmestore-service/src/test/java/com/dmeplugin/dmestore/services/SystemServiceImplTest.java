package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.dao.SystemDao;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * SystemServiceImpl Tester.
 *
 * @author wangxiangyong
 * @version 1.0
 * @since <pre>十一月 11, 2020</pre>
 */
public class SystemServiceImplTest {
    private SystemServiceImpl systemService;

    @Before
    public void before() throws Exception {
        systemService = new SystemServiceImpl();
        SystemDao systemDao = new SystemDao();
        systemDao.setUrl("jdbc:h2://C:/ProgramData/VMware/vCenterServer/runtime/");
        systemService.setSystemDao(systemDao);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: initDb()
     */
    @Test
    public void testInitDb() throws Exception {
        systemService.initDb();
    }

    /**
     * Method: isTableExists(String tableName)
     */
    @Test
    public void testIsTableExists() throws Exception {
        boolean b = systemService.isTableExists("DP_DME_ACCESS_INFO");
        assert b;
    }

    /**
     * Method: isColumnExists(String tableName, String columnName)
     */
    @Test
    public void testIsColumnExists() throws Exception {
        boolean b = systemService.isColumnExists("DP_DME_ACCESS_INFO", "hostIp");
        assert b;
    }

    /**
     * Method: cleanData()
     */
    @Test
    public void testCleanData() throws Exception {
        return;
    }

    /**
     * Method: setSystemDao(SystemDao systemDao)
     */
    @Test
    public void testSetSystemDao() throws Exception {
        systemService.setSystemDao(null);
    }

    /**
     * Method: setvCenterInfoService(VCenterInfoService vCenterInfoService)
     */
    @Test
    public void testSetvCenterInfoService() throws Exception {
        systemService.setvCenterInfoService(null);
    }

} 
