package com.dmeplugin.dmestore.model;

/**
 * @author lianq
 * @className DtreeList
 * @description TODO
 * @date 2020/9/7 9:42
 */
public class Dtrees {

    private String name;
    //所属文件系统名称
    private String fsName;
    //配额
    private boolean quotaSwitch;
    //安全模式
    private String securityStyle;
    //服务等级名称
    private String tierName;
    //nfs
    private Integer nfsCount;
    private Integer cifsCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFsName() {
        return fsName;
    }

    public void setFsName(String fsName) {
        this.fsName = fsName;
    }

    public boolean isQuotaSwitch() {
        return quotaSwitch;
    }

    public void setQuotaSwitch(boolean quotaSwitch) {
        this.quotaSwitch = quotaSwitch;
    }

    public String getSecurityStyle() {
        return securityStyle;
    }

    public void setSecurityStyle(String securityStyle) {
        this.securityStyle = securityStyle;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public Integer getNfsCount() {
        return nfsCount;
    }

    public void setNfsCount(Integer nfsCount) {
        this.nfsCount = nfsCount;
    }

    public Integer getCifsCount() {
        return cifsCount;
    }

    public void setCifsCount(Integer cifsCount) {
        this.cifsCount = cifsCount;
    }

    @Override
    public String toString() {
        return "Dtrees{" +
            "name='" + name + '\'' +
            ", fsName='" + fsName + '\'' +
            ", quotaSwitch=" + quotaSwitch +
            ", securityStyle='" + securityStyle + '\'' +
            ", tierName='" + tierName + '\'' +
            ", nfsCount=" + nfsCount +
            ", cifsCount=" + cifsCount +
            '}';
    }
}
