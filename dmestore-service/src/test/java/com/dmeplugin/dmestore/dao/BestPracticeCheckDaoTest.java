package com.dmeplugin.dmestore.dao;

import static org.mockito.Mockito.mock;

import com.dmeplugin.dmestore.model.BestPracticeBean;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName BestPracticeCheckDaoTest.java
 * @Description TODO
 * @createTime 2020年12月01日 14:11:00
 */
public class BestPracticeCheckDaoTest {

    @InjectMocks
    private BestPracticeCheckDao bestPracticeCheckDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save() {
        List<BestPracticeBean> list = mock(ArrayList.class);
        BestPracticeBean bestPracticeBean = new BestPracticeBean();
        list.add(bestPracticeBean);
        bestPracticeCheckDao.save(list);
    }

    @Test
    public void getHostNameByHostsetting() {
    }

    @Test
    public void getAllHostIds() {
    }

    @Test
    public void getByHostIds() {
    }

    @Test
    public void getRecordByPage() {
    }

    @Test
    public void getRecordBeanByHostsetting() {
    }

    @Test
    public void deleteBy() {
    }

    @Test
    public void deleteByHostNameAndHostsetting() {
    }

    @Test
    public void update() {
    }
}