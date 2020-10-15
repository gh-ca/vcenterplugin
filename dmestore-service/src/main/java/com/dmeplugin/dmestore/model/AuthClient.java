package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class AuthClient {
    private String accessval;
    private String all_squash;
    private String id;
    private String name;
    private String parent_id;
    private String root_squash;
    private String secure;
    private String sync;
    private String type;
    private String vstore_id_in_storage;
    private String vstore_name;

    public String getAccessval() {
        return accessval;
    }

    public void setAccessval(String accessval) {
        this.accessval = accessval;
    }

    public String getAll_squash() {
        return all_squash;
    }

    public void setAll_squash(String all_squash) {
        this.all_squash = all_squash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getRoot_squash() {
        return root_squash;
    }

    public void setRoot_squash(String root_squash) {
        this.root_squash = root_squash;
    }

    public String getSecure() {
        return secure;
    }

    public void setSecure(String secure) {
        this.secure = secure;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVstore_id_in_storage() {
        return vstore_id_in_storage;
    }

    public void setVstore_id_in_storage(String vstore_id_in_storage) {
        this.vstore_id_in_storage = vstore_id_in_storage;
    }

    public String getVstore_name() {
        return vstore_name;
    }

    public void setVstore_name(String vstore_name) {
        this.vstore_name = vstore_name;
    }
}
