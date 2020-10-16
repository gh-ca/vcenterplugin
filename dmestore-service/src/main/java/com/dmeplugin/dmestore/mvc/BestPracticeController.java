package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.BestPracticeUpdateByTypeRequest;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.BestPracticeProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/bestpractice")
public class BestPracticeController extends BaseController {
    @Autowired
    private BestPracticeProcessService bestPracticeProcessService;

    public BestPracticeProcessService getBestPracticeProcessService() {
        return bestPracticeProcessService;
    }

    public void setBestPracticeProcessService(BestPracticeProcessService bestPracticeProcessService) {
        this.bestPracticeProcessService = bestPracticeProcessService;
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResponseBodyBean bestPractice() throws Exception {
        try {
            bestPracticeProcessService.check(null);
            return success();
        } catch (Exception ex) {
            return failure(ex.getMessage());
        }
    }

    @RequestMapping(value = "/records/all", method = RequestMethod.GET)
    public ResponseBodyBean getBestPracticeRecords() throws Exception {
        try {
            return success(bestPracticeProcessService.getCheckRecord());
        } catch (Exception ex) {
            return failure(ex.getMessage());
        }
    }

    @RequestMapping(value = "/records/bypage", method = RequestMethod.GET)
    public ResponseBodyBean getBySettingAndPage(@RequestParam String hostSetting,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize) throws Exception {
        try {
            return success(bestPracticeProcessService.getCheckRecordBy(hostSetting, pageNo, pageSize));
        } catch (Exception ex) {
            return failure(ex.getMessage());
        }
    }


    @RequestMapping(value = "/update/bylist", method = RequestMethod.POST)
    public ResponseBodyBean bylist(@RequestBody List<BestPracticeUpdateByTypeRequest> list) throws Exception {
        try {
            for (int i = 0; i < list.size(); i++) {
                BestPracticeUpdateByTypeRequest request = list.get(i);
                upByHostSetting(request);
            }
            return success();
        } catch (Exception ex) {
            return failure(ex.getMessage());
        }
    }

    //应用某一最佳实践项的指定主机
    @RequestMapping(value = "/update/bytype", method = RequestMethod.POST)
    public ResponseBodyBean upByHostSetting(@RequestBody BestPracticeUpdateByTypeRequest request) throws Exception {
        try {
            return success(bestPracticeProcessService.update(request.getHostObjectIds(), request.getHostSetting()));
        } catch (Exception ex) {
            return failure(ex.getMessage());
        }
    }

    //应用指定主机列表的所有最佳实践项
    @RequestMapping(value = "/update/byhosts", method = RequestMethod.POST)
    public ResponseBodyBean upByHosts(@RequestBody List<String> hostObjectIds) throws Exception {
        try {
            return success(bestPracticeProcessService.update(hostObjectIds));
        } catch (Exception ex) {
            return failure(ex.getMessage());
        }
    }

    //应用所有主机最佳实践项
    @RequestMapping(value = "/update/all", method = RequestMethod.POST)
    public ResponseBodyBean upAll() throws Exception {
        try {
            return success(bestPracticeProcessService.update(null, null));
        } catch (Exception ex) {
            return failure(ex.getMessage());
        }
    }

    //应用集群最佳实践项
    @RequestMapping(value = "/update/byCluster", method = RequestMethod.POST)
    public ResponseBodyBean upByCluster() throws Exception {
        try {
            return success(bestPracticeProcessService.update(null, null));
        } catch (Exception ex) {
            return failure(ex.getMessage());
        }
    }

}
