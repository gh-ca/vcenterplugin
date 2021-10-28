package com.huawei.dmestore.services.bestpractice;

import static com.huawei.dmestore.utils.ToolUtils.GI;

import com.huawei.dmestore.dao.BestPracticeCheckDao;
import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.model.BestPracticeRecommand;
import com.huawei.dmestore.services.VmfsOperationService;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.mo.DatastoreMo;
import com.huawei.vmware.util.DatastoreVmwareMoFactory;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.ManagedObjectReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Vmfs6AutoReclaimImpl
 *
 * @author wangxiangyong
 * @since 2020-11-30
 **/
public class VmfsDatastoreSpaceUtilizationImpl extends BaseBestPracticeService implements BestPracticeService {
    private final Logger logger = LoggerFactory.getLogger(BaseBestPracticeService.class);

    private final Map<String, DatastoreSummary> CAPACITY_MAP = new ConcurrentHashMap<>();

    private String DEFAULT_RECOMMAND_VALUE = "80%";

    private VmfsOperationService vmfsOperationService;

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private BestPracticeCheckDao bestPracticeCheckDao;

    public void setVmfsOperationService(VmfsOperationService vmfsOperationService) {
        this.vmfsOperationService = vmfsOperationService;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    public void setBestPracticeCheckDao(BestPracticeCheckDao bestPracticeCheckDao) {
        this.bestPracticeCheckDao = bestPracticeCheckDao;
    }

    @Override
    public String getHostSetting() {
        return "VMFS Datastore Space Utilization";
    }

    @Override
    public Object getRecommendValue() {
        //通过查询数据库获取，默认为80%
        try {
            BestPracticeRecommand bestPracticeRecommand = bestPracticeCheckDao.getRecommand(getHostSetting(),
                DEFAULT_RECOMMAND_VALUE);
            return bestPracticeRecommand.getRecommandValue();
        } catch (DmeSqlException ex) {
            logger.error("get vmfs recommand value error,return default value 80%!", ex);
        }
        return "80%";
    }

    @Override
    public Object getCurrentValue(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        DatastoreSummary summary = getSummary(vcsdkUtils, objectId);
        double capacity = (double) summary.getCapacity();
        double freeSpace = (double) summary.getFreeSpace();
        double used = capacity - freeSpace;
        double rate = used / capacity * 100;
        String rateStr = new DecimalFormat("#.00").format(rate) + "%";
        return rateStr;
    }

    @Override
    public String getLevel() {
        return "Warning";
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
        DatastoreSummary summary = getSummary(vcsdkUtils, objectId);
        // 不可访问的VMFS存储直接不检查
        if (!summary.isAccessible()) {
            logger.info("存储'{}'不可访问，不进行最佳实践操作！", summary.getName());
            return true;
        }

        // 每次检查的时候刷新缓存，提供给update使用避免重复查询
        CAPACITY_MAP.put(objectId, summary);
        double capacity = (double) summary.getCapacity();
        double freeSpace = (double) summary.getFreeSpace();
        double used = capacity - freeSpace;
        double rate = used / capacity * 100;
        String recommendValue = ((String) getRecommendValue()).split("%")[0];
        // 超过预定值则认为违规
        if (Double.compare(rate, Double.valueOf(recommendValue)) > 0) {
            return false;
        }
        return true;
    }

    @Override
    public void update(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        DatastoreSummary summary = CAPACITY_MAP.get(objectId);
        if (summary == null) {
            summary = getSummary(vcsdkUtils, objectId);
        }
        // 以步长10%进行扩容
        double capacity = summary.getCapacity();
        double freeSpace = summary.getFreeSpace();
        double used = capacity - freeSpace;
        String recommendStr = String.valueOf(getRecommendValue());
        double recommendDouble = new Double(recommendStr.substring(0, recommendStr.indexOf("%"))) / 100;
        double needCapacity = used / recommendDouble;
        double stepCapacity = capacity * 1.1;
        double addCapacityD = capacity * 0.1;
        if (needCapacity > stepCapacity) {
            addCapacityD = (needCapacity - capacity);
            logger.info("{}, A 10 percent growth rate is not enough!Updated The new capacity to {}",
                capacity * 0.1 / GI, addCapacityD / GI);
        }
        double addCapacityGB = addCapacityD / GI;
        int addCapacity = addCapacityGB % 1 == 0 ? (int) addCapacityGB : (int) (addCapacityGB / 1) + 1;
        String volume_id = dmeVmwareRalationDao.getVolumeIdlByStoreId(objectId);

        Map<String, String> volumes = new HashMap<>();
        volumes.put("capacityUnit", "GB");
        volumes.put("obj_id", objectId);
        volumes.put("ds_name", summary.getName());
        volumes.put("vo_add_capacity", Integer.toString(addCapacity));
        volumes.put("volume_id", volume_id);
        vmfsOperationService.expandVmfs2(volumes);
        logger.info("最佳实践VMFS扩容成功！增加容量：{}GB", addCapacity);
    }

    @Override
    public String repairAction() {
        try {
            BestPracticeRecommand bestPracticeRecommand = bestPracticeCheckDao.getRecommand(getHostSetting(),
                DEFAULT_RECOMMAND_VALUE);
            return bestPracticeRecommand.getRepairAction();
        } catch (DmeSqlException ex) {
            logger.error("get vmfs repair action error,return default value 0!", ex);
        }

        return "0";
    }

    private DatastoreSummary getSummary(VCSDKUtils vcsdkUtils, String objectId) throws Exception {
        ManagedObjectReference mor = vcsdkUtils.getVcConnectionHelper().objectId2Mor(objectId);
        VmwareContext context = vcsdkUtils.getVcConnectionHelper().getServerContext(objectId);
        DatastoreMo datastoreMo = DatastoreVmwareMoFactory.getInstance().build(context, mor);
        return datastoreMo.getSummary();
    }

}
