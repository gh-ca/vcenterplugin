package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.mo.DatastoreMO;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vmware.vim25.ManagedObjectReference;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class NumberOfVolumesInDatastoreImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "Number of volumes in Datastore";
    }

    @Override
    public Object getRecommendValue() {
        return 1;
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = hostMo.getDatastoreMountsOnHost();
        int volumeCount = 0;
        JsonArray array = new JsonArray();
        for (Pair<ManagedObjectReference, String> pair : datastoreMountsOnHost) {
            ManagedObjectReference dsMor = pair.first();
            DatastoreMO datastoreMo = new DatastoreMO(context, dsMor);
            String dataStoreName = datastoreMo.getSummary().getName();
            int volumeSize = datastoreMo.getVmfsDatastoreInfo().getVmfs().getExtent().size();
            JsonObject object = new JsonObject();
            object.addProperty("dataStoreName", dataStoreName);
            object.addProperty("volumeSize", volumeSize);
            volumeCount = volumeCount + volumeSize;

            array.add(object);
        }

        JsonObject reObject = new JsonObject();
        reObject.addProperty("volumeCount", volumeCount);
        reObject.add("dataStores", array);
        return reObject.toString();
    }

    @Override
    public String getLevel() {
        return "Info";
    }

    @Override
    public boolean needReboot() {
        return false;
    }

    @Override
    public boolean autoRepair() {
        return true;
    }

    @Override
    public boolean check(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = hostMo.getDatastoreMountsOnHost();
        for (Pair<ManagedObjectReference, String> pair : datastoreMountsOnHost) {
            ManagedObjectReference dsMor = pair.first();
            DatastoreMO datastoreMo = new DatastoreMO(context, dsMor);
            int volumeSize = datastoreMo.getVmfsDatastoreInfo().getVmfs().getExtent().size();
            //只要有一个存储对应的卷超过推荐值就认为该主机不满足最佳实践
            if (volumeSize > Integer.parseInt(getRecommendValue().toString())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        //不可自动修复，无需处理
    }
}
