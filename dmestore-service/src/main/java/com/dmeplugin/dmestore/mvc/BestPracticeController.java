package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.BestPracticeProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseBodyBean bestPractice() throws Exception {
        bestPracticeProcessService.check(null);
        return success();
    }

    @RequestMapping(value = "/records", method = RequestMethod.GET)
    public ResponseBodyBean getBestPracticeRecords() throws Exception {
        return success(bestPracticeProcessService.getCheckRecord());
    }
}
