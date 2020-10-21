package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;

import java.util.List;
import java.util.Map;

public interface OverviewService {

    /**
     * get storage device num from dme(total,normal,abnormal)
     * @return
     * data like {"total": 100,"normal": 90,"abnormal", 10}
     */
    Map<String, Object> getStorageNum() throws DMEException;


    /**
     * get dataStorage overview
     * @param type 0 :VMFS and NFS, 1:VMFS, 2:NFS
     * @return
     * data like {
     *             "totalCapacity": 100,
     *             "usedCapacity": 20,
     *             "freeCapacity": 80,
     *             "utilization": 20,
     *             "capacityUnit": "TB"
     *           }
     */
    Map<String, Object> getDataStoreCapacitySummary(String type);


    /**
     * get dataStorage overview
     * @param type 0 :VMFS and NFS, 1:VMFS, 2:NFS
     * @param topn top n
     * @return
     * data like [{
     *             "id": "5BDCCE7C4DF74E47AA3F042ED95D60909290",
     *             "name": "dataStore1"
     *             "totalCapacity": 100,
     *             "usedCapacity": 20,
     *             "freeCapacity": 80,
     *             "utilization": 20,
     *             "capacityUnit": "TB"
     *           }]
     */
    List<Map<String, Object>> getDataStoreCapacityTopN(String type, int topn);

    /**
     * get best practice violations
     * @return {critical : 5,
     *        major: 2,
     *        warning: 3,
     *        info: 44}
     */
    Map<String, Object> getBestPracticeViolations();
}
