package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.DmePerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dmeperformance")
public class DmePerformanceController  extends BaseController {

    public static final Logger LOG = LoggerFactory.getLogger(DmePerformanceController.class);

    @Autowired
    private DmePerformanceService dmePerformanceService;

    @GetMapping("/storage")
    public ResponseBodyBean getStorageDetail(@RequestParam(name = "storageId") String storageId) {
        try {
            return success(dmePerformanceService.getPerformanceDataStore(storageId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/current", method = RequestMethod.POST)
    public ResponseBodyBean getPerfCurrent(@RequestBody Map<String, Object> params) throws Exception {
        try {
            return success(dmePerformanceService.getPerformanceCurrent(params));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
}
