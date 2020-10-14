package com.dmeplugin.vmware.autosdk;

import com.dmeplugin.vmware.autosdk.auth.VapiAuthenticationHelper;
import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.protocol.HttpConfiguration;

public class SessionHelper {


    protected VapiAuthenticationHelper vapiAuthHelper;
    protected StubConfiguration sessionStubConfig;

    public void login(String server,String port,String username,String password) throws Exception {
        this.vapiAuthHelper = new VapiAuthenticationHelper();

        HttpConfiguration httpConfig = buildHttpConfiguration();
        this.sessionStubConfig =
                vapiAuthHelper.loginByUsernameAndPassword(
                        server,port, username, password, httpConfig);

    }

    protected HttpConfiguration buildHttpConfiguration() throws Exception {
        HttpConfiguration httpConfig =
                new HttpConfiguration.Builder()
                        .setSslConfiguration(buildSslConfiguration())
                        .getConfig();

        return httpConfig;
    }

    protected HttpConfiguration.SslConfiguration buildSslConfiguration() throws Exception {
        HttpConfiguration.SslConfiguration sslConfig;

            /*
             * Below method enables all VIM API connections to the server
             * without validating the server certificates.
             *
             * Note: Below code is to be used ONLY IN DEVELOPMENT ENVIRONMENTS.
             * Circumventing SSL trust is unsafe and should not be used in
             * production software.
             */
            SslUtil.trustAllHttpsCertificates();

            /*
             * Below code enables all vAPI connections to the server
             * without validating the server certificates..
             *
             * Note: Below code is to be used ONLY IN DEVELOPMENT ENVIRONMENTS.
             * Circumventing SSL trust is unsafe and should not be used in
             * production software.
             */
            sslConfig = new HttpConfiguration.SslConfiguration.Builder()
                    .disableCertificateValidation()
                    .disableHostnameVerification()
                    .getConfig();


        return sslConfig;
    }

    public void logout() throws Exception {
        this.vapiAuthHelper.logout();
    }
}
