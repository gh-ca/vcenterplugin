package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.TaskDetailInfo;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @ClassName: TaskServiceImpl
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-08
 **/
public class TaskServiceImpl implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private DmeAccessService dmeAccessService;

    private final String LIST_TASK_URL = "/rest/taskmgmt/v1/tasks";
    private final String QUERY_TASK_URL = "/rest/taskmgmt/v1/tasks/{task_id}";

    Gson gson = new Gson();

    @Override
    public List<TaskDetailInfo> listTasks() throws Exception {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(LIST_TASK_URL, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.error("查询指定任务列表失败!错误信息:{}", responseEntity.getBody());
                return null;
            }
        } catch (Exception ex) {
            LOG.error("查询任务列表异常", ex);
            return null;
        }
        //解析responseEntity 转换为 TaskDetailInfo
        Object object = responseEntity.getBody();
        List<TaskDetailInfo> tasks = converBean(object);
        return tasks;
    }

    @Override
    public TaskDetailInfo queryTaskById(String taskId) throws Exception {
        String url = QUERY_TASK_URL.replace("{task_id}", taskId);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() / 100 != 2) {
                LOG.error("查询指定任务信息失败!错误信息:{}", responseEntity.getBody());
                return null;
            }
        } catch (Exception ex) {
            LOG.error("查询任务信息异常", ex);
            return null;
        }
        //解析responseEntity 转换为 TaskDetailInfo
        Object object = responseEntity.getBody();
        List<TaskDetailInfo> tasks = converBean(object);
        return tasks.get(0);
    }

    private List<TaskDetailInfo> converBean(Object object) {
        List<TaskDetailInfo> taskDetailInfos = new ArrayList<>();

        JsonArray jsonArray;
        //task列表 则先取任务列表
        String msg = gson.toJson(object);
        if (msg.indexOf("total") > -1 && msg.indexOf("tasks") > -1) {
            JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            jsonArray = jsonObject.get("tasks").getAsJsonArray();
        } else {
            jsonArray = new JsonParser().parse(object.toString()).getAsJsonArray();
        }
        for (JsonElement jsonElement : jsonArray) {
            TaskDetailInfo taskDetailInfo = new TaskDetailInfo();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String taskId = jsonObject.get("id").getAsString();
            String taskName = jsonObject.get("name_en").getAsString();
            int status = jsonObject.get("status").getAsInt();
            int progress = jsonObject.get("progress").getAsInt();
            String ownerName = jsonObject.get("owner_name").getAsString();
            long createTime = jsonObject.get("create_time").getAsLong();
            long startTime = jsonObject.get("start_time").getAsLong();
            long endTime = jsonObject.get("end_time").getAsLong();
            String detail = null != jsonObject.get("detail_en") ? "" : jsonObject.get("detail_en").getAsString();

            taskDetailInfo.setId(taskId);
            taskDetailInfo.setTaskName(taskName);
            taskDetailInfo.setStatus(status);
            taskDetailInfo.setProgress(progress);
            taskDetailInfo.setOwnerName(ownerName);
            taskDetailInfo.setStartTime(startTime);
            taskDetailInfo.setCreateTiem(createTime);
            taskDetailInfo.setEndTime(endTime);
            taskDetailInfo.setDetail(detail);

            taskDetailInfos.add(taskDetailInfo);
        }
        return taskDetailInfos;
    }

   /* public static void main(String[] args) {
        //String str = "[{\"id\":\"564537e8-295b-4cb6-8484-171ea552cb40\",\"name_en\":\"Createvolume\",\"name_cn\":\"创建卷\",\"description\":null,\"parent_id\":\"564537e8-295b-4cb6-8484-171ea552cb40\",\"seq_no\":1,\"status\":3,\"progress\":100,\"owner_name\":\"admin\",\"owner_id\":\"1\",\"create_time\":1580953613057,\"start_time\":1580953613091,\"end_time\":1580953615050,\"detail_en\":null,\"detail_cn\":null,\"resources\":[{\"operate\":\"MODIFY\",\"type\":\"Device\",\"id\":\"4e1db765-4882-11ea-95d0-00505691e086\",\"name\":\"Huawei.Storage\"}]}]";
        String str = "{\"total\":1,\"tasks\":[{\"id\":\"564537e8-295b-4cb6-8484-171ea552cb40\",\"name_en\":\"Createvolume\",\"name_cn\":\"创建卷\",\"description\":\"createavolume.\",\"parent_id\":\"564537e8-295b-4cb6-8484-171ea552cb40\",\"seq_no\":1,\"status\":3,\"progress\":100,\"owner_name\":\"admin\",\"owner_id\":\"1\",\"create_time\":1580953613057,\"start_time\":1580953613091,\"end_time\":1580953615050,\"detail_en\":null,\"detail_cn\":null,\"resources\":[]}]}";
        TaskServiceImpl taskService = new TaskServiceImpl();
        List<TaskDetailInfo> list = taskService.converBean(str);
        System.out.println((new Gson()).toJson(list));
    }*/
}
