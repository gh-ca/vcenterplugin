package com.dmeplugin.dmestore.mvc;


import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * overview controller
 */
@RestController
@RequestMapping("/overview")
public class OverviewController extends BaseController{


    @Autowired
    private OverviewService overviewService;
    /**
     * get storage device num from dme(total,normal,abnormal)
     * @return ResponseBodyBean
     * data like {"total": 100,"normal": 90,"abnormal", 10}
     */
    @RequestMapping(value = "/getstoragenum", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getStorageNum(){
        try {
            Map<String, Object> resMap = overviewService.getStorageNum();
            return success(resMap);
        } catch (Exception e) {
            e.printStackTrace();
            return failure();
        }
    }


    /**
     * get dataStorage overview
     * @param type 0 :VMFS and NFS, 1:VMFS, 2:NFS
     * @return ResponseBodyBean
     * data like {
     *             "totalCapacity": 100,
     *             "usedCapacity": 20,
     *             "freeCapacity": 80,
     *             "utilization": 20,
     *             "capacityUnit": "TB"
     *           }
     */
    @RequestMapping(value = "/getdatastoreoverview", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getDataStoreOverview(@RequestParam String type){
        try {
            Map<String, Object> resMap = overviewService.getDataStoreCapacitySummary(type);
            return success(resMap);
        } catch (Exception e) {
            e.printStackTrace();
            return failure();
        }
    }

    /**
     * get dataStorage overview
     * @param type 0 :VMFS and NFS, 1:VMFS, 2:NFS
     * @param topn top n
     * @param orderBy order by this column desc
     * @return ResponseBodyBean
     * data like [{
     *             "id": "5BDCCE7C4DF74E47AA3F042ED95D60909290",
     *             "name": "dataStore1"
     *             "totalCapacity": 100,
     *             "usedCapacity": 20,
     *             "freeCapacity": 80,
     *             "utilization": 20,
     *             "capacityUnit": "TB"
     *           }]
     */
    @RequestMapping(value = "/getdatastoretopn", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getDataStoreTopN(@RequestParam String type,
                                             @RequestParam(required = false) Integer topn,
                                             @RequestParam(defaultValue = "utilization", required = false) String orderBy,
                                             @RequestParam(defaultValue = "desc", required = false) String desc){
        if (topn == null){
            topn = 5;
        }
        //type 0 :VMFS and NFS, 1:VMFS, 2:NFS
        try {
            List<Map<String, Object>> resMap = overviewService.getDataStoreCapacityTopN(type, topn);
            return success(resMap);
        } catch (Exception e) {
            e.printStackTrace();
            return failure();
        }
    }


    /**
     * get best practice violations
     * @return ResponseBodyBean
     * data like {
     *        critical : 5,
     *        major: 2,
     *        warning: 3,
     *        info: 44
     *        }
     */
    @RequestMapping(value = "/getbestpracticeviolations", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyBean getBestPracticeViolations(){
        //type 0 :critical, 1:major, 2:warning, 3: info
        try {
            Map<String, Object> resMap = overviewService.getBestPracticeViolations();
            return success(resMap);
        } catch (Exception e) {
            e.printStackTrace();
            return failure();
        }
    }
}
