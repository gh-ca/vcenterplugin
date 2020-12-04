//
// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
//

package com.dmeplugin.vmware.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecureSSLSocketFactory extends SSLSocketFactory {

    public static final Logger s_logger = LoggerFactory.getLogger(SecureSSLSocketFactory.class);
    private SSLContext sslContext;

    public SecureSSLSocketFactory(SSLContext sslContext) throws NoSuchAlgorithmException {
        if (sslContext != null) {
            this.sslContext = sslContext;
        } else {
            this.sslContext = SSLUtils.getSslContext();
        }
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return getSupportedCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        String[] ciphers = null;
        try {
            ciphers = SSLUtils.getSupportedCiphers();
        } catch (NoSuchAlgorithmException e) {
            s_logger.error("SecureSSLSocketFactory::getDefaultCipherSuites found no cipher suites");
        }
        return ciphers;
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        SSLSocketFactory factory = sslContext.getSocketFactory();
        Socket socket = factory.createSocket(s, host, port, autoClose);
        if (socket instanceof SSLSocket) {
            ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
        }
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        SSLSocketFactory factory = sslContext.getSocketFactory();
        Socket socket = factory.createSocket(host, port);
        if (socket instanceof SSLSocket) {
            ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
        }
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress inetAddress, int localPort) throws IOException, UnknownHostException {
        SSLSocketFactory factory = sslContext.getSocketFactory();
        Socket socket = factory.createSocket(host, port, inetAddress, localPort);
        if (socket instanceof SSLSocket) {
            ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
        }
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int localPort) throws IOException {
        SSLSocketFactory factory = sslContext.getSocketFactory();
        Socket socket = factory.createSocket(inetAddress, localPort);
        if (socket instanceof SSLSocket) {
            ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
        }
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        SSLSocketFactory factory = this.sslContext.getSocketFactory();
        Socket socket = factory.createSocket(address, port, localAddress, localPort);
        if (socket instanceof SSLSocket) {
            ((SSLSocket)socket).setEnabledProtocols(SSLUtils.getSupportedProtocols(((SSLSocket)socket).getEnabledProtocols()));
        }
        return socket;
    }
}