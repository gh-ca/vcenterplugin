package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMO;
import com.huawei.vmware.mo.HostNetworkSystemMO;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.HostConfigChangeMode;
import com.vmware.vim25.HostNetworkConfig;
import com.vmware.vim25.HostPortGroupConfig;
import com.vmware.vim25.HostVirtualNicConfig;
import com.vmware.vim25.HostVirtualNicSpec;
import com.vmware.vim25.HostVirtualSwitch;
import com.vmware.vim25.HostVirtualSwitchConfig;
import com.vmware.vim25.HostVirtualSwitchSpec;
import com.vmware.vim25.ManagedObjectReference;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JumboFrameMTUImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class JumboFrameMTUImpl extends BaseBestPracticeService implements BestPracticeService {
    private static final int RECOMMEND_VALUE = 9000;

    private static final String MTU_NULL = "--";

    @Override
    public String getHostSetting() {
        return "Jumbo Frame (MTU)";
    }

    @Override
    public Integer getRecommendValue() {
        return RECOMMEND_VALUE;
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        HostNetworkConfig networkConfig = hostMo.getHostNetworkSystemMo().getNetworkConfig();
        List<HostVirtualNicConfig> list = networkConfig.getVnic();
        for (HostVirtualNicConfig config : list) {
            HostVirtualNicSpec spec = config.getSpec();
            Integer mtu = spec.getMtu();
            if (null == mtu || (mtu.intValue() != getRecommendValue().intValue())) {
                return null == mtu ? MTU_NULL : mtu.intValue();
            }
        }

        return MTU_NULL;
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
        return check(vcsdkUtils, objectId, getRecommendValue());
    }

    private boolean check(VCSDKUtils vcsdkUtils, String objectId, Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        HostNetworkConfig networkConfig = hostMo.getHostNetworkSystemMo().getNetworkConfig();
        if (networkConfig == null) {
            return true;
        }
        List<HostVirtualNicConfig> list = networkConfig.getVnic();
        if (list == null || list.size() == 0) {
            return true;
        }
        for (HostVirtualNicConfig config : list) {
            HostVirtualNicSpec spec = config.getSpec();
            Integer mtu = spec.getMtu();
            if (null == mtu || (mtu.intValue() != ((Integer) recommendValue).intValue())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        update(vcsdkUtils, objectId, getRecommendValue());
    }

    private void update(VCSDKUtils vcsdkUtils, String objectId, Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        if (check(vcsdkUtils, objectId, recommendValue)) {
            return;
        }
        HostMO hostMo = this.getHostMoFactory().build(context, mor);
        HostNetworkSystemMO hostNetworkSystemMo = hostMo.getHostNetworkSystemMo();
        HostNetworkConfig networkConfig = hostNetworkSystemMo.getNetworkConfig();
        List<HostVirtualNicConfig> list = networkConfig.getVnic();
        ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(list.size());
        for (HostVirtualNicConfig config : list) {
            HostVirtualNicSpec spec = config.getSpec();
            String device = config.getDevice();
            Integer mtu = spec.getMtu();
            if (null == mtu || (mtu.intValue() != ((Integer) recommendValue).intValue())) {
                singleThreadExecutor.execute(() -> {
                    spec.setMtu((Integer) recommendValue);
                    try {
                        hostNetworkSystemMo.updateVirtualNic(device, spec);
                    } catch (Exception exception) {
                        LOGGER.error("updateVirtualNic error!hostObjectId={},device={}", objectId, device);
                        exception.printStackTrace();
                    }
                });
            }
        }
    }
}