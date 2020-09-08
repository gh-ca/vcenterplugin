package com.dmeplugin.dmestore.mvc;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operatenfs")
@Api
public class NfsOperationController {

    public static final Logger LOG = LoggerFactory.getLogger(NfsOperationController.class);

    @Autowired
    private Gson gson;


}
