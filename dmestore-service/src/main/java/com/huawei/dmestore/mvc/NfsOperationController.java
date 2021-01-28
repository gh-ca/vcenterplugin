package com.huawei.dmestore.mvc;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.CapacityAutonegotiation;
import com.huawei.dmestore.model.ResponseBodyBean;
import com.huawei.dmestore.model.StorageDetail;
import com.huawei.dmestore.model.StorageTypeShow;
import com.huawei.dmestore.services.DmeStorageService;
import com.huawei.dmestore.services.NfsOperationService;
import com.huawei.dmestore.utils.ToolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * NfsOperationController
 *
 * @author liuxh
 * @since 2020-09-02
 **/
@RestController
@RequestMapping("/operatenfs")
public class NfsOperationController extends BaseController {
    /**
     * QOS策略上限标识
     */
    private static final String CONTROL_UP = "up";

    /**
     * QOS策略下限标识
     */
    private static final String CONTROL_LOW = "low";

    /**
     * nfsName
     */
    private static final String NFS_NAME_FIELD = "nfsName";

    /**
     * latency
     */
    private static final String LATENCY_FIELD = "latency";

    /**
     * allocation_type
     */
    private static final String ALLOCATION_TYPE_FIELD = "allocation_type";

    /**
     * compression_enabled
     */
    private static final String COMPRESSION_ENABLED_FIELD = "compression_enabled";

    /**
     * fsName
     */
    private static final String FSNAME_FIELD = "fsName";

    /**
     * thin
     */
    private static final String THIN_FIELD = "thin";

    /**
     * name
     */
    private static final String NAME_FIELD = "name";

    /**
     * deduplication_enabled
     */
    private static final String DEDUPLICATION_ENABLED_FIELD = "deduplication_enabled";

    /**
     * capacity_self_adjusting_mode
     */
    private static final String ADJUSTING_MODE_FIELD = "capacity_self_adjusting_mode";

    /**
     * auto_size_enable
     */
    private static final String AUTO_SIZE_ENABLE_FIELD = "auto_size_enable";

    /**
     * autoSizeEnable
     */
    private static final String AUTO_SIZE_ENABLE_REQUEST_FIELD = "autoSizeEnable";

    /**
     * 文件路径分隔符
     */
    private static final String FILE_SEPARATOR = "/";

    @Autowired
    private NfsOperationService nfsOperationService;

    @Autowired
    private DmeStorageService dmeStorageService;

    /**
     * createNfsDatastore
     *
     * @param requestParams requestParams
     * @return ResponseBodyBean
     */
    @PostMapping("/createnfsdatastore")
    public ResponseBodyBean createNfsDatastore(@RequestBody Map<String, Object> requestParams) {
        try {
            Map<String, Object> targetParams = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
            parseCreateNfsParams(requestParams, targetParams);
            nfsOperationService.createNfsDatastore(targetParams);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * updateNfsDatastore
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @PostMapping("/updatenfsdatastore")
    public ResponseBodyBean updateNfsDatastore(final @RequestBody Map<String, Object> params) throws DmeException {
        try {
            Map<String, Object> param = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
            parseUpdateNfsParams(params, param);
            nfsOperationService.updateNfsDatastore(param);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * changeNfsCapacity
     *
     * @param params params
     * @return ResponseBodyBean
     */
    @PutMapping("/changenfsdatastore")
    public ResponseBodyBean changeNfsCapacity(final @RequestBody Map<String, Object> params) {
        try {
            nfsOperationService.changeNfsCapacity(params);
            return success();
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }

    /**
     * getEditNfsStore
     *
     * @param storeObjectId storeObjectId
     * @return ResponseBodyBean
     */
    @GetMapping("/editnfsstore")
    public ResponseBodyBean getEditNfsStore(@RequestParam(name = "storeObjectId") String storeObjectId) {
        try {
            return success(nfsOperationService.getEditNfsStore(storeObjectId));
        } catch (DmeException e) {
            return failure(e.getMessage());
        }
    }
    /**
     * advanceExcute
     *
     * @param requestParams requestParams
     * @param targetParams  targetParams
     * @param isAdvance     isAdvance
     */
    private void advanceExcute(Map<String, Object> requestParams, Map<String, Object> targetParams, boolean isAdvance,
                               StorageTypeShow storageTypeShow) {
        Map<String, Object> tuning = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        Map<String, Object> capacityAutonegotiation = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        String defaultCapacitymode = CapacityAutonegotiation.CAPACITY_MODE_OFF;
        if (isAdvance) {
            parseQos((Boolean) requestParams.get("qosFlag"), requestParams, targetParams, false);
            if ((Boolean) requestParams.get(THIN_FIELD)) {
                tuning.put(ALLOCATION_TYPE_FIELD, THIN_FIELD);
            } else {
                tuning.put(ALLOCATION_TYPE_FIELD, "thick");
            }
            if (storageTypeShow.getDeduplicationShow() || storageTypeShow.getCompressionShow()) {
                tuning.put(COMPRESSION_ENABLED_FIELD, requestParams.get("compressionEnabled"));
                tuning.put(DEDUPLICATION_ENABLED_FIELD, requestParams.get("deduplicationEnabled"));
            }

            String capacitymode = (Boolean) requestParams.get(AUTO_SIZE_ENABLE_REQUEST_FIELD)
                ? CapacityAutonegotiation.CAPACITY_MODE_AUTO : defaultCapacitymode;
            if (!"grow_off".equalsIgnoreCase(capacitymode)) {
                capacityAutonegotiation.put("capacity_recycle_mode", "expand_capacity");
                capacityAutonegotiation.put("auto_grow_threshold_percent", 85);
                capacityAutonegotiation.put("auto_shrink_threshold_percent", 50);
                capacityAutonegotiation.put("max_auto_size", 16777216);
                capacityAutonegotiation.put("min_auto_size", 16777216);
                capacityAutonegotiation.put("auto_size_increment", 1024);
                capacityAutonegotiation.put(AUTO_SIZE_ENABLE_FIELD, requestParams.get(AUTO_SIZE_ENABLE_REQUEST_FIELD));
            }
            capacityAutonegotiation.put(ADJUSTING_MODE_FIELD, capacitymode);
        } else {
            tuning.put(ALLOCATION_TYPE_FIELD, THIN_FIELD);
            if (storageTypeShow.getDeduplicationShow() || storageTypeShow.getCompressionShow()) {
                tuning.put(COMPRESSION_ENABLED_FIELD, false);
                tuning.put(DEDUPLICATION_ENABLED_FIELD, false);
            }
            //capacityAutonegotiation.put(AUTO_SIZE_ENABLE_FIELD, false);
            capacityAutonegotiation.put(ADJUSTING_MODE_FIELD, defaultCapacitymode);
        }
        targetParams.put("tuning", tuning);
        targetParams.put("capacity_autonegotiation", capacityAutonegotiation);
    }

    public StorageTypeShow isDorado(String storageId) throws DmeException {
        StorageTypeShow storageTypeShow = new StorageTypeShow();
        StorageDetail storageDetail = dmeStorageService.getStorageDetail(storageId);
        if (storageDetail != null) {
            String storageType = storageDetail.getModel() +" "+ storageDetail.getProductVersion();
            storageTypeShow = ToolUtils.getStorageTypeShow(storageType);
        }
        return storageTypeShow;
    }

    private void parseQos(Boolean qosFlag, Map<String, Object> params, Map<String, Object> param, boolean enabled) {
        Map<String, Object> qosPolicy = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        if (qosFlag) {
            String contolPolicy = (String) params.get("contolPolicy");
            if (CONTROL_UP.equals(contolPolicy)) {
                if (params.get("maxBandwidth") != null) {
                    qosPolicy.put("max_bandwidth", Integer.parseInt((String) params.get("maxBandwidth")));
                }
                if (params.get("maxIops") != null) {
                    qosPolicy.put("max_iops", Integer.parseInt((String) params.get("maxIops")));
                }
            } else if (CONTROL_LOW.equals(contolPolicy)) {
                if (params.get("minBandwidth") != null) {
                    qosPolicy.put("min_bandwidth", Integer.parseInt((String) params.get("minBandwidth")));
                }
                if (params.get("minIops") != null) {
                    qosPolicy.put("min_iops", Integer.parseInt((String) params.get("minIops")));
                }
                if (params.get(LATENCY_FIELD) != null) {
                    qosPolicy.put(LATENCY_FIELD, Integer.parseInt((String) params.get(LATENCY_FIELD)));
                }
            } else {
                if (params.get("maxBandwidth") != null) {
                    qosPolicy.put("max_bandwidth", Integer.parseInt((String) params.get("maxBandwidth")));
                }
                if (params.get("maxIops") != null) {
                    qosPolicy.put("max_iops", Integer.parseInt((String) params.get("maxIops")));
                }
                if (params.get("minBandwidth") != null) {
                    qosPolicy.put("min_bandwidth", Integer.parseInt((String) params.get("minBandwidth")));
                }
                if (params.get("minIops") != null) {
                    qosPolicy.put("min_iops", Integer.parseInt((String) params.get("minIops")));
                }
                if (params.get(LATENCY_FIELD) != null) {
                    qosPolicy.put(LATENCY_FIELD, Integer.parseInt((String) params.get(LATENCY_FIELD)));
                }
            }
            if (qosPolicy != null && qosPolicy.size() != 0) {
                if (enabled) {
                    qosPolicy.put("enabled", enabled);
                }
                param.put("qos_policy", qosPolicy);
            }
        }
    }

    private void parseAutoSizeEnable(Object autoSizeEnable, Map<String, Object> params, Map<String, Object> param)
        throws DmeException {
        Map<String, Object> filesystemDetail = getFilesystemDetail((String) params.get("fileSystemId"));
        Map<String, Object> capacityAutonegotiationMap =
            (Map<String, Object>) filesystemDetail.get("capacity_auto_negotiation");
        Map<String, Object> capacityAutonegotiation = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        if (autoSizeEnable != null) {
            String capacitymode = (Boolean) autoSizeEnable
                ? CapacityAutonegotiation.CAPACITY_MODE_AUTO
                : CapacityAutonegotiation.CAPACITY_MODE_OFF;
            if (!"grow-off".equalsIgnoreCase(capacitymode)) {
                capacityAutonegotiation.put(AUTO_SIZE_ENABLE_FIELD, params.get(AUTO_SIZE_ENABLE_REQUEST_FIELD));
                capacityAutonegotiation
                    .put("capacity_recycle_mode", capacityAutonegotiationMap.get("capacity_recycle_mode"));
                capacityAutonegotiation
                    .put("auto_grow_threshold_percent", capacityAutonegotiationMap.get("auto_grow_threshold_percent"));
                capacityAutonegotiation.put("auto_shrink_threshold_percent",
                    capacityAutonegotiationMap.get("auto_shrink_threshold_percent"));
                capacityAutonegotiation.put("max_auto_size", capacityAutonegotiationMap.get("max_auto_size"));
                capacityAutonegotiation.put("min_auto_size", capacityAutonegotiationMap.get("min_auto_size"));
                capacityAutonegotiation
                    .put("auto_size_increment", capacityAutonegotiationMap.get("auto_size_increment"));
            }
            capacityAutonegotiation.put(ADJUSTING_MODE_FIELD, capacitymode);
            param.put("capacity", filesystemDetail.get("capacity"));
            param.put("capacity_autonegotiation", capacityAutonegotiation);
        }
    }

    private void parseCreateNfsParams(Map<String, Object> requestParams,Map<String, Object> targetParams)
        throws DmeException {
        Map<String, Object> createNfsShareParam = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        String storagId = (String) requestParams.get("storagId");
        targetParams.put("storage_id", storagId);
        targetParams.put("storage_pool_id", requestParams.get("storagePoolId"));
        targetParams.put("pool_raw_id", requestParams.get("poolRawId"));
        targetParams.put("current_port_id", requestParams.get("currentPortId"));
        String accessModeDme = (String) requestParams.get("accessMode");
        if ("readWrite".equals(accessModeDme)) {
            accessModeDme = "read/write";
        } else {
            accessModeDme = "read-only";
        }
        targetParams.put("accessMode", requestParams.get("accessMode"));
        targetParams.put("accessModeDme", accessModeDme);
        Object nfsName = requestParams.get(NFS_NAME_FIELD);
        targetParams.put(NFS_NAME_FIELD, nfsName);
        targetParams.put("type", requestParams.get("type"));
        targetParams.put("securityType", requestParams.get("securityType"));
        Map<String, Object> filesystemSpec = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        filesystemSpec.put("capacity", requestParams.get("size"));
        filesystemSpec.put("count", 1);
        boolean sameName = (Boolean) requestParams.get("sameName");
        if (sameName) {
            createNfsShareParam.put(NAME_FIELD, FILE_SEPARATOR + nfsName);
            createNfsShareParam.put("share_path", FILE_SEPARATOR + nfsName + FILE_SEPARATOR);
            filesystemSpec.put(NAME_FIELD, nfsName);
            targetParams.put("exportPath", FILE_SEPARATOR + nfsName);
        } else {
            createNfsShareParam.put(NAME_FIELD, FILE_SEPARATOR + requestParams.get("shareName"));
            createNfsShareParam
                .put("share_path", FILE_SEPARATOR + requestParams.get(FSNAME_FIELD) + FILE_SEPARATOR);
            filesystemSpec.put(NAME_FIELD, requestParams.get(FSNAME_FIELD));
            targetParams.put("exportPath", FILE_SEPARATOR + requestParams.get(FSNAME_FIELD));
        }
        List<Map<String, Object>> filesystemSpecs = new ArrayList<>(DmeConstants.COLLECTION_CAPACITY_16);
        filesystemSpecs.add(filesystemSpec);
        targetParams.put("filesystem_specs", filesystemSpecs);
        Map<String, Object> nfsShareClientAddition = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        nfsShareClientAddition.put(NAME_FIELD, requestParams.get("vkernelIp"));
        nfsShareClientAddition.put("objectId", requestParams.get("hostObjectId"));
        createNfsShareParam.put("character_encoding", requestParams.get("characterEncoding"));
        targetParams.put("create_nfs_share_param", createNfsShareParam);
        boolean advance = (Boolean) requestParams.get("advance");
        List<Map<String, Object>> nfsShareClientAdditions = new ArrayList<>(DmeConstants.COLLECTION_CAPACITY_16);
        nfsShareClientAdditions.add(nfsShareClientAddition);
        targetParams.put("nfs_share_client_addition", nfsShareClientAdditions);
        advanceExcute(requestParams, targetParams, advance, isDorado(storagId));
    }

    private void parseUpdateNfsParams(Map<String, Object> params,Map<String, Object> param) throws DmeException {
        param.put("dataStoreObjectId", params.get("dataStoreObjectId"));
        param.put(NFS_NAME_FIELD, params.get(NFS_NAME_FIELD));
        param.put("file_system_id", params.get("fileSystemId"));
        param.put("nfs_share_id", params.get("shareId"));
        param.put("name", params.get("fsName"));
        parseAutoSizeEnable(params.get(AUTO_SIZE_ENABLE_REQUEST_FIELD), params, param);
        Map<String, Object> tuning = new HashMap<>(DmeConstants.COLLECTION_CAPACITY_16);
        String deviceId = ToolUtils.getStr(params.get("deviceId"));
        if (!StringUtils.isEmpty(deviceId)) {
            StorageTypeShow storageTypeShow = isDorado(deviceId);
            if (storageTypeShow.getDeduplicationShow() || storageTypeShow.getCompressionShow()) {
                tuning.put(DEDUPLICATION_ENABLED_FIELD, params.get("deduplicationEnabled"));
                tuning.put(COMPRESSION_ENABLED_FIELD, params.get("compressionEnabled"));
            }
        }
        Object thin = params.get(THIN_FIELD);
        if (thin != null) {
            if ((Boolean) thin) {
                tuning.put(ALLOCATION_TYPE_FIELD, THIN_FIELD);
            } else {
                tuning.put(ALLOCATION_TYPE_FIELD, "thick");
            }
            param.put("tuning", tuning);
        }
        parseQos((boolean) params.get("qosFlag"), params, param, true);
    }

    private Map<String, Object> getFilesystemDetail(String fsId) throws DmeException {
        return dmeStorageService.getFileSystemDetail(fsId);
    }

}