package com.huawei.dmestore.utils;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className JsonUtilTest
 * @description TODO
 * @date 2020/12/1 20:15
 */
public class JsonUtilTest {

    @InjectMocks
    JsonUtil jsonUtil = new JsonUtil();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void readAsMap() throws IOException {
        String data = " {\n" +
            "    \"id\": \"b94bff9d-0dfb-11eb-bd3d-0050568491c9\",\n" +
            "    \"name\": \"Huawei.Storage\",\n" +
            "    \"ip\": \"10.143.133.201\",\n" +
            "    \"status\": \"1\",\n" +
            "    \"synStatus\": \"2\",\n" +
            "    \"sn\": \"2102351QLH9WK5800028\",\n" +
            "    \"vendor\": \"Huawei\",\n" +
            "    \"model\": \"5300 V5\",\n" +
            "    \"usedCapacity\": 297.5625,\n" +
            "    \"totalCapacity\": 6542.693389892578,\n" +
            "    \"totalEffectiveCapacity\": 3181,\n" +
            "    \"freeEffectiveCapacity\": 2883.44,\n" +
            "    \"subscriptionCapacity\": 309.57,\n" +
            "    \"protectionCapacity\": 0,\n" +
            "    \"fileCapacity\": 77.5,\n" +
            "    \"blockCapacity\": 220.06,\n" +
            "    \"dedupedCapacity\": 0,\n" +
            "    \"compressedCapacity\": 0,\n" +
            "    \"optimizeCapacity\": 297.5625,\n" +
            "    \"azIds\": [],\n" +
            "    \"storagePool\": null,\n" +
            "    \"volume\": null,\n" +
            "    \"fileSystem\": null,\n" +
            "    \"dTrees\": null,\n" +
            "    \"nfsShares\": null,\n" +
            "    \"bandPorts\": null,\n" +
            "    \"logicPorts\": null,\n" +
            "    \"storageControllers\": null,\n" +
            "    \"storageDisks\": null,\n" +
            "    \"productVersion\": \"V500R007C10\",\n" +
            "    \"warning\": null,\n" +
            "    \"event\": null,\n" +
            "    \"location\": \"\",\n" +
            "    \"patchVersion\": \"SPH013\",\n" +
            "    \"maintenanceStart\": null,\n" +
            "    \"maintenanceOvertime\": null\n" +
            "  }";
        jsonUtil.readAsMap(data);

    }
}