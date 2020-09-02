package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/vmfsDataStroreVolume")
public class VmfsDataStroreVolumeController extends BaseController{
    @RequestMapping(value = "/{volume_id}", method = RequestMethod.GET)
    public ResponseBodyBean volumeDetail(@PathVariable(value = "volume_id") String volume_id) throws Exception {
        return null;
    }
}
