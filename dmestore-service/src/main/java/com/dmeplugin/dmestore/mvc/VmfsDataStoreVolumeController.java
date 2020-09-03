package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Administrator
 * @Description vmfs Data Store 卷详细信息查询
 * @Date 14:17 2020/9/3
 * @Param
 * @Return
 **/
@RestController
@RequestMapping(value = "v1/vmfsdatastrore")
public class VmfsDataStoreVolumeController extends BaseController{
    @RequestMapping(value = "volume/{volume_id}", method = RequestMethod.GET)
    public ResponseBodyBean volumeDetail(@PathVariable(value = "volume_id") String volume_id) throws Exception {
        return null;
    }
}
