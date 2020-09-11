package com.dmeplugin.vmware;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vise.usersession.UserSessionService;
import com.vmware.vise.vim.data.VimObjectReferenceService;

public abstract class VCConnectionHelper {

    private UserSessionService userSessionService;
    private VimObjectReferenceService vimObjectReferenceService;
    //private ManagedObjectReference mor;

    private String serverurl;
    private String username;
    private String password;

    public UserSessionService getUserSessionService() {
        return userSessionService;
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    public VimObjectReferenceService getVimObjectReferenceService() {
        return vimObjectReferenceService;
    }

    public void setVimObjectReferenceService(VimObjectReferenceService vimObjectReferenceService) {
        this.vimObjectReferenceService = vimObjectReferenceService;
    }

  /*  public ManagedObjectReference getMor() {
        return mor;
    }

    public void setMor(ManagedObjectReference mor) {
        this.mor = mor;
    }*/

    public String getServerurl() {
        return serverurl;
    }

    public void setServerurl(String serverurl) {
        this.serverurl = serverurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取单个context，主要用于前端传入了objectid的情况，类似详情，修改这种，需要将object转换为mor传入到helper使用
     * @return
     * @throws Exception
     */
    public abstract VmwareContext getServerContext(ManagedObjectReference mor) throws Exception;
    /**
     * 获取多个context，主要用于获取所有的主机，datastore这种
     * @return
     * @throws Exception
     */
    public abstract VmwareContext[] getAllContext() throws Exception;

}
