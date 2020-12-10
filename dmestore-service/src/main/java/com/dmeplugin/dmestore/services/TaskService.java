package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DmeException;
import com.dmeplugin.dmestore.model.TaskDetailInfo;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * TaskService
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public interface TaskService {
    List<TaskDetailInfo> listTasks();

    /**
     * get task by id
     *
     * @param taskId taskId
     * @return TaskDetailInfo TaskDetailInfo
     **/
    TaskDetailInfo queryTaskById(String taskId);

    /**
     * get task status
     *
     * @param taskIds taskIds
     * @param taskStatusMap taskStatusMap
     * @param timeout timeout
     * @param startTime startTime
     **/
    void getTaskStatus(List<String> taskIds, Map<String, Integer> taskStatusMap, int timeout, long startTime);

    /**
     * task status check
     *
     * @param taskIds taskIds
     * @return Boolean
     **/
    Boolean checkTaskStatus(List<String> taskIds);

    /**
     * get task info untill finish
     *
     * @param taskId taskId
     * @return JsonObject
     * @throws DmeException DmeException
     **/
    JsonObject queryTaskByIdUntilFinish(String taskId) throws DmeException;
}
