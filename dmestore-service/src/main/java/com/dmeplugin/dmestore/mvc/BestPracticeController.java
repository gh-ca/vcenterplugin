package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.BestPracticeProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/bestpractice")
public class BestPracticeController extends BaseController{
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
        }catch (Exception ex){
            return failure(ex.getMessage());
        }
    }

    @RequestMapping(value = "/records/all", method = RequestMethod.GET)
    public ResponseBodyBean getBestPracticeRecords() throws Exception {
        try {
            return success(bestPracticeProcessService.getCheckRecord());
        }catch (Exception ex){
            return failure(ex.getMessage());
        }
    }

    @RequestMapping(value = "/records/bypage", method = RequestMethod.GET)
    public ResponseBodyBean getBySettingAndPage(@RequestParam String hostSetting,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize) throws Exception {
        try {
            return success(bestPracticeProcessService.getCheckRecordBy(hostSetting, pageNo, pageSize));
        }catch (Exception ex){
            return failure(ex.getMessage());
        }
    }

    /**
     * manual update best practise
     */
    @RequestMapping(value = "/manualupdate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean manualupdate()
            throws Exception {
        bestPracticeProcessService.check(null);
        return success();
    }

    /**
     * second entry for apply best practise on host
     */
    @RequestMapping(value = "/manualapplyforhost", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean applyforhost(@RequestParam(value = "hostid", required = true) String hostid)
            throws Exception {

        return success();
    }

    /**
     * second entry for apply best practise on cluster
     */
    @RequestMapping(value = "/manualapplyforcluster", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean applyforcluster(@RequestParam(value = "clusterid", required = true) String clusterid)
            throws Exception {

        return success();
    }
}
