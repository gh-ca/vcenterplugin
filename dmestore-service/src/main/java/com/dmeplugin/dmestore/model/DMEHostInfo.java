package com.dmeplugin.dmestore.model;

/**
 * @author wangxiangyong
 **/
public class DMEHostInfo {
    private String	managed_status;
    private String	hostGroups;
    private String	ip;
    private String	overall_status;
    private String	initiator_count;
    private String	type;
    private String	access_mode;
    private String	sync_to_storage;
    private String	name;
    private String	id;
    private String	os_status;
    private String	display_status;


    public String getManaged_status() {
        return managed_status;
    }

    public void setManaged_status(String managed_status) {
        this.managed_status = managed_status;
    }

    public String getHostGroups() {
        return hostGroups;
    }

    public void setHostGroups(String hostGroups) {
        this.hostGroups = hostGroups;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOverall_status() {
        return overall_status;
    }

    public void setOverall_status(String overall_status) {
        this.overall_status = overall_status;
    }

    public String getInitiator_count() {
        return initiator_count;
    }

    public void setInitiator_count(String initiator_count) {
        this.initiator_count = initiator_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccess_mode() {
        return access_mode;
    }

    public void setAccess_mode(String access_mode) {
        this.access_mode = access_mode;
    }

    public String getSync_to_storage() {
        return sync_to_storage;
    }

    public void setSync_to_storage(String sync_to_storage) {
        this.sync_to_storage = sync_to_storage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOs_status() {
        return os_status;
    }

    public void setOs_status(String os_status) {
        this.os_status = os_status;
    }

    public String getDisplay_status() {
        return display_status;
    }

    public void setDisplay_status(String display_status) {
        this.display_status = display_status;
    }
}
