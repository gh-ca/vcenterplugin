package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeNFSAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "accessnfs")
public class NFSDataStoreController extends BaseController{

    @Autowired
    private DmeNFSAccessService dmeNFSAccessService;

    @RequestMapping(value = "/logicport/{storage_objectId}", method = RequestMethod.GET)
    public ResponseBodyBean portAttr(@PathVariable("storage_objectId") String storage_objectId) throws Exception {
        return success(dmeNFSAccessService.getNFSDatastoreLogicPortAttr(storage_objectId));
    }

    @RequestMapping(value = "/share/{storage_objectId}", method = RequestMethod.GET)
    public ResponseBodyBean shareAttr(@PathVariable("storage_objectId") String storage_objectId) throws Exception {
        return success(dmeNFSAccessService.getNFSDatastoreShareAttr(storage_objectId));
    }

    @RequestMapping(value = "/fileservice/{storage_objectId}", method = RequestMethod.GET)
    public ResponseBodyBean fsAttr(@PathVariable("storage_objectId") String storage_objectId) throws Exception {
        return success(dmeNFSAccessService.getNFSDatastoreFSAttr(storage_objectId));
    }

    @RequestMapping(value = "/scannfs", method = RequestMethod.GET)
    public ResponseBodyBean scannfs() throws Exception {
        return success(dmeNFSAccessService.scanNfs());
    }
}
