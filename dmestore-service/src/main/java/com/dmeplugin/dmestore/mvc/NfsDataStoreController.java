package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.services.DmeNFSAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * NfsDataStoreController
 *
 * @author wangxiangyong
 * @since 2020-09-02
 **/
@RestController
@RequestMapping(value = "accessnfs")
public class NfsDataStoreController extends BaseController {
    @Autowired
    private DmeNFSAccessService dmeNfsAccessService;

    @RequestMapping(value = "/logicport/{storageObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean portAttr(@PathVariable("storageObjectId") String storageObjectId) {
        String failureStr;
        try {
            return success(dmeNfsAccessService.getNfsDatastoreLogicPortAttr(storageObjectId));
        } catch (DMEException e) {
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    @RequestMapping(value = "/share/{storageObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean shareAttr(@PathVariable("storageObjectId") String storageObjectId) {
        String failureStr;
        try {
            return success(dmeNfsAccessService.getNfsDatastoreShareAttr(storageObjectId));
        } catch (DMEException e) {
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    @RequestMapping(value = "/fileservice/{storageObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean fsAttr(@PathVariable("storageObjectId") String storageObjectId) {
        String failureStr;
        try {
            return success(dmeNfsAccessService.getNfsDatastoreFsAttr(storageObjectId));
        } catch (DMEException e) {
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }

    @RequestMapping(value = "/scannfs", method = RequestMethod.GET)
    public ResponseBodyBean scannfs() {
        String failureStr;
        try {
            return success(dmeNfsAccessService.scanNfs());
        } catch (DMEException e) {
            failureStr = e.getMessage();
        }
        return failure(failureStr);
    }
}
