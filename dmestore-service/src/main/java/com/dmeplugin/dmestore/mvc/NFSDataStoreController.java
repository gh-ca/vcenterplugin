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

    @RequestMapping(value = "/logicport/{logic_port_id}", method = RequestMethod.GET)
    public ResponseBodyBean portAttr(@PathVariable("logic_port_id") String logic_port_id) throws Exception {
        return success(dmeNFSAccessService.getNFSDatastoreLogicPortAttr(logic_port_id));
    }

    @RequestMapping(value = "/share/{nfs_share_id}", method = RequestMethod.GET)
    public ResponseBodyBean shareAttr(@PathVariable("nfs_share_id") String nfs_share_id) throws Exception {
        return success(dmeNFSAccessService.getNFSDatastoreShareAttr(nfs_share_id));
    }

    @RequestMapping(value = "/fileservice/{storage_id}", method = RequestMethod.GET)
    public ResponseBodyBean fsAttr(@PathVariable("storage_id") String storage_id) throws Exception {
        return success(dmeNFSAccessService.getNFSDatastoreFSAttr(storage_id));
    }
}
