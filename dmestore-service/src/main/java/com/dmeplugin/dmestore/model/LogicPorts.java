package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className LogicPort
 * @description TODO
 * @date 2020/9/7 15:40
 */
public class LogicPorts {
    private String id;
    private String name;
    private String running_status;
    private String operational_status;
    private String mgmt_ip;
    private String mgmt_ipv6;
    private String home_port_id;
    private String home_port_name;
    private String current_port_id;
    private String current_port_name;
    private String role;
    private String ddns_status;
    private String support_protocol;
    private String management_access;
    private String vstore_id;
    private String vstore_name;

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

    public String getRunning_status() {
        return running_status;
    }

    public void setRunning_status(String running_status) {
        this.running_status = running_status;
    }

    public String getOperational_status() {
        return operational_status;
    }

    public void setOperational_status(String operational_status) {
        this.operational_status = operational_status;
    }

    public String getMgmt_ip() {
        return mgmt_ip;
    }

    public void setMgmt_ip(String mgmt_ip) {
        this.mgmt_ip = mgmt_ip;
    }

    public String getMgmt_ipv6() {
        return mgmt_ipv6;
    }

    public void setMgmt_ipv6(String mgmt_ipv6) {
        this.mgmt_ipv6 = mgmt_ipv6;
    }

    public String getHome_port_id() {
        return home_port_id;
    }

    public void setHome_port_id(String home_port_id) {
        this.home_port_id = home_port_id;
    }

    public String getHome_port_name() {
        return home_port_name;
    }

    public void setHome_port_name(String home_port_name) {
        this.home_port_name = home_port_name;
    }

    public String getCurrent_port_id() {
        return current_port_id;
    }

    public void setCurrent_port_id(String current_port_id) {
        this.current_port_id = current_port_id;
    }

    public String getCurrent_port_name() {
        return current_port_name;
    }

    public void setCurrent_port_name(String current_port_name) {
        this.current_port_name = current_port_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDdns_status() {
        return ddns_status;
    }

    public void setDdns_status(String ddns_status) {
        this.ddns_status = ddns_status;
    }

    public String getSupport_protocol() {
        return support_protocol;
    }

    public void setSupport_protocol(String support_protocol) {
        this.support_protocol = support_protocol;
    }

    public String getManagement_access() {
        return management_access;
    }

    public void setManagement_access(String management_access) {
        this.management_access = management_access;
    }

    public String getVstore_id() {
        return vstore_id;
    }

    public void setVstore_id(String vstore_id) {
        this.vstore_id = vstore_id;
    }

    public String getVstore_name() {
        return vstore_name;
    }

    public void setVstore_name(String vstore_name) {
        this.vstore_name = vstore_name;
    }
}
