package com.dmeplugin.dmestore.model;

import java.io.Serializable;
/**
 * @author lianq
 * @ClassName: Volume
 * @Company: GH-CA
 * @create 2020-09-03
 */
public class Volume implements Serializable {


    private String name;
    private String ownerController;
    private String prefetchPolicy;
    private Integer prefetchValue;
    private CustomizeVolume tuning;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerController() {
        return ownerController;
    }

    public void setOwnerController(String ownerController) {
        this.ownerController = ownerController;
    }

    public String getPrefetchPolicy() {
        return prefetchPolicy;
    }

    public void setPrefetchPolicy(String prefetchPolicy) {
        this.prefetchPolicy = prefetchPolicy;
    }

    public Integer getPrefetchValue() {
        return prefetchValue;
    }

    public void setPrefetchValue(Integer prefetchValue) {
        this.prefetchValue = prefetchValue;
    }

    public CustomizeVolume getTuning() {
        return tuning;
    }

    public void setTuning(CustomizeVolume tuning) {
        this.tuning = tuning;
    }

    @Override
    public String toString() {
        return "VolumeInfo{" +
                "name='" + name + '\'' +
                ", ownerController='" + ownerController + '\'' +
                ", prefetchPolicy='" + prefetchPolicy + '\'' +
                ", prefetchValue=" + prefetchValue +
                ", tuning=" + tuning +
                '}';
    }
}
