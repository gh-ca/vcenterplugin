package com.huawei.dmestore.services.bestpractice;

import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.HostMo;
import com.huawei.vmware.mo.HostNetworkSystemMo;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.HostNetworkConfig;
import com.vmware.vim25.HostVirtualNicConfig;
import com.vmware.vim25.HostVirtualNicSpec;
import com.vmware.vim25.ManagedObjectReference;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * JumboFrameMTUImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class JumboFrameMTUImpl extends BaseBestPracticeService implements BestPracticeService {
    private ThreadPoolTaskExecutor threadPoolExecutor;
    protected final Logger logger = LoggerFactory.getLogger(BaseBestPracticeService.class);

    public static final int RECOMMEND_VALUE = 9000;

    private static final String MTU_NULL = "--";

    public ThreadPoolTaskExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolTaskExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

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
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
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
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
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
        /*if (check(vcsdkUtils, objectId, recommendValue)) {
            return;
        }*/
        HostMo hostMo = this.getHostMoFactory().build(context, mor);
        HostNetworkSystemMo hostNetworkSystemMo = hostMo.getHostNetworkSystemMo();
        HostNetworkConfig networkConfig = hostNetworkSystemMo.getNetworkConfig();
        List<HostVirtualNicConfig> list = networkConfig.getVnic();
        //ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(list.size());
        for (HostVirtualNicConfig config : list) {
            HostVirtualNicSpec spec = config.getSpec();
            String device = config.getDevice();
            Integer mtu = spec.getMtu();
            if (null == mtu || (mtu.intValue() != ((Integer) recommendValue).intValue())) {
                threadPoolExecutor.execute(() -> {
                    spec.setMtu((Integer) recommendValue);
                    try {
                        hostNetworkSystemMo.updateVirtualNic(device, spec);
                    } catch (Exception exception) {
                        logger.error("updateVirtualNic error!hostObjectId={},device={}", objectId, device);
                        exception.printStackTrace();
                    }
                });
            }
        }
    }
}