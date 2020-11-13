package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.DMEServiceApplication;
import com.dmeplugin.dmestore.dao.SystemDao;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * SystemServiceImpl Tester.
 *
 * @author wangxiangyong
 * @version 1.0
 * @since <pre>十一月 11, 2020</pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DMEServiceApplication.class)
public class SystemServiceImplTest {
    @Autowired
    private SystemServiceImpl systemService;

    @Before
    public void before() throws Exception {
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
        assert true;
    }

    /**
     * Method: setSystemDao(SystemDao systemDao)
     */
    @Test
    public void testSetSystemDao() throws Exception {
      assert true;
    }

} 
