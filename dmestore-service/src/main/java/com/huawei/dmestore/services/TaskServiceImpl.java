package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.model.TaskDetailInfo;
import com.huawei.dmestore.model.TaskDetailInfoNew;
import com.huawei.dmestore.model.TaskDetailResource;
import com.huawei.dmestore.model.TasksResultObject;
import com.huawei.dmestore.utils.ToolUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TaskServiceImpl
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public class TaskServiceImpl implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static final int TASK_SUCCESS = 3;

    private static final int TASK_FAILURE = 5;

    private static final int TASK_TIMEOUT = 6;

    private static final int TASK_FLAG_2 = 2;

    private static final int TASK_FINISH = 100;

    private static final int ONE_SECEND = 1000;

    private static final int TWO_SECEND = 2 * ONE_SECEND;

    private static final int HTTP_STATUS_200 = 200;

    private static final String ID_FIELD = "id";

    private DmeAccessService dmeAccessService;

    /**
     * 轮询任务状态的超值时间
     */
    private final long taskTimeOut = 10 * 60 * 1000;

    private Gson gson = new Gson();

    @Override
    public List<TaskDetailInfo> listTasks() {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(DmeConstants.DME_TASK_BASE_URL, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
                LOG.error("get task list exception:{}", responseEntity.getBody());
                return Collections.EMPTY_LIST;
            }
        } catch (DmeException ex) {
            LOG.error("get task list error:{}", ex);
            return Collections.EMPTY_LIST;
        }

        // 解析responseEntity 转换为 TaskDetailInfo
        Object object = responseEntity.getBody();
        List<TaskDetailInfo> tasks = converBean(null, object);
        return tasks;
    }

    @Override
    public TaskDetailInfo queryTaskById(String taskId) {
        String url = DmeConstants.QUERY_TASK_URL.replace("{task_id}", taskId);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
                LOG.error("queryTaskById failed!taskId={},errorMsg:{}", taskId, responseEntity.getBody());
                return null;
            }
           // LOG.info("task="+url+"------resutl="+responseEntity.getBody());


        // 解析responseEntity 转换为 TaskDetailInfo
        Object object = responseEntity.getBody();
        List<TaskDetailInfo> tasks = converBean(taskId, object);
        if (tasks != null && tasks.size() > 0) {
            return tasks.get(0);
        } else {
            return null;
        }
        } catch (DmeException ex) {
            LOG.error("queryTaskById error, errorMsg:{}", ex.getMessage());
            return null;
        }
    }

    @Override
    public JsonObject queryTaskByIdUntilFinish(String taskId) throws DmeException {
        String url = DmeConstants.QUERY_TASK_URL.replace("{task_id}", taskId);
        boolean isLoop = true;
        int waitTime = TWO_SECEND;
        long times = taskTimeOut / waitTime;
        while (isLoop) {
            ResponseEntity<String> responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() == HTTP_STATUS_200) {
                JsonArray taskArray = gson.fromJson(responseEntity.getBody(), JsonArray.class);
                for (int index = 0; index < taskArray.size(); index++) {
                    JsonObject taskDetail = taskArray.get(index).getAsJsonObject();

                    // 只查主任务
                    if (taskDetail.get(ID_FIELD).getAsString().equals(taskId)) {
                        // 任务进度完成100%或者任务状态不正常直接结束查询，否则等待2s后再尝试
                        if (taskDetail.get("progress").getAsInt() == TASK_FINISH
                                || taskDetail.get("status").getAsInt() > TASK_FLAG_2) {
                            return taskDetail;
                        } else {
                            try {
                                Thread.sleep(waitTime);
                            } catch (InterruptedException e) {
                                throw new DmeException(e.getMessage());
                            }
                        }
                    }
                }
            } else {
                throw new DmeSqlException(responseEntity.getBody());
            }

            if ((times--) < 0) {
                throw new DmeSqlException("query task status timeout！taskId=" + taskId);
            }
        }
        return null;
    }

    private List<TaskDetailInfo> converBean(String origonTaskId, Object object) {
        List<TaskDetailInfo> taskDetailInfos = new ArrayList<>();

        JsonArray jsonArray;

        // task列表 则先取任务列表
        String msg = gson.toJson(object);
        if (msg.contains("total") && msg.contains("tasks")) {
            JsonObject jsonObject = new JsonParser().parse(object.toString()).getAsJsonObject();
            jsonArray = jsonObject.get("tasks").getAsJsonArray();
        } else {
            jsonArray = new JsonParser().parse(object.toString()).getAsJsonArray();
        }
        for (JsonElement jsonElement : jsonArray) {
            TaskDetailInfo taskDetailInfo = new TaskDetailInfo();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String taskId = ToolUtils.jsonToStr(jsonObject.get(ID_FIELD));

            // 过滤子任务
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
            taskDetailInfo.setId(taskId);
            taskDetailInfo.setTaskName(taskName);
            taskDetailInfo.setStatus(status);
            taskDetailInfo.setProgress(progress);
            taskDetailInfo.setOwnerName(ownerName);
            taskDetailInfo.setStartTime(startTime);
            taskDetailInfo.setCreateTiem(createTime);
            taskDetailInfo.setEndTime(endTime);
            taskDetailInfo.setDetail(detail);

            JsonArray resourcesArray = jsonObject.getAsJsonArray("resources");
            if (resourcesArray != null) {
                List<TaskDetailResource> resourceList = getTaskDetailResources(resourcesArray);
                taskDetailInfo.setResources(resourceList);
            }
            taskDetailInfos.add(taskDetailInfo);
        }
        return taskDetailInfos;
    }

    private List<TaskDetailResource> getTaskDetailResources(JsonArray resourcesArray) {
        List<TaskDetailResource> resourceList = new ArrayList<>();
        for (JsonElement jsonElement1 : resourcesArray) {
            TaskDetailResource tdr = new TaskDetailResource();
            JsonObject resourceObj = jsonElement1.getAsJsonObject();
            String operate = ToolUtils.jsonToStr(resourceObj.get("operate"));
            String type = ToolUtils.jsonToStr(resourceObj.get("type"));
            String id = ToolUtils.jsonToStr(resourceObj.get(ID_FIELD));
            String name = ToolUtils.jsonToStr(resourceObj.get("name"));

            tdr.setOperate(operate);
            tdr.setType(type);
            tdr.setId(id);
            tdr.setName(name);
            resourceList.add(tdr);
        }
        return resourceList;
    }

    /**
     * getTaskStatus
     *
     * @param taskIds 待查询的任务id集合
     */
    @Override
    public Map<String, Integer> getTaskStatusWhile(Set<String> taskIds) {
        Map<String, Integer> taskStatusMap = new HashMap<>();
        List<TaskDetailInfo> detailInfos = new ArrayList<>();
        //Set<String> queryTaskIds = new HashSet<>();
        for (String taskId : taskIds) {
            TaskDetailInfo taskDetailInfo = queryTaskById(taskId);
            if (taskDetailInfo != null) {
                detailInfos.add(taskDetailInfo);
            }
        }
        for (TaskDetailInfo taskInfo : detailInfos) {
            if (taskInfo != null) {
                String taskId = taskInfo.getId();

                // 过滤子任务
                if (!taskIds.contains(taskId)) {
                    continue;
                }
                int status = taskInfo.getStatus();
                int progress = taskInfo.getProgress();

                taskStatusMap.put(taskId, status);

            }
        }



        return taskStatusMap;
    }

    /**
     * getTaskStatus
     *
     * @param taskIds 待查询的任务id集合
     * @param taskStatusMap 结束任务对应的状态集合
     * @param timeout 任务查询超时时间 单位ms
     * @param startTime 任务查询开始时间 单位ms
     */
    @Override
    public void getTaskStatus(List<String> taskIds, Map<String, Integer> taskStatusMap, long timeout, long startTime) {
        long start = startTime;

        // 没传开始时间或开始时间小于格林威治启用时间,初始话为当前时间
        if (0 == startTime) {
            start = System.currentTimeMillis();
        }
        List<TaskDetailInfo> detailInfos = new ArrayList<>();
        Set<String> queryTaskIds = new HashSet<>();
        for (String taskId : taskIds) {
            TaskDetailInfo taskDetailInfo = queryTaskById(taskId);
            if (taskDetailInfo != null) {
                detailInfos.add(taskDetailInfo);
            }
        }
        for (TaskDetailInfo taskInfo : detailInfos) {
            if (taskInfo != null) {
                String taskId = taskInfo.getId();

                // 过滤子任务
                if (!taskIds.contains(taskId)) {
                    continue;
                }
                int status = taskInfo.getStatus();
                int progress = taskInfo.getProgress();
                //由于判断progress会导致，progress是100，但是statu还没有正常反应过来，还是2的情况，故去掉progress判断
                if ( status > TASK_FLAG_2) {
                    taskStatusMap.put(taskId, status);
                } else {
                    queryTaskIds.add(taskId);
                }
            }
        }

        // 判断是否超时
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - start;
        if (delta > timeout) {
            LOG.info("query task timeout!taskIds={}", gson.toJson(taskIds));
            return;
        }

        // 未超时 判断是否还有任务未结束
        if (queryTaskIds.size() > 0) {
            try {
                Thread.sleep(ONE_SECEND);
            } catch (InterruptedException e) {
                LOG.info("===wait one secend error==={}", e.getMessage());
            }
            getTaskStatus(new ArrayList<>(queryTaskIds), taskStatusMap, timeout, start);
        } else {
            return;
        }
    }

    @Override
    public boolean checkTaskStatus(List<String> taskIds) {

        return checkTaskStatus(taskIds,taskTimeOut);
    }

    @Override
    public boolean checkTaskStatus(List<String> taskIds,long timeout) {
        boolean isSuccess = false;
        if (taskIds != null && taskIds.size() > 0) {
            Map<String, Integer> taskStatusMap = new HashMap<>();
            getTaskStatus(taskIds, taskStatusMap, timeout, System.currentTimeMillis());
            for (Map.Entry<String, Integer> entry : taskStatusMap.entrySet()) {
                int status = entry.getValue();
                if (status == TASK_SUCCESS) {
                    isSuccess = true;
                    break;
                }
            }
        }
        return isSuccess;
    }

    @Override
    public boolean checkTaskStatusLarge(Set<String> taskIds,long timeout) {
        LOG.info("checking taskid="+gson.toJson(taskIds));
        boolean isSuccess = false;
        long currentmilitions=System.currentTimeMillis();
        if (taskIds != null && taskIds.size() > 0) {
            Map<String, Integer> taskStatusMap=new HashMap<>();
            Set<String> remaintasks = new HashSet<>(taskIds);
            do {
                long inccurrentmilitions=System.currentTimeMillis();
                if (inccurrentmilitions-currentmilitions>timeout)
                break;

                // 未超时 判断是否还有任务未结束
                if (remaintasks.size()>0) {
                    try {
                        //Thread.sleep(ONE_SECEND);
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        LOG.info("===wait one secend error==={}", e.getMessage());
                    }
                } else {
                    break;
                }
                try {
                    LOG.info("remain task size="+remaintasks.size());
                    Map<String, Integer> taskmapping=getTaskStatusWhile(remaintasks);
                    LOG.info("checking taskmapping="+gson.toJson(taskmapping));
                    //LOG.info("checking taskStatusMap="+gson.toJson(taskStatusMap));
                    //taskStatusMap.putAll(taskmapping);
                    for (Map.Entry<String, Integer> entry : taskmapping.entrySet()) {
                        int status = entry.getValue();
                        if (status >TASK_FLAG_2) {
                            taskStatusMap.put(entry.getKey(),entry.getValue());
                            remaintasks.remove(entry.getKey());
                        }
                    }

                }catch (Exception e){
                    LOG.error("wait task falut=",e);
                }
            } while (true);
            for (Map.Entry<String, Integer> entry : taskStatusMap.entrySet()) {
                int status=entry.getValue();
                if (status==TASK_SUCCESS){
                    isSuccess=true;
                }else if (status==TASK_FAILURE||status==TASK_TIMEOUT){
                    return false;
                }
            }
        }
        else
        {
            //无任务，或者传入任务为空，则直接返回成功
            return true;
        }
        return isSuccess;
    }

    public void setDmeAccessService(DmeAccessService dmeAccessService) {
        this.dmeAccessService = dmeAccessService;
    }

    public DmeAccessService getDmeAccessService() {
        return dmeAccessService;
    }


    /**
      * @Description: 新方法-检查任务状态
      * @Param taskId,timeout
      * @return TasksResultObject
      * @author yc
      * @Date 2021/5/21 15:38
     */
    @Override
    public TasksResultObject checkTaskStatusNew(String taskId, long timeout)  {
        //首先进行参数判断
        if(StringUtils.isEmpty(taskId)){
            return new TasksResultObject(true);
        }
        //设置方法的默认超时时间为3分钟
        long overTime  = 3*60*1000;
        if (0!= timeout){
            overTime = timeout;
        }
        long currentmilitions=System.currentTimeMillis();
        TasksResultObject result = new TasksResultObject();
        do{
            try {
                //程序每次进入等待2秒
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                LOG.info("===wait one secend error==={}", e.getMessage());
            }
            //首先根据任务号查询任务状态
            result = queryTaskByIdNew(taskId);
            if (StringUtils.isEmpty(result)){
                return null;
            }else if (result.isStatus()){
                return result;
            }
        }while (System.currentTimeMillis()-overTime < currentmilitions);
        return result;
    }

    /**
      * @Description: 根据任务号查询任务结果，并且分组统计
      * @Param @param null
      * @return @return
      * @throws
      * @author yc
      * @Date 2021/5/21 15:16
     */
    private TasksResultObject queryTaskByIdNew(String taskId) {
        String url = DmeConstants.QUERY_TASK_URL.replace("{task_id}", taskId);
        ResponseEntity<String> responseEntity;
        try {
            //调用接口查询任务状态
            responseEntity = dmeAccessService.access(url, HttpMethod.GET, null);
            if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value() || StringUtils.isEmpty(responseEntity.getBody())) {
                LOG.error("queryTaskById failed!taskId={},errorMsg:{}", taskId, responseEntity.getBody());
                return null;
            }
            // 解析返回结果，返回任务状态和成功或者失败的
            List<TaskDetailInfoNew> taskInfos = analyzeTaskRequestResult(responseEntity.getBody());
            TasksResultObject resultMap = analyzeTaskDetailInfo(taskId,taskInfos);
            if (StringUtils.isEmpty(resultMap)) {
                return null;
            }
            return resultMap;
        } catch (DmeException ex) {
            LOG.error("queryTaskById error, errorMsg:{}", ex.getMessage());
            return null;
        }
    }


    /**
      * @Description: 获取任务对应的子任务集合
      * @Param body
      * @return List<TaskDetailInfo>
      * @author yc
      * @Date 2021/5/21 11:44
     */
    private List<TaskDetailInfoNew> analyzeTaskRequestResult(String body) {
        if (StringUtils.isEmpty(body)){
            return null;
        }
        JsonArray jsonArray;
        if (body.contains("total") && body.contains("tasks")) {
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            jsonArray = jsonObject.get("tasks").getAsJsonArray();
        } else {
            jsonArray = new JsonParser().parse(body).getAsJsonArray();
        }
        List<TaskDetailInfoNew>  taskDetailInfoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(Collections.singleton(jsonArray))){
            return null;
        }
        jsonArray.forEach(item -> {
            TaskDetailInfoNew taskDetailInfo = new TaskDetailInfoNew();
            JsonObject jsonObject = item.getAsJsonObject();
            taskDetailInfo.setId(ToolUtils.jsonToStr(jsonObject.get("id")));
            taskDetailInfo.setNameCn(ToolUtils.jsonToStr(jsonObject.get("name_cn")));
            taskDetailInfo.setNameEn(ToolUtils.jsonToStr(jsonObject.get("name_en")));
            taskDetailInfo.setStatus(ToolUtils.jsonToInt(jsonObject.get("status")));
            taskDetailInfo.setProgress(ToolUtils.jsonToInt(jsonObject.get("progress")));
            taskDetailInfo.setOwnerName(ToolUtils.jsonToStr(jsonObject.get("owner_name")));
            taskDetailInfo.setOwnerId(ToolUtils.jsonToStr(jsonObject.get("owner_id")));
            taskDetailInfo.setStartTime(ToolUtils.getLong(jsonObject.get("create_time")));
            taskDetailInfo.setCreateTime(ToolUtils.getLong(jsonObject.get("create_time")));
            taskDetailInfo.setEndTime(ToolUtils.getLong(jsonObject.get("end_time")));
            taskDetailInfo.setDetailEn(ToolUtils.jsonToStr(jsonObject.get("detail_en")));
            taskDetailInfo.setDetailCn(ToolUtils.jsonToStr(jsonObject.get("detail_cn")));
            JsonArray resourcesArray = jsonObject.getAsJsonArray("resources");
            if (resourcesArray != null) {
                List<TaskDetailResource> resourceList = getTaskDetailResources(resourcesArray);
                taskDetailInfo.setResources(resourceList);
            }
            taskDetailInfoList.add(taskDetailInfo);
        });
       return taskDetailInfoList;
    }
    /**
      * @Description: 使用流的方式过滤子任务状态和进行统计
      * @Param @param null
      * @return @return
      * @throws
      * @author yc
      * @Date 2021/5/21 15:13
     */
    private TasksResultObject analyzeTaskDetailInfo(String taskId, List<TaskDetailInfoNew> taskInfos) {
        //首先判断任务集合是否为空
        if(CollectionUtils.isEmpty(taskInfos)){
            return null;
        }
        //根据taskid获取集合中对应的任务状态，如果为100，说明创建成功
        List<TaskDetailInfoNew> taskDeatailInfos  =
                taskInfos.stream().filter(taskDetailInfoNew -> taskId.equalsIgnoreCase(taskDetailInfoNew.getId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskDeatailInfos) || taskDeatailInfos.size()>1){
            return null;
        }
        String description = null;
        if (100 == taskDeatailInfos.get(0).getProgress() || 3 < taskDeatailInfos.get(0).getStatus()){
            TasksResultObject tasksResultObject2 = new TasksResultObject(true);
            description = taskDeatailInfos.get(0).getDetailEn();
            tasksResultObject2.setDescription(description);
            return tasksResultObject2;
        }
    return new TasksResultObject(false);
    }
}
