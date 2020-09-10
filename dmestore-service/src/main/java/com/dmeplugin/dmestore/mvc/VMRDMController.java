package com.dmeplugin.dmestore.mvc;

import com.dmeplugin.dmestore.model.ResponseBodyBean;
import com.dmeplugin.dmestore.model.VmRDMCreateBean;
import com.dmeplugin.dmestore.services.DmeAccessService;
import com.dmeplugin.dmestore.services.TaskService;
import com.dmeplugin.dmestore.services.VMRDMService;
import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.mo.DatacenterMO;
import com.dmeplugin.vmware.mo.DatastoreMO;
import com.dmeplugin.vmware.mo.VirtualMachineMO;
import com.dmeplugin.vmware.util.TestVmwareContextFactory;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vmware.vim25.VirtualDiskMode;
import com.vmware.vim25.VirtualDiskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "v1/vmrdm")
public class VMRDMController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(VMRDMController.class);

    @Autowired
    private VMRDMService vmrdmService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DmeAccessService dmeAccessService;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "createRdm", method = RequestMethod.POST)
    public ResponseBodyBean createRDM(@RequestParam("datacenter_name") String datacenter_name,
                                      @RequestParam("datastore_name") String datastore_name,
                                      @RequestParam("vm_name") String vm_name,
                                      @RequestParam("host_id") String host_id,
                                      @RequestBody VmRDMCreateBean createBean) throws Exception {
        String taskId = vmrdmService.createRDM(createBean);
        if (null == taskId) {
            LOG.error("Failed to create RDM on DME! taskId is null");
            return failure("Failed to create RDM on DME!");
        }
        JsonObject taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (null != taskDetail && taskDetail.get("status").getAsInt() != 3) {
            LOG.error("Failed to create RDM on DME!taskDetail={}", gson.toJson(taskDetail));
            return failure("Failed to create RDM on DME");
        }
        List<String> volumeIds = new ArrayList();
        //获取卷资源
        JsonArray resources = taskDetail.getAsJsonArray("resources");
        for (int i = 0; i < resources.size(); i++) {
            JsonObject resource = resources.get(i).getAsJsonObject();
            String id = resource.get("id").getAsString();
            volumeIds.add(id);
        }
        //将卷映射给主机
        taskId = vmrdmService.hostMapping(host_id, volumeIds);
        if (null == taskId) {
            LOG.error("Disk mapping to host failed! taskId is null");
            return failure("Disk mapping to host failed!");
        }
        taskDetail = taskService.queryTaskByIdUntilFinish(taskId);
        if (null != taskDetail && taskDetail.get("status").getAsInt() == 3) {
            LOG.info("Disk mapping to host succeeded!");
        } else {
            LOG.error("Disk mapping to host failed!taskDetail={}", gson.toJson(taskDetail));
            return failure("Disk mapping to host failed!");
        }

        //查询主机信息
        Map<String, Object> hostMap = dmeAccessService.getDmeHost(host_id);
        String host_ip = hostMap.get("ip").toString();
        String host_name = hostMap.get("name").toString();
        //调用vcenter扫描卷
        VCSDKUtils.hostRescanVmfs(host_ip);
        LOG.info("RescanVmfs successful!");
        //获取卷信息
        String lunsStr = VCSDKUtils.getLunsOnHost(host_name);
        JsonArray lunArray = gson.fromJson(lunsStr, JsonArray.class);
        JsonObject lunObject = null;
        boolean flag = false;
        for (int i = 0; i < lunArray.size(); i++) {
            lunObject = lunArray.get(i).getAsJsonObject();
            LOG.info("Lun deviceName={}, requested name={}", lunObject.get("deviceName").getAsString(), createBean.getName());
            //TODO 根据实际情况优化
            if (lunObject.get("deviceName").getAsString().equals(createBean.getName())) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            LOG.error("not find lun, disk mapping to host failed!");
            return failure("Disk mapping to host failed!");
        }
        //调用Vcenter创建磁盘
        VmwareContext vmwareContext = TestVmwareContextFactory.getContext("10.143.132.248", "administrator@vsphere.local", "Pbu4@123");
        DatacenterMO dcMo = new DatacenterMO(vmwareContext, datacenter_name);
        DatastoreMO dsMo = new DatastoreMO(vmwareContext, dcMo.findDatastore(datastore_name));
        VirtualMachineMO virtualMachineMO = new VirtualMachineMO(vmwareContext, vm_name);
        String rdmdevicename = lunObject.get("deviceName").getAsString();
        String vmdkDatastorePath = dsMo.getDatastorePath(datastore_name);
        int sizeInMb = createBean.getSize();
        try {
            virtualMachineMO.createDisk(vmdkDatastorePath, VirtualDiskType.RDM, VirtualDiskMode.PERSISTENT,
                    rdmdevicename, sizeInMb, dsMo.getMor(), -1);
        }catch (Exception ex){
            LOG.error("create vcenter disk rdm failed!errorMsg:{}", ex.getMessage());
            return failure("Create Vcenter disk RDM failed");
        }

        return success();
    }
}
