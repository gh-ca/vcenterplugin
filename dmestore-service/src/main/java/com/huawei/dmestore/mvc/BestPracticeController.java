package com.huawei.dmestore.mvc;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.model.BestPracticeLog;
import com.huawei.dmestore.model.BestPracticeRecommand;
import com.huawei.dmestore.model.BestPracticeRecommandUpReq;
import com.huawei.dmestore.model.BestPracticeUpResultResponse;
import com.huawei.dmestore.model.BestPracticeUpdateByTypeRequest;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.model.UpHostVnicRequestBean;
import com.huawei.dmestore.services.BestPracticeProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BestPracticeController
 *
 * @author wangxiangyong
 * @since 2020-12-01
 **/
@RestController
@RequestMapping(value = "v1/bestpractice")
public class BestPracticeController extends BaseController {
    @Autowired
    private BestPracticeProcessService bestPracticeProcessService;

    public BestPracticeProcessService getBestPracticeProcessService() {
        return bestPracticeProcessService;
    }

    public void setBestPracticeProcessService(BestPracticeProcessService bestPracticeProcessService) {
        this.bestPracticeProcessService = bestPracticeProcessService;
    }

    /**
     * bestPractice
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResponseBodyBean bestPractice(@RequestParam(required = false) String objectId) {
        try {
            bestPracticeProcessService.check(objectId);
            return success();
        } catch (VcenterException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * bestPractice
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/check/byCluster", method = RequestMethod.POST)
    public ResponseBodyBean checkByCluster(@RequestParam String clusterObjId) {
        try {
            bestPracticeProcessService.checkByCluster(clusterObjId);
            return success();
        } catch (VcenterException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * bestPractice
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/check/vmfs", method = RequestMethod.POST)
    public ResponseBodyBean checkVmfs(@RequestBody(required = false) List<String> objectIds) {
        try {
            bestPracticeProcessService.checkVmfs(objectIds);
            return success();
        } catch (VcenterException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * getBestPracticeRecords
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/records/all", method = RequestMethod.GET)
    public ResponseBodyBean getBestPracticeRecords(@RequestParam(required = false) String type,
        @RequestParam(required = false) String objectId) {
        try {
            return success(bestPracticeProcessService.getCheckRecord(type, objectId));
        } catch (DmeSqlException ex) {
            return failure(ex.getMessage());
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getBySettingAndPage
     *
     * @param hostSetting hostSetting
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/records/bypage", method = RequestMethod.GET)
    public ResponseBodyBean getBySettingAndPage(@RequestParam String hostSetting, @RequestParam int pageNo,
        @RequestParam int pageSize) {
        try {
            return success(bestPracticeProcessService.getCheckRecordBy(hostSetting, pageNo, pageSize));
        } catch (DmeException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * bylist
     *
     * @param list list
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/bylist", method = RequestMethod.POST)
    public ResponseBodyBean bylist(@RequestBody List<BestPracticeUpdateByTypeRequest> list) {
        try {
            List<BestPracticeUpResultResponse> resultList = new ArrayList<>();
            for (int start = 0; start < list.size(); start++) {
                BestPracticeUpdateByTypeRequest request = list.get(start);
                List<BestPracticeUpResultResponse> listTemp = bestPracticeProcessService.update(
                    request.getHostObjectIds(), request.getHostSetting(), false);
                if (null != listTemp && listTemp.size() > 0) {
                    resultList.addAll(listTemp);
                }
            }
            return success(resultList);
        } catch (DmeException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * upByHostSetting
     *
     * @param request request
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/bytype", method = RequestMethod.POST)
    public ResponseBodyBean upByHostSetting(@RequestBody BestPracticeUpdateByTypeRequest request) {
        try {
            return success(
                bestPracticeProcessService.update(request.getHostObjectIds(), request.getHostSetting(), false));
        } catch (DmeException ex) {
            return failure(ex.getMessage());
        }
    }

    /**
     * upByHosts
     *
     * @param hostObjectIds hostObjectIds
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/byhosts", method = RequestMethod.POST)
    public ResponseBodyBean upByHosts(@RequestBody List<String> hostObjectIds) {
        try {
            return success(bestPracticeProcessService.update(hostObjectIds));
        } catch (DmeSqlException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * upAll
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/all", method = RequestMethod.POST)
    public ResponseBodyBean upAll() {
        try {
            return success(bestPracticeProcessService.update(null));
        } catch (DmeSqlException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * upByCluster
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/byCluster", method = RequestMethod.POST)
    public ResponseBodyBean upByCluster() {
        try {
            return success(bestPracticeProcessService.update(null, null, false));
        } catch (DmeSqlException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * upByCluster
     *
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/update/byCluster/{clusterObjectId}", method = RequestMethod.POST)
    public ResponseBodyBean upByClusterId(@PathVariable("clusterObjectId") String clusterObjectId) {
        try {
            return success(bestPracticeProcessService.updateByCluster(clusterObjectId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getVirtualNicList
     *
     * @param hostObjectIds hostObjectIds
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/virtual-nic", method = RequestMethod.POST)
    public ResponseBodyBean getVirtualNicList(@RequestBody List<String> hostObjectIds) {
        return success(bestPracticeProcessService.getVirtualNicList(hostObjectIds));
    }

    /**
     * getVirtualNicList
     *
     * @param beans beans
     * @return ResponseBodyBean
     */
    @RequestMapping(value = "/virtual-nic/update", method = RequestMethod.POST)
    public ResponseBodyBean updateVirtualNicList(@RequestBody List<UpHostVnicRequestBean> beans) {
        bestPracticeProcessService.upVirtualNicList(beans);
        return success();
    }

    @RequestMapping(value = "/recommand", method = RequestMethod.GET)
    public ResponseBodyBean recommand(@RequestParam(value = "hostsetting") String hostsetting) {
        try {
            BestPracticeRecommand recommand = bestPracticeProcessService.getRecommand(hostsetting);
            return success(recommand);
        } catch (DmeSqlException ex) {
            LOGGER.error("get recommand value error!", ex);
            return failure(ex.getMessage());
        }
    }

    @RequestMapping(value = "/recommand/{id}", method = RequestMethod.PUT)
    public ResponseBodyBean recommandUp(@PathVariable(value = "id") String id,
        @RequestBody BestPracticeRecommandUpReq req) {
        try {
            int size = bestPracticeProcessService.recommandUp("ID", id, req);
            return success(size);
        } catch (DmeSqlException ex) {
            LOGGER.error("get recommand value error!", ex);
            return failure(ex.getMessage());
        }
    }

    @RequestMapping(value = "/recommand", method = RequestMethod.PUT)
    public ResponseBodyBean recommendUpByHostSetting(@RequestParam(value = "hostSetting") String hostSetting,
        @RequestBody BestPracticeRecommandUpReq req) {
        try {
            int size = bestPracticeProcessService.recommandUp("HOST_SETTING", hostSetting, req);
            return success(size);
        } catch (DmeSqlException ex) {
            LOGGER.error("get recommend value error!", ex);
            return failure(ex.getMessage());
        }
    }

    @RequestMapping(value = "/logs", method = RequestMethod.POST)
    public ResponseBodyBean saveLogs(@RequestBody List<BestPracticeLog> logs) {
        bestPracticeProcessService.saveLog(logs);
        return success();
    }

    @RequestMapping(value = "/logs", method = RequestMethod.GET)
    public ResponseBodyBean getLogs(@RequestParam(required = false) String hostsetting,
        @RequestParam(required = false) String objectId,
        @RequestParam(required = false) Integer pageNo,
        @RequestParam(required = false) Integer pageSize) {
        try {
            List<BestPracticeLog> logList = bestPracticeProcessService.getLog(hostsetting, objectId,
                pageNo == null ? 0 : pageNo, pageSize == null ? 0 : pageSize);
            return success(logList);
        } catch (DmeSqlException e) {
            return failure(e.getMessage());
        }
    }
}
