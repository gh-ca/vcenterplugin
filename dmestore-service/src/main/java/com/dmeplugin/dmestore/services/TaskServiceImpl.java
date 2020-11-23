package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.exception.DmeSqlException;
import com.dmeplugin.dmestore.model.TaskDetailInfo;
import com.dmeplugin.dmestore.model.TaskDetailResource;
import com.dmeplugin.dmestore.utils.ToolUtils;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.*;

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
    //轮询任务状态的超值时间
    private final int taskTimeOut = 10 * 60 * 1000;
    Gson gson = new Gson();

    @Override
    public List<TaskDetailInfo> listTasks()  {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(LIST_TASK_URL, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
                LOG.error("查询指定任务列表失败!错误信息:{}", responseEntity.getBody());
                return null;
            }
        } catch (Exception ex) {
            LOG.error("查询任务列表异常", ex);
            return null;
        }
        //解析responseEntity 转换为 TaskDetailInfo
        Object object = responseEntity.getBody();
        List<TaskDetailInfo> tasks = converBean(null, object);
        return tasks;
    }

    @Override
    public TaskDetailInfo queryTaskById(String taskId)  {
        String url = QUERY_TASK_URL.replace("{task_id}", taskId);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
                LOG.error("查询指定任务信息失败!错误信息:{}", responseEntity.getBody());
                return null;
            }
        } catch (Exception ex) {
            LOG.error("查询任务信息异常", ex);
            return null;
        }
        //解析responseEntity 转换为 TaskDetailInfo
        Object object = responseEntity.getBody();
        List<TaskDetailInfo> tasks = converBean(taskId, object);
        if (null != tasks && tasks.size() > 0) {
            return tasks.get(0);
        } else {
            return null;
        }
    }

    @Override
    public JsonObject queryTaskByIdUntilFinish(String taskId) throws DMEException {
        String url = QUERY_TASK_URL.replace("{task_id}", taskId);
        boolean loopFlag = true;
        int waitTime = 2 * 1000;
        int times = taskTimeOut / waitTime;
        while (loopFlag) {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == 200) {
                JsonArray taskArray = gson.fromJson(responseEntity.getBody(), JsonArray.class);
                for (int i = 0; i < taskArray.size(); i++) {
                    JsonObject taskDetail = taskArray.get(i).getAsJsonObject();
                    //只查主任务
                    if (taskDetail.get("id").getAsString().equals(taskId)) {
                        //任务进度完成100%或者任务状态不正常直接结束查询，否则等待2s后再尝试
                        if (taskDetail.get("progress").getAsInt() == 100 || taskDetail.get("status").getAsInt() > 2) {
                            return taskDetail;
                        } else {
                            try {
                                Thread.sleep(waitTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                throw new DMEException(e.getMessage());
                            }
                        }
                    }
                }
            } else {
                throw new DmeSqlException(responseEntity.getBody());
            }

            if((times--) < 0){
                throw new DmeSqlException("查询任务状态超时！");
            }
        }
        return null;
    }

    private List<TaskDetailInfo> converBean(String origonTaskId, Object object) {
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
            String taskId = ToolUtils.jsonToStr(jsonObject.get("id"));
            //过滤子任务
            if (!StringUtils.isEmpty(origonTaskId) && !origonTaskId.equals(taskId)) {
                continue;
            }
            String taskName = ToolUtils.jsonToStr(jsonObject.get("name_en"));
            int status = ToolUtils.jsonToInt(jsonObject.get("status"));
            int progress = ToolUtils.jsonToInt(jsonObject.get("progress"));
            String ownerName = ToolUtils.jsonToStr(jsonObject.get("owner_name"));
            long createTime = ToolUtils.getLong(jsonObject.get("create_time"));
            long startTime = ToolUtils.getLong(jsonObject.get("start_time"));
            long endTime = ToolUtils.getLong(jsonObject.get("end_time"));
            String detail = ToolUtils.jsonToStr(jsonObject.get("detail_en"));
            JsonArray resourcesArray = jsonObject.getAsJsonArray("resources");

            taskDetailInfo.setId(taskId);
            taskDetailInfo.setTaskName(taskName);
            taskDetailInfo.setStatus(status);
            taskDetailInfo.setProgress(progress);
            taskDetailInfo.setOwnerName(ownerName);
            taskDetailInfo.setStartTime(startTime);
            taskDetailInfo.setCreateTiem(createTime);
            taskDetailInfo.setEndTime(endTime);
            taskDetailInfo.setDetail(detail);

            if (null != resourcesArray) {
                List<TaskDetailResource> resourceList = new ArrayList<>();
                for (JsonElement jsonElement1 : resourcesArray) {
                    TaskDetailResource tdr = new TaskDetailResource();
                    JsonObject resourceObj = jsonElement1.getAsJsonObject();
                    String operate = ToolUtils.jsonToStr(resourceObj.get("operate"));
                    String type = ToolUtils.jsonToStr(resourceObj.get("type"));
                    String id = ToolUtils.jsonToStr(resourceObj.get("id"));
                    String name = ToolUtils.jsonToStr(resourceObj.get("name"));

                    tdr.setOperate(operate);
                    tdr.setType(type);
                    tdr.setId(id);
                    tdr.setName(name);
                    resourceList.add(tdr);
                }
                taskDetailInfo.setResources(resourceList);
            }
            taskDetailInfos.add(taskDetailInfo);
        }
        return taskDetailInfos;
    }

    /**
     * @param taskIds       待查询的任务id集合
     * @param taskStatusMap 结束任务对应的状态集合
     * @param timeout       任务查询超时时间 单位ms
     * @param startTime     任务查询开始时间 单位ms
     */
    @Override
    public void getTaskStatus(List<String> taskIds, Map<String, Integer> taskStatusMap, int timeout, long startTime) {
        //没传开始时间或开始时间小于格林威治启用时间,初始话为当前时间
        if (0 == startTime) {
            startTime = System.currentTimeMillis();
        }
        List<TaskDetailInfo> detailInfos = new ArrayList<>();
        Set<String> queryTaskIds = new HashSet<>();
        for (String taskId : taskIds) {
            try {
                TaskDetailInfo taskDetailInfo = queryTaskById(taskId);
                if(taskDetailInfo!=null) {
                    detailInfos.add(taskDetailInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (TaskDetailInfo taskInfo : detailInfos) {
            if(taskInfo!=null) {
                String taskId = taskInfo.getId();
                //过滤子任务
                if (!taskIds.contains(taskId)) {
                    continue;
                }
                int status = taskInfo.getStatus();
                int progress = taskInfo.getProgress();
                if (100 == progress || status > 2) {
                    taskStatusMap.put(taskId, status);
                } else {
                    queryTaskIds.add(taskId);
                }
            }
        }
        //判断是否超时
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - startTime;
        if (delta > timeout) {
            return;
        }
        //未超时 判断是否还有任务未结束
        if (queryTaskIds.size() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getTaskStatus(new ArrayList<>(queryTaskIds), taskStatusMap, timeout, startTime);
        } else {
            return;
        }
    }

    @Override
    public Boolean checkTaskStatus(List<String> taskIds) {
        boolean flag = false;
        if(null !=taskIds && taskIds.size() >0){
            Map<String, Integer> taskStatusMap = new HashMap<>(16);
            getTaskStatus(taskIds, taskStatusMap, taskTimeOut, System.currentTimeMillis());
            LOG.info("taskStatusMap===" + (taskStatusMap == null ? "null" : taskStatusMap.size()));
            for (Map.Entry<String, Integer> entry : taskStatusMap.entrySet()) {
                int status = entry.getValue();
                if (3 == status) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    /* public static void main(String[] args) {
        //String str = "[{\"id\":\"564537e8-295b-4cb6-8484-171ea552cb40\",\"name_en\":\"Createvolume\",\"name_cn\":\"创建卷\",\"description\":null,\"parent_id\":\"564537e8-295b-4cb6-8484-171ea552cb40\",\"seq_no\":1,\"status\":3,\"progress\":100,\"owner_name\":\"admin\",\"owner_id\":\"1\",\"create_time\":1580953613057,\"start_time\":1580953613091,\"end_time\":1580953615050,\"detail_en\":null,\"detail_cn\":null,\"resources\":[{\"operate\":\"MODIFY\",\"type\":\"Device\",\"id\":\"4e1db765-4882-11ea-95d0-00505691e086\",\"name\":\"Huawei.Storage\"}]}]";
        String str = "{\"total\":1,\"tasks\":[{\"id\":\"564537e8-295b-4cb6-8484-171ea552cb40\",\"name_en\":\"Createvolume\",\"name_cn\":\"创建卷\",\"description\":\"createavolume.\",\"parent_id\":\"564537e8-295b-4cb6-8484-171ea552cb40\",\"seq_no\":1,\"status\":3,\"progress\":100,\"owner_name\":\"admin\",\"owner_id\":\"1\",\"create_time\":1580953613057,\"start_time\":1580953613091,\"end_time\":1580953615050,\"detail_en\":null,\"detail_cn\":null,\"resources\":[]}]}";
        TaskServiceImpl taskService = new TaskServiceImpl();
        List<TaskDetailInfo> list = taskService.converBean(str);
        System.out.println((new Gson()).toJson(list));
    }*/
}
