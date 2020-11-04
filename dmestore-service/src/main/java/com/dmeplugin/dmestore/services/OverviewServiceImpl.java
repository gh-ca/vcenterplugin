package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.exception.DMEException;
import com.dmeplugin.dmestore.model.BestPracticeCheckRecordBean;
import com.dmeplugin.dmestore.model.NfsDataInfo;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.model.VmfsDataInfo;

import java.util.*;

/**
 * @author chenjie
 */
public class OverviewServiceImpl implements OverviewService {

    private static final String STORAGE_TYPE_VMFS = "1";
    private static final String STORAGE_TYPE_NFS = "2";

    private static final String SUCCESS_CODE = "200";
    private static final String COLUMN_CODE = "code";

    private static final String STORAGE_DEVICE_STATUS_NORMAL = "1";

    private static final String CRITICAL = "Critical";
    private static final String MAJOR = "Major";
    private static final String WARNING = "Warning";
    private static final String INFO = "Info";

    private VmfsAccessService vmfsAccessService;
    private DmeNFSAccessService dmeNFSAccessService;
    private DmeStorageService dmeStorageService;
    private BestPracticeProcessService bestPracticeProcessService;

    @Override
    public Map<String, Object> getStorageNum() throws DMEException {
        Map<String, Object> r = new HashMap<>(16);
        int normal = 0;
        int abnormal = 0;
        int total;
        List<Storage> storages = dmeStorageService.getStorages();
            total = storages.size();
            for (Storage storage : storages) {
                // 运行状态 0-离线 1-正常 2-故障 9-未管理。
                if (STORAGE_DEVICE_STATUS_NORMAL.equals(storage.getStatus())) {
                    normal++;
                } else{
                    abnormal++;
                }
            }
            r.put("total", total);
            r.put("normal", normal);
            r.put("abnormal", abnormal);
        return r;
    }

    /**
     *
     * @param type 0 :VMFS and NFS, 1:VMFS, 2:NFS
     * @return
     */
    @Override
    public Map<String, Object> getDataStoreCapacitySummary(String type) {
        Map<String, Object> r = new HashMap();
        try {
            double[] ds;
            if (STORAGE_TYPE_VMFS.equals(type)){
                ds = computeVmfsDataStoreCapacity();
            } else if (STORAGE_TYPE_NFS.equals(type)){
                ds = computeNfsDataStoreCapacity();
            } else {
                ds = new double[4];
                double[] ds1 = computeVmfsDataStoreCapacity();
                double[] ds2 = computeNfsDataStoreCapacity();
                ds[0] = ds1[0] + ds2[0];
                ds[1] = ds1[1] + ds2[1];
                ds[2] = ds1[2] + ds2[2];
                ds[3] = ds[2]/(ds[0] == 0.0? 1: ds[0])*100.0;
            }
            r.put("totalCapacity", ds[0]);
            r.put("freeCapacity", ds[1]);
            r.put("usedCapacity", ds[2]);
            r.put("utilization", ds[3]);
            r.put("capacityUnit", "GB");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    /**
     *
     * @param type 0 :VMFS and NFS, 1:VMFS, 2:NFS
     * @param topn top n
     * @return
     */
    @Override
    public List<Map<String, Object>> getDataStoreCapacityTopN(String type, int topn) {
        List<Map<String, Object>> r;
        try {
            if (STORAGE_TYPE_VMFS.equals(type)){
                r = getVmfsInfos();
            } else if (STORAGE_TYPE_NFS.equals(type)){
                r = getNfsInfos();
            } else {
                r = getVmfsInfos();
                r.addAll(getNfsInfos());
            }
            r.sort(Comparator.comparing(o->(double)((Map)o).get("utilization")).reversed());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return r.size() > topn ? r.subList(0, topn) : r;
    }

    /**
     * critical : 5,
     *        major: 2,
     *        warning: 3,
     *        info: 44
     * @return
     */
    @Override
    public Map<String, Object> getBestPracticeViolations() {
        Map<String, Object> r = new HashMap<>(16);
        try {
            int critical = 0;
            int major = 0;
            int warning = 0;
            int info = 0;
            List<BestPracticeCheckRecordBean> rs = bestPracticeProcessService.getCheckRecord();
            for (BestPracticeCheckRecordBean b : rs){
                if (CRITICAL.equals(b.getLevel())){
                    critical++;
                } else if (MAJOR.equals(b.getLevel())){
                    major++;
                } else if (WARNING.equals(b.getLevel())){
                    warning++;
                } else if (INFO.equals(b.getLevel())){
                    info++;
                }

                r.put("critical", critical);
                r.put("major", major);
                r.put("warning", warning);
                r.put("info", info);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return r;
    }

    private double[] computeVmfsDataStoreCapacity() throws Exception{
        double[] ds = new double[4];
        double totalCapacity = 0;
        double freeCapacity = 0;
        double usedCapacity;
        double utilization;
        List<VmfsDataInfo> vmfsDataInfos = vmfsAccessService.listVmfs();
        if (vmfsDataInfos != null && vmfsDataInfos.size() > 0){
            for (VmfsDataInfo vmfsDataInfo : vmfsDataInfos){
                totalCapacity += vmfsDataInfo.getCapacity();
                freeCapacity += vmfsDataInfo.getFreeSpace();
            }
            usedCapacity = totalCapacity - freeCapacity;
            utilization = usedCapacity/totalCapacity*100.0;
            ds[0] = totalCapacity;
            ds[1] = freeCapacity;
            ds[2] = usedCapacity;
            ds[3] = utilization;
        }

        return ds;
    }

    private double[] computeNfsDataStoreCapacity() throws Exception{
        double[] ds = new double[4];
        double totalCapacity = 0;
        double freeCapacity = 0;
        double usedCapacity;
        double utilization;
        List<NfsDataInfo> nfsDataInfos = dmeNFSAccessService.listNfs();
        if (nfsDataInfos != null && nfsDataInfos.size() > 0){
            for (NfsDataInfo nfsDataInfo : nfsDataInfos){
                totalCapacity += nfsDataInfo.getCapacity();
                freeCapacity += nfsDataInfo.getFreeSpace();
            }
            usedCapacity = totalCapacity - freeCapacity;
            utilization = usedCapacity/totalCapacity*100.0;
            ds[0] = totalCapacity;
            ds[1] = freeCapacity;
            ds[2] = usedCapacity;
            ds[3] = utilization;
        }

        return ds;
    }

    private List<Map<String, Object>> getVmfsInfos() throws Exception {
        List<Map<String, Object>> r = new ArrayList<>();
        List<VmfsDataInfo> vmfsDataInfos = vmfsAccessService.listVmfs();
        if (vmfsDataInfos != null){
            for (VmfsDataInfo vmfsDataInfo : vmfsDataInfos){
                Map<String, Object> t = new HashMap();
                t.put("id", vmfsDataInfo.getDeviceId());
                t.put("name", vmfsDataInfo.getName());
                t.put("freeCapacity", vmfsDataInfo.getFreeSpace());
                t.put("totalCapacity", vmfsDataInfo.getCapacity());
                double usedCapacity = vmfsDataInfo.getCapacity() - vmfsDataInfo.getFreeSpace();
                t.put("usedCapacity", usedCapacity);
                t.put("utilization", usedCapacity/vmfsDataInfo.getCapacity()*100.0);
                t.put("capacityUnit", "GB");

                r.add(t);
            }
        }

        return r;
    }

    private List<Map<String, Object>> getNfsInfos() throws Exception {
        List<Map<String, Object>> r = new ArrayList<>();
        List<NfsDataInfo> nfsDataInfos = dmeNFSAccessService.listNfs();
        if (nfsDataInfos != null){
            for (NfsDataInfo nfsDataInfo : nfsDataInfos){
                Map<String, Object> t = new HashMap();
                double usedCapacity = nfsDataInfo.getCapacity() - nfsDataInfo.getFreeSpace();
                t.put("id", nfsDataInfo.getDeviceId());
                t.put("name", nfsDataInfo.getName());
                t.put("totalCapacity", nfsDataInfo.getCapacity());
                t.put("freeCapacity", nfsDataInfo.getFreeSpace());
                t.put("usedCapacity", usedCapacity);
                t.put("utilization", usedCapacity/nfsDataInfo.getCapacity()*100.0);
                t.put("capacityUnit", "GB");

                r.add(t);
            }
        }

        return r;
    }

    public VmfsAccessService getVmfsAccessService() {
        return vmfsAccessService;
    }

    public void setVmfsAccessService(VmfsAccessService vmfsAccessService) {
        this.vmfsAccessService = vmfsAccessService;
    }

    public DmeNFSAccessService getDmeNFSAccessService() {
        return dmeNFSAccessService;
    }

    public void setDmeNFSAccessService(DmeNFSAccessService dmeNFSAccessService) {
        this.dmeNFSAccessService = dmeNFSAccessService;
    }

    public DmeStorageService getDmeStorageService() {
        return dmeStorageService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public BestPracticeProcessService getBestPracticeProcessService() {
        return bestPracticeProcessService;
    }

    public void setBestPracticeProcessService(BestPracticeProcessService bestPracticeProcessService) {
        this.bestPracticeProcessService = bestPracticeProcessService;
    }
}
