package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.TaskDetailInfo;

import java.util.List;

/**
 * @Description: TODO
 * @ClassName: TaskService
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-08
 **/
public interface TaskService {

    List<TaskDetailInfo> listTasks() throws Exception;

    TaskDetailInfo queryTaskById(String taskId) throws Exception;
}
