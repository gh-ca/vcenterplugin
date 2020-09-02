package com.dmeplugin.dmestore.mvc;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @ClassName: NfsDataStatisticAccessController
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-02
 **/
@RestController
@RequestMapping(value = "/accessnfsstatistic")
public class NfsDataStatisticController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(NfsDataStatisticController.class);

    @Autowired
    private Gson gson;

}
