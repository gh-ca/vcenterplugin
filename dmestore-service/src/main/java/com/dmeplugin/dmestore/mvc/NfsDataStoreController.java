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
public class NfsDataStoreController extends BaseController{

    @Autowired
    private DmeNFSAccessService dmeNFSAccessService;

    @RequestMapping(value = "/logicport/{storageObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean portAttr(@PathVariable("storageObjectId") String storageObjectId) throws Exception {
        return success(dmeNFSAccessService.getNFSDatastoreLogicPortAttr(storageObjectId));
    }

    @RequestMapping(value = "/share/{storageObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean shareAttr(@PathVariable("storageObjectId") String storageObjectId) throws Exception {
        return success(dmeNFSAccessService.getNFSDatastoreShareAttr(storageObjectId));
    }

    @RequestMapping(value = "/fileservice/{storageObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean fsAttr(@PathVariable("storageObjectId") String storageObjectId) throws Exception {
        return success(dmeNFSAccessService.getNFSDatastoreFSAttr(storageObjectId));
    }

    @RequestMapping(value = "/scannfs", method = RequestMethod.GET)
    public ResponseBodyBean scannfs() throws Exception {
        return success(dmeNFSAccessService.scanNfs());
    }
}
