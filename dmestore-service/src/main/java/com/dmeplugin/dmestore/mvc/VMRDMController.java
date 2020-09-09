package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmRDMCreateBean;
import com.dmeplugin.dmestore.services.TaskService;
import com.dmeplugin.dmestore.services.VMRDMService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "v1/vmrdm")
public class VMRDMController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(VMRDMController.class);

    @Autowired
    private VMRDMService vmrdmService;

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "{host_id}", method = RequestMethod.POST)
    public ResponseBodyBean createRDM(@PathVariable("host_id") String host_id,
                                      @RequestBody VmRDMCreateBean createBean) throws Exception {
        String taskId = vmrdmService.createRDM(createBean);
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if(null != taskDetail && taskDetail.get("status").getAsInt() == 3){
            List<String> volumeIds = new ArrayList();
            //获取卷资源
            JsonArray resources = taskDetail.getAsJsonArray("resources");
            for(int i=0;i<resources.size();i++){
                JsonObject resource = resources.get(i).getAsJsonObject();
                String id = resource.get("id").getAsString();
                volumeIds.add(id);
            }
            //将卷映射给主机
            taskId = vmrdmService.hostMapping(host_id, volumeIds);
            taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
            if(null != taskDetail && taskDetail.get("status").getAsInt() == 3){
                LOG.info("DME create volume and map to host successful!");
            }else {
                LOG.info("Disk mapping to host failed!");
            }
        }
        return null;
    }
}
