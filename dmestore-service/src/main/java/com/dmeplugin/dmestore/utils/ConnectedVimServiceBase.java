package com.dmeplugin.dmestore.utils;

import com.vmware.common.Main;
import com.vmware.common.ssl.TrustAll;
import com.vmware.connection.BasicConnection;
import com.vmware.connection.Connection;
import com.vmware.connection.helpers.GetMOREF;
import com.vmware.connection.helpers.WaitForValues;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public  class ConnectedVimServiceBase {
    public static final String PROP_ME_NAME = "name";
    public static final String SVC_INST_NAME = "ServiceInstance";
    protected Connection connection;
    protected VimPortType vimPort;
    protected ServiceContent serviceContent;
    protected ManagedObjectReference rootRef;
    protected Map headers;
    protected WaitForValues waitForValues;
    protected GetMOREF getMOREFs;
    Boolean hostConnection;

    public ConnectedVimServiceBase() {
        this.hostConnection = Boolean.FALSE;
    }


    public void setHostConnection(Boolean var1) {
        this.hostConnection = var1;
    }

    public ManagedObjectReference getServiceInstanceReference() {
        return this.connection.getServiceInstanceReference();
    }


    public void setConnection(Connection var1) {
        this.connection = var1;
    }


    public Connection connect() {
        this.setIgnoreCert();
        if (this.hostConnection) {
            this.connection = this.basicConnectionFromConnection(this.connection);
        }

        try {
            this.connection.connect();
            this.waitForValues = new WaitForValues(this.connection);
            this.getMOREFs = new GetMOREF(this.connection);
            this.headers = this.connection.getHeaders();
            this.vimPort = this.connection.getVimPort();
            this.serviceContent = this.connection.getServiceContent();
            this.rootRef = this.serviceContent.getRootFolder();
        } catch (ConnectionException var2) {
            var2.printStackTrace();
            System.err.println("No valid connection available. Exiting now.");
            System.exit(0);
        }

        return this.connection;
    }

    public BasicConnection basicConnectionFromConnection(Connection var1) {
        BasicConnection var2 = new BasicConnection();
        var2.setUrl(var1.getUrl());
        var2.setUsername(var1.getUsername());
        var2.setPassword(var1.getPassword());
        return var2;
    }


    public Connection disconnect() {
        this.waitForValues = null;

        try {
            this.connection.disconnect();
        } catch (Throwable var2) {
            throw new ConnectionException(var2);
        }

        return this.connection;
    }

    public class ConnectionException extends RuntimeException {
        public ConnectionException(Throwable var2) {
            super(var2);
        }

        public ConnectionException(String var2) {
            super(var2);
        }
    }

    public static void setIgnoreCert() {
        System.setProperty(Main.Properties.TRUST_ALL, Boolean.TRUE.toString());
        try {
            TrustAll.trust();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
           e.printStackTrace();
        }
    }
}