package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.BestPracticeReq;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/bestpractice")
public class BestPracticeController extends BaseController{
    @RequestMapping(method = RequestMethod.POST)
    public ResponseBodyBean bestPractice(@RequestBody BestPracticeReq request) throws Exception {

        return null;
    }
}
