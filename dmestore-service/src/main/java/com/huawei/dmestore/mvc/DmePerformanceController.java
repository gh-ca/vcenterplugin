package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.services.DmePerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
