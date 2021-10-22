package com.huawei.dmestore.mvc;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.DelVmRdmsRequest;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.model.VmRdmCreateBean;
import com.huawei.dmestore.services.VmRdmService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * VmRdmController
 *
 * @author wangxiangyong
 * @since 2020-09-03
 **/
@RestController
@RequestMapping(value = "v1/vmrdm")
public class VmRdmController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(VmRdmController.class);
    private final Gson gson = new Gson();
    @Autowired
    private VmRdmService vmRdmService;


    /**
     * createBean
     *
     * @param vmObjectId vmObjectId
     * @param createBean createBean
     * @param dataStoreObjectId dataStoreObjectId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "createRdm", method = RequestMethod.POST)
    public ResponseBodyBean createRdm(@RequestParam("vmObjectId") String vmObjectId,
        @RequestBody VmRdmCreateBean createBean, @RequestParam("dataStoreObjectId") String dataStoreObjectId) {
        try {
            vmRdmService.createRdm(dataStoreObjectId, vmObjectId, createBean, createBean.getCompatibilityMode());
            return success();
        } catch (DmeException e) {
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    /**
     * dmeHosts
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "dmeHosts", method = RequestMethod.GET)
    public ResponseBodyBean dmeHosts() {
        try {
            return success(vmRdmService.getAllDmeHost());
        } catch (DmeException e) {
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    /**
     * getDatastoreMountsOnHost
     *
     * @param vmObjectId vmObjectId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "vCenter/datastoreOnHost", method = RequestMethod.GET)
    public ResponseBodyBean getDatastoreMountsOnHost(@RequestParam("vmObjectId") String vmObjectId) {
        try {
            List<Object> objects = vmRdmService.getDatastoreMountsOnHost(vmObjectId);
            return success(new Gson().toJson(objects));
        } catch (DmeException e) {
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    /**
     * getRdms
     *
     * @param vmObjectId vmObjectId
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "rdms/{vmObjectId}", method = RequestMethod.GET)
    public ResponseBodyBean getRdms(@PathVariable("vmObjectId") String vmObjectId) {
        try {
            List<Map<String, String>> rdms = vmRdmService.getVmRdmByObjectId(vmObjectId);
            return success(rdms);
        } catch (DmeException e) {
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

    /**
     * delRdms
     *
     * @param vmObjectId vmObjectId
     * @param diskObjectIds RDM对象标识
     * @param language 语言
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "rdms/{vmObjectId}", method = RequestMethod.POST)
    public ResponseBodyBean delRdms(@PathVariable("vmObjectId") String vmObjectId,
        @RequestBody List<DelVmRdmsRequest> diskObjectIds,
        @RequestParam(value = "language", required = false) String language) {
        try {
            if(StringUtils.isEmpty(language)){
                language = DmeConstants.LANGUAGE_CN;
            }
            String delResultStr = vmRdmService.delVmRdmByObjectId(vmObjectId, diskObjectIds, language);
            JsonObject delResultObj = gson.fromJson(delResultStr, JsonObject.class);
            ResponseBodyBean responseBodyBean = new ResponseBodyBean();
            responseBodyBean.setCode(delResultObj.get("code").getAsString());
            if(delResultObj.get("data") != null){
                List<String> hostList = gson.fromJson(delResultObj.get("data"), List.class);
                responseBodyBean.setData(hostList);
            }
            responseBodyBean.setDescription(delResultObj.get("description").getAsString());
            return responseBodyBean;
        } catch (DmeException e) {
            LOG.error(e.getMessage());
            return failure(e.getMessage());
        }
    }

}
