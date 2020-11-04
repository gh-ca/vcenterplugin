package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author Administrator
 **/
public class VolumeListRestponse {
    private List<Volume> volumeList;
    private int count;

    public List<Volume> getVolumeList() {
        return volumeList;
    }

    public void setVolumeList(List<Volume> volumeList) {
        this.volumeList = volumeList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
