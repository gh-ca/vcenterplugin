package com.dmeplugin.dmestore.mvc;


import com.dmeplugin.dmestore.model.ResponseBodyBean;
import org.springframework.web.bind.annotation.*;

/**
 * overview controller
 */
@RestController
public class OverviewController extends BaseController{

    /**
     * get storage device num from dme(total,normal,abnormal)
     * @return ResponseBodyBean
     * data like {"total": 100,"normal": 90,"abnormal", 10}
     */
    @RequestMapping(value = "/getstoragenum", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getStorageNum(){
        return success();
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
    @RequestMapping(value = "/getdatastoreoverview", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getDataStoreOverview(@RequestParam String type){
        //type 0 :VMFS and NFS, 1:VMFS, 2:NFS
        if ("0".equals(type)){

        } else if ("1".equals(type)){

        } else if ("2".equals(type)){

        }
        return success();
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
    @RequestMapping(value = "/getdatastoretopn", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getDataStoreTopN(@RequestParam String type,
                                             @RequestParam(required = false) Integer topn,
                                             @RequestParam(defaultValue = "utilization", required = false) String orderBy,
                                             @RequestParam(defaultValue = "desc", required = false) String desc){
        if (topn == null){
            topn = 5;
        }
        //type 0 :VMFS and NFS, 1:VMFS, 2:NFS
        if ("0".equals(type)){

        } else if ("1".equals(type)){

        } else if ("2".equals(type)){

        }
        return success();
    }


    /**
     * get best practice violations
     * @param type 0 :critical, 1:major, 2:warning, 3: info
     * @return ResponseBodyBean
     * data like {"num": 5}
     */
    @RequestMapping(value = "/getbestpracticeviolations", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean getBestPracticeViolations(@RequestParam String type){
        //type 0 :critical, 1:major, 2:warning, 3: info
        if ("0".equals(type)){

        } else if ("1".equals(type)){

        } else if ("2".equals(type)){

        } else if ("4".equals(type)){

        }
        return success();
    }
}
