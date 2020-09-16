package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.BestPracticeCheckRecordBean;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName BestPracticeProcessService.java
 * @Description TODO
 * @createTime 2020年09月15日 16:35:00
 */
public interface BestPracticeProcessService {
    List<BestPracticeCheckRecordBean> getCheckRecord() throws Exception;

    void check(String hostName) throws Exception;

    void update(List<String> hostNames, String hostSetting) throws Exception;
}
