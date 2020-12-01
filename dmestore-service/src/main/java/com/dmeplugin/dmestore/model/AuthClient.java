package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class AuthClient {
    private String accessval;

    private String id;

    private String name;

    private String secure;

    private String sync;

    private String type;

    public String getAccessval() {
        return accessval;
    }

    public void setAccessval(String accessval) {
        this.accessval = accessval;
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

}
