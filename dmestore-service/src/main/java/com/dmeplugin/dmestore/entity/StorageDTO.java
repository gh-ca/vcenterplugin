package com.dmeplugin.dmestore.entity;

import java.util.ArrayList;
import java.util.List;

public class StorageDTO  {

    private Storage storage;
    private List<StoragePool> storagePools = new ArrayList<>();
    private List<Dtree> dtrees = new ArrayList<>();
    private List<FileSystem> fileSystems = new ArrayList<>();
    private List<NfsShare> nfsShares = new ArrayList<>();
    private List<Volume> volumes = new ArrayList<>();

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public List<StoragePool> getStoragePools() {
        return storagePools;
    }

    public void setStoragePools(List<StoragePool> storagePools) {
        this.storagePools = storagePools;
    }

    public List<Dtree> getDtrees() {
        return dtrees;
    }

    public void setDtrees(List<Dtree> dtrees) {
        this.dtrees = dtrees;
    }

    public List<FileSystem> getFileSystems() {
        return fileSystems;
    }

    public void setFileSystems(List<FileSystem> fileSystems) {
        this.fileSystems = fileSystems;
    }

    public List<NfsShare> getNfsShares() {
        return nfsShares;
    }

    public void setNfsShares(List<NfsShare> nfsShares) {
        this.nfsShares = nfsShares;
    }

    public List<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    @Override
    public String toString() {
        return "StorageDTO{" +
                "storage=" + storage +
                ", storagePools=" + storagePools +
                ", dtrees=" + dtrees +
                ", fileSystems=" + fileSystems +
                ", nfsShares=" + nfsShares +
                ", volumes=" + volumes +
                '}';
    }
}
