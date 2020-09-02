package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.NfsDataInfo;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/nfsdatastrore")
public class NFSDataStoreController extends BaseController{
    @RequestMapping(value = "/port", method = RequestMethod.POST)
    public ResponseBodyBean portAttr(@RequestBody NfsDataInfo nfsDataInfo) throws Exception {

        return null;
    }

    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public ResponseBodyBean shareAttr(@RequestBody NfsDataInfo nfsDataInfo) throws Exception {

        return null;
    }

    @RequestMapping(value = "/fs", method = RequestMethod.POST)
    public ResponseBodyBean fsAttr(@RequestBody NfsDataInfo nfsDataInfo) throws Exception {

        return null;
    }
}
