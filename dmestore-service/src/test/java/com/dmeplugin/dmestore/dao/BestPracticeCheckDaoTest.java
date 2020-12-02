package com.dmeplugin.dmestore.dao;

import static org.junit.Assert.*;

import com.dmeplugin.dmestore.DMEServiceApplication;
import com.dmeplugin.dmestore.model.BestPracticeBean;
import com.dmeplugin.dmestore.model.BestPracticeUpResultResponse;
import com.vmware.vim.binding.vmodl.list;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lianq
 * @className BestPracticeCheckDaoTest
 * @description TODO
 * @date 2020/12/2 20:09
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DMEServiceApplication.class)
public class BestPracticeCheckDaoTest {

    @Autowired
    private BestPracticeCheckDao bestPracticeCheckDao;

    @Test
    public void getDbFileName() {
        bestPracticeCheckDao.getDbFileName();
    }

    @Test
    public void setUrl() {
        bestPracticeCheckDao.setUrl("321");
    }

    @Test
    public void save() {
        List<BestPracticeBean> list = new ArrayList<>();
        BestPracticeBean bestPracticeBean = new BestPracticeBean();
        bestPracticeBean.setHostSetting("321");
        bestPracticeBean.setRecommendValue("321");
        bestPracticeBean.setLevel("321");
        bestPracticeBean.setActualValue("321");
        bestPracticeBean.setNeedReboot("321");
        bestPracticeBean.setHostObjectId("321");
        bestPracticeBean.setHostName("321");
        bestPracticeBean.setAutoRepair("321");
        list.add(bestPracticeBean);
        bestPracticeCheckDao.save(list);
    }

    @Test
    public void getHostNameByHostsetting() throws SQLException {
        System.out.println(bestPracticeCheckDao.getHostNameByHostsetting("321"));
    }

    @Test
    public void getAllHostIds() throws SQLException {
        System.out.println(bestPracticeCheckDao.getAllHostIds(1, 10));
    }

    @Test
    public void getByHostIds() throws SQLException {
        List<String> list = new ArrayList<>();
        list.add("321");
        bestPracticeCheckDao.getByHostIds(list);
    }

    @Test
    public void getRecordByPage() throws SQLException {
        bestPracticeCheckDao.getRecordByPage("321", 1, 10);
    }

    @Test
    public void getRecordBeanByHostsetting() throws SQLException {
        bestPracticeCheckDao.getRecordBeanByHostsetting("321");
    }

    @Test
    public void deleteBy() {
        List<BestPracticeUpResultResponse> list = new ArrayList<>();
        BestPracticeUpResultResponse bestPracticeUpResultResponse = new BestPracticeUpResultResponse();
        bestPracticeUpResultResponse.setHostObjectId("321");
        bestPracticeUpResultResponse.setHostName("321");
        list.add(bestPracticeUpResultResponse);
        bestPracticeCheckDao.deleteBy(list);
    }

    @Test
    public void deleteByHostNameAndHostsetting() {
        List<String> list = new ArrayList<>();
        list.add("321");
        bestPracticeCheckDao.deleteByHostNameAndHostsetting(list, "321");
    }

    @Test
    public void update() {
        List<BestPracticeBean> list = new ArrayList<>();
        BestPracticeBean bestPracticeBean = new BestPracticeBean();
        bestPracticeBean.setHostSetting("321");
        bestPracticeBean.setRecommendValue("321");
        bestPracticeBean.setLevel("321");
        bestPracticeBean.setActualValue("321");
        bestPracticeBean.setNeedReboot("321");
        bestPracticeBean.setHostObjectId("321");
        bestPracticeBean.setHostName("321");
        bestPracticeBean.setAutoRepair("321");
        list.add(bestPracticeBean);
        bestPracticeCheckDao.update(list);
    }
}