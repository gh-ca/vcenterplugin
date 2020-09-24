package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.BestPracticeCheckRecordBean;
import com.dmeplugin.dmestore.model.NfsDataInfo;
import com.dmeplugin.dmestore.model.Storage;
import com.dmeplugin.dmestore.model.VmfsDataInfo;

import java.util.*;

public class OverviewServiceImpl implements OverviewService {

    private VmfsAccessService vmfsAccessService;
    private DmeNFSAccessService dmeNFSAccessService;
    private DmeStorageService dmeStorageService;
    private BestPracticeProcessService bestPracticeProcessService;

    @Override
    public Map<String, Object> getStorageNum() {
        Map<String, Object> r = new HashMap<>();
        int normal = 0;
        int abnormal = 0;
        int total;
        Map<String, Object> storageOriginal = dmeStorageService.getStorages();
        if (null == storageOriginal || !storageOriginal.get("code").toString().equals("200")) {
            throw new RuntimeException("query storage error.");
        } else{
            List<Storage> storages = (List<Storage>) storageOriginal.get("data");
            total = storages.size();
            for (Storage storage : storages) {
                // 运行状态 0-离线 1-正常 2-故障 9-未管理。
                if ("1".equals(storage.getStatus())) {
                    normal++;
                } else{
                    abnormal++;
                }
            }
            r.put("total", total);
            r.put("normal", normal);
            r.put("abnormal", abnormal);
        }
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
            if ("1".equals(type)){
                ds = computeVMFsDataStoreCapacity();
            } else if ("2".equals(type)){
                ds = computeNFSDataStoreCapacity();
            } else {
                ds = new double[4];
                double[] ds1 = computeVMFsDataStoreCapacity();
                double[] ds2 = computeNFSDataStoreCapacity();
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
            if ("1".equals(type)){
                r = getVMFSInfos();
            } else if ("2".equals(type)){
                r = getNFSInfos();
            } else {
                r = getVMFSInfos();
                r.addAll(getNFSInfos());
            }
            r.sort(Comparator.comparing(o->(double)o.get("utilization")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return r;
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
        Map<String, Object> r = new HashMap<>();
        try {
            int critical = 0;
            int major = 0;
            int warning = 0;
            int info = 0;
            List<BestPracticeCheckRecordBean> rs = bestPracticeProcessService.getCheckRecord();
            for (BestPracticeCheckRecordBean b : rs){
                if ("Critical".equals(b.getLevel())){
                    critical++;
                } else if ("Major".equals(b.getLevel())){
                    major++;
                } else if ("Warning".equals(b.getLevel())){
                    warning++;
                } else if ("Info".equals(b.getLevel())){
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

    private double[] computeVMFsDataStoreCapacity() throws Exception{
        double[] ds = new double[4];
        double totalCapacity = 0;
        double freeCapacity = 0;
        double usedCapacity;
        double utilization;
        List<VmfsDataInfo> vmfsDataInfos = vmfsAccessService.listVmfs();
        if (vmfsDataInfos != null){
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

    private double[] computeNFSDataStoreCapacity() throws Exception{
        double[] ds = new double[4];
        double totalCapacity = 0;
        double freeCapacity = 0;
        double usedCapacity;
        double utilization;
        List<NfsDataInfo> nfsDataInfos = dmeNFSAccessService.listNfs();
        if (nfsDataInfos != null){
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

    private List<Map<String, Object>> getVMFSInfos() throws Exception {
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

    private List<Map<String, Object>> getNFSInfos() throws Exception {
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
