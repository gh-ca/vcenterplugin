package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/servicelevel")
public class DMEServiceLevelController extends BaseController{
    /**
     * Echo a message back to the client.
     */
    @RequestMapping(value = "/manualupdate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBodyBean manualupdate(@RequestParam(value = "message", required = true) String message)
            throws Exception {

        return success();
    }
}
