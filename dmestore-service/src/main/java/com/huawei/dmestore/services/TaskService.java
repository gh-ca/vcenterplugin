package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.TaskDetailInfo;
import com.google.gson.JsonObject;
import com.huawei.dmestore.model.TasksResultObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    void getTaskStatus(List<String> taskIds, Map<String, Integer> taskStatusMap, long timeout, long startTime);

    public Map<String, Integer> getTaskStatusWhile(Set<String> taskIds) ;

    /**
     * task status check
     *
     * @param taskIds taskIds
     * @return Boolean
     **/
    boolean checkTaskStatus(List<String> taskIds);

    /**
     * task status check
     *
     * @param taskIds taskIds
     * @return Boolean
     **/
    boolean checkTaskStatus(List<String> taskIds,long timeout);

    /**
     * checkTaskStatusLarge
     *
     * @param taskIds taskIds
     * @return Boolean
     **/
    boolean checkTaskStatusLarge(Set<String> taskIds,long timeout);

    /**
     * get task info untill finish
     *
     * @param taskId taskId
     * @return JsonObject
     * @throws DmeException DmeException
     **/
    JsonObject queryTaskByIdUntilFinish(String taskId) throws DmeException;
    /**
     * checkTaskStatusNew
     *
     * @param taskId timeout
     * @return TasksResultObject
     **/
    TasksResultObject checkTaskStatusNew(String taskId, long timeout);
}
