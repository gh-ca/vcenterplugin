package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.OverviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * OverviewController
 *
 * @author liuxh
 * @since 2020-09-02
 **/
@RestController
@RequestMapping("/overview")
public class OverviewController extends BaseController {
    private static final int DEFAULT_TOPN = 5;

    @Autowired
    private OverviewService overviewService;

    @RequestMapping(value = "/getstoragenum", method = RequestMethod.GET)
    public ResponseBodyBean getStorageNum() {
        try {
            return success(overviewService.getStorageNum());
        } catch (DMEException e) {
            return failure(e.getMessage());
        }
    }

    @RequestMapping(value = "/getdatastoreoverview", method = RequestMethod.GET)
    public ResponseBodyBean getDataStoreOverview(@RequestParam String type) {
        return success(overviewService.getDataStoreCapacitySummary(type));
    }

    @RequestMapping(value = "/getdatastoretopn", method = RequestMethod.GET)
    public ResponseBodyBean getDataStoreTopN(@RequestParam String type, @RequestParam(required = false) Integer topn,
        @RequestParam(defaultValue = "utilization", required = false) String orderBy,
        @RequestParam(defaultValue = "desc", required = false) String desc) {
        int topnDefault = DEFAULT_TOPN;
        if (null != topn) {
            topnDefault = topn.intValue();
        }

        return success(overviewService.getDataStoreCapacityTopN(type, topnDefault));
    }

    @RequestMapping(value = "/getbestpracticeviolations", method = RequestMethod.GET)
    public ResponseBodyBean getBestPracticeViolations() {
        // type 0 :critical, 1:major, 2:warning, 3: info
        return success(overviewService.getBestPracticeViolations());
    }
}
