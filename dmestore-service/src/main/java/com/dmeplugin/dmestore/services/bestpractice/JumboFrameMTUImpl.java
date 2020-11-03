package com.dmeplugin.dmestore.services.bestpractice;

import com.dmeplugin.dmestore.utils.VCSDKUtils;
import com.dmeplugin.vmware.mo.HostMO;
import com.dmeplugin.vmware.mo.HostNetworkSystemMO;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.*;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class JumboFrameMTUImpl extends BaseBestPracticeService implements BestPracticeService {
    @Override
    public String getHostSetting() {
        return "Jumbo Frame (MTU)";
    }

    @Override
    public Integer getRecommendValue() {
        return new Integer(9000);
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<HostVirtualSwitch> virtualSwitches = hostMo.getHostNetworkInfo().getVswitch();
        for (HostVirtualSwitch virtualSwitch : virtualSwitches) {
            HostVirtualSwitchSpec spec = virtualSwitch.getSpec();
            Integer mtu = spec.getMtu();
            if (null == mtu || (mtu.intValue() != getRecommendValue().intValue())) {
                return null == mtu ? "--" : mtu.intValue();
            }
        }

        HostNetworkConfig networkConfig = hostMo.getHostNetworkSystemMO().getNetworkConfig();
        List<HostVirtualSwitchConfig> list = networkConfig.getVswitch();
        for (HostVirtualSwitchConfig vSwitchConfig : list) {
            HostVirtualSwitchSpec spec = vSwitchConfig.getSpec();
            Integer mtu = spec.getMtu();
            if (null == mtu || (mtu.intValue() != getRecommendValue().intValue())) {
                return null == mtu ? "--" : mtu.intValue();
            }
        }


        return null;
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

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        update(vcsdkUtils, objectId, getRecommendValue());
    }

    private boolean check(VCSDKUtils vcsdkUtils, String objectId, Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        HostMO hostMo = new HostMO(context, mor);
        List<HostVirtualSwitch> virtualSwitches = hostMo.getHostNetworkInfo().getVswitch();
        for (HostVirtualSwitch virtualSwitch : virtualSwitches) {
            HostVirtualSwitchSpec spec = virtualSwitch.getSpec();
            Integer mtu = spec.getMtu();
            if (null == mtu || (mtu.intValue() != ((Integer) recommendValue).intValue())) {
                return false;
            }
        }

        HostNetworkConfig networkConfig = hostMo.getHostNetworkSystemMO().getNetworkConfig();
        List<HostVirtualSwitchConfig> list = networkConfig.getVswitch();
        for (HostVirtualSwitchConfig vSwitchConfig : list) {
            HostVirtualSwitchSpec spec = vSwitchConfig.getSpec();
            Integer mtu = spec.getMtu();
            if (null == mtu || (mtu.intValue() != ((Integer) recommendValue).intValue())) {
                return false;
            }
        }

        return true;
    }

    private void update(VCSDKUtils vcsdkUtils, String objectId, Object recommendValue) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectID2MOR(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        if (check(vcsdkUtils, objectId, recommendValue)) {
            return;
        }
        HostMO hostMo = new HostMO(context, mor);
        HostNetworkInfo networkInfo = hostMo.getHostNetworkInfo();
        HostNetworkSystemMO hostNetworkSystemMO = hostMo.getHostNetworkSystemMO();
        List<HostVirtualSwitch> virtualSwitches = networkInfo.getVswitch();
        for (HostVirtualSwitch virtualSwitch : virtualSwitches) {
            String name = virtualSwitch.getName();
            HostVirtualSwitchSpec spec = virtualSwitch.getSpec();
            Integer mtu = spec.getMtu();
            if (null == mtu || (mtu.intValue() != ((Integer) recommendValue).intValue())) {
                spec.setMtu((Integer) recommendValue);
                hostNetworkSystemMO.updateVirtualSwitch(name, spec);
            }
        }

        try {
            HostNetworkConfig networkConfig = hostNetworkSystemMO.getNetworkConfig();
            List<HostVirtualSwitchConfig> list = networkConfig.getVswitch();
            for (HostVirtualSwitchConfig vSwitchConfig : list) {
                HostVirtualSwitchSpec spec = vSwitchConfig.getSpec();
                Integer mtu = spec.getMtu();
                if (null == mtu || (mtu.intValue() != ((Integer) recommendValue).intValue())) {
                    spec.setMtu((Integer) recommendValue);
                }
            }
            hostNetworkSystemMO.updateNetworkConfig(networkConfig, HostConfigChangeMode.MODIFY.value());
        } catch (Exception ex) {

        }
    }
}
