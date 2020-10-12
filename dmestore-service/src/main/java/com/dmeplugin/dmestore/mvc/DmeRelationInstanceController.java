package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.RelationInstance;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeRelationInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO
 * @ClassName: DmeRelationInstanceController
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-15
 **/
@RestController
@RequestMapping(value = "/relationinstance")
public class DmeRelationInstanceController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(DmeRelationInstanceController.class);

    @Autowired
    DmeRelationInstanceService dmeRelationInstanceService;

    @GetMapping("/listbyrelationname")
    @ResponseBody
    public ResponseBodyBean queryByRelationName(@RequestParam(name = "relationName") String relationName) {
        LOG.info("listbyrelationname:" + relationName);
        try {
            List<RelationInstance> ris = dmeRelationInstanceService.queryRelationByRelationName(relationName);
            return success(ris);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String errMsg = "queryRelationInstanceByRelationName error,relationName:" + relationName;
        return failure(errMsg);
    }

    @GetMapping("/getbyrelationnameinstanceid")
    @ResponseBody
    public ResponseBodyBean queryByRelationNameInstanceId(@RequestParam(name = "relationName") String relationName, @RequestParam(name = "instanceId") String instanceId) {
        LOG.info("getbyrelationname instanceid:" + relationName + " , " + instanceId);
        try {
            RelationInstance ri = dmeRelationInstanceService.queryRelationByRelationNameInstanceId(relationName, instanceId);
            return success(ri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String errMsg = "queryRelationInstanceByRelationNameInstanceId error,relationName:" + relationName + " , " + instanceId;
        return failure(errMsg);
    }

    @GetMapping("/getbyrelationnameinstanceidcondition")
    @ResponseBody
    public ResponseBodyBean queryByRelationNameInstanceIdCondition(@RequestParam(name = "relationName") String relationName, @RequestParam(name = "instanceId") String instanceId) {
        LOG.info("getbyrelationname instanceid:" + relationName + " , " + instanceId);
        try {
            List<RelationInstance> ris = dmeRelationInstanceService.queryRelationByRelationNameConditionSourceInstanceId(relationName, instanceId);
            return success(ris);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String errMsg = "queryRelationInstanceByRelationNameInstanceId error,relationName:" + relationName + " , " + instanceId;
        return failure(errMsg);
    }



}
