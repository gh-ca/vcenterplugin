package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.exception.DmeSqlException;
import com.dmeplugin.dmestore.model.TaskDetailInfo;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @ClassName: TaskService
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-08
 **/
public interface TaskService {

    List<TaskDetailInfo> listTasks() ;

    TaskDetailInfo queryTaskById(String taskId) ;

    void getTaskStatus(List<String> taskIds, Map<String, Integer> taskStatusMap, int timeout, long startTime);

    Boolean checkTaskStatus(List<String> taskIds);

    /**
     * @Author wangxiangyong
     * @Description 直到任务完成后返回任务详情
     * @Date 14:59 2020/9/9
     * @Param [taskId]
     * @Return com.google.gson.JsonObject
     **/
    JsonObject queryTaskByIdUntilFinish(String taskId) throws DMEException;
}
