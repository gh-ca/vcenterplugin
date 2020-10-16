package com.dmeplugin.dmestore.model;

import java.util.List;

/**
 * @author wangxiangyong
 **/
public class NfsDataStoreShareAttr {
    private String fs_name;
    private String name;
    private String share_path;
    private String description;
    private String owning_dtree_id;
    private String owning_dtree_name;
    private String device_name;
    private List<AuthClient> auth_client_list;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFs_name() {
        return fs_name;
    }

    public void setFs_name(String fs_name) {
        this.fs_name = fs_name;
    }

    public String getShare_path() {
        return share_path;
    }

    public void setShare_path(String share_path) {
        this.share_path = share_path;
    }

    public String getOwning_dtree_id() {
        return owning_dtree_id;
    }

    public void setOwning_dtree_id(String owning_dtree_id) {
        this.owning_dtree_id = owning_dtree_id;
    }

    public String getOwning_dtree_name() {
        return owning_dtree_name;
    }

    public void setOwning_dtree_name(String owning_dtree_name) {
        this.owning_dtree_name = owning_dtree_name;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AuthClient> getAuth_client_list() {
        return auth_client_list;
    }

    public void setAuth_client_list(List<AuthClient> auth_client_list) {
        this.auth_client_list = auth_client_list;
    }
}
