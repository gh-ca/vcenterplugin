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
package com.dmeplugin.vmware.util;


import com.dmeplugin.dmestore.utils.StringUtil;
import com.dmeplugin.vmware.mo.DatacenterMO;
import com.dmeplugin.vmware.mo.DatastoreFile;
import com.vmware.connection.helpers.builders.ObjectSpecBuilder;
import com.vmware.connection.helpers.builders.PropertyFilterSpecBuilder;
import com.vmware.connection.helpers.builders.PropertySpecBuilder;
import com.vmware.connection.helpers.builders.TraversalSpecBuilder;
import com.vmware.pbm.PbmPortType;
import com.vmware.pbm.PbmServiceInstanceContent;
import com.vmware.vim25.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

public class VmwareContext {

    private static final Logger s_logger = LoggerFactory.getLogger(VmwareContext.class);

    private static final int MAX_CONNECT_RETRY = 5;
    private static final int CONNECT_RETRY_INTERVAL = 1000;

    /**
     * 1M
     **/
    private static final int CHUNK_SIZE = 1 * 1024 * 1024;

    private final VmwareClient vimClient;
    private final String serverAddress;

    private final Map<String, Object> stockMap = new HashMap<String, Object>();

    private VmwareContextPool pool;
    private String poolKey;

    private static volatile int s_outstandingCount = 0;

    static {
        try {
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
            javax.net.ssl.TrustManager tm = new TrustAllManager();
            trustAllCerts[0] = tm;
            javax.net.ssl.SSLContext sc = SSLUtils.getSSLContext();
            sc.init(null, trustAllCerts, null);
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(new SecureSSLSocketFactory(sc));

            HostnameVerifier hv = new HostnameVerifier() {
                @Override
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (Exception e) {
            s_logger.error("Unexpected exception ", e);
        }
    }

    public VmwareContext(VmwareClient client, String address) {
        assert (client != null) : "Invalid parameter in constructing VmwareContext object";

        vimClient = client;
        serverAddress = address;

        registerOutstandingContext();
        if (s_logger.isInfoEnabled()) {
            s_logger.info("New VmwareContext object, current outstanding count: " + getOutstandingContextCount());
        }
    }

    public VmwareContext() {
        vimClient =new VmwareClient("vcentercontext");
        serverAddress ="";
    }

    public boolean validate() {
        return vimClient.validate();
    }

    public void registerStockObject(String name, Object obj) {
        synchronized (stockMap) {
            stockMap.put(name, obj);
        }
    }

    public void uregisterStockObject(String name) {
        synchronized (stockMap) {
            stockMap.remove(name);
        }
    }

    public void clearStockObjects() {
        synchronized (stockMap) {
            stockMap.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getStockObject(String name) {
        synchronized (stockMap) {
            return (T) stockMap.get(name);
        }
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public VimPortType getService() {
        return vimClient.getService();
    }

    public PbmPortType getPbmService() {
        return vimClient.getPbmService();
    }

    public ServiceContent getServiceContent() {
        return vimClient.getServiceContent();
    }

    public PbmServiceInstanceContent getPbmServiceContent() {
        return vimClient.getPbmServiceContent();
    }

    public ManagedObjectReference getPropertyCollector() {
        return vimClient.getPropCol();
    }

    public ManagedObjectReference getRootFolder() {
        return vimClient.getRootFolder();
    }

    public VmwareClient getVimClient() {
        return vimClient;
    }

    public void setPoolInfo(VmwareContextPool pool, String poolKey) {
        this.pool = pool;
        this.poolKey = poolKey;
    }

    public VmwareContextPool getPool() {
        return pool;
    }

    public String getPoolKey() {
        return poolKey;
    }

    public void idleCheck() throws Exception {
        getRootFolder();
    }

    public static int getOutstandingContextCount() {
        return s_outstandingCount;
    }

    public static void registerOutstandingContext() {
        s_outstandingCount++;
    }

    public static void unregisterOutstandingContext() {
        s_outstandingCount--;
    }

    public ManagedObjectReference getHostMorByPath(String inventoryPath) throws Exception {
        assert (inventoryPath != null);

        String[] tokens;
        if (inventoryPath.startsWith("/")) {
            tokens = inventoryPath.substring(1).split("/");
        } else {
            tokens = inventoryPath.split("/");
        }

        ManagedObjectReference mor = getRootFolder();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            List<ObjectContent> ocs;
            PropertySpec pSpec = null;
            ObjectSpec oSpec = null;
            if ("Datacenter".equalsIgnoreCase(mor.getType())) {
                pSpec = new PropertySpec();
                pSpec.setAll(false);
                pSpec.setType("ManagedEntity");
                pSpec.getPathSet().add("name");

                TraversalSpec dcHostFolderTraversal = new TraversalSpec();
                dcHostFolderTraversal.setType("Datacenter");
                dcHostFolderTraversal.setPath("hostFolder");
                dcHostFolderTraversal.setName("dcHostFolderTraversal");

                oSpec = new ObjectSpec();
                oSpec.setObj(mor);
                oSpec.setSkip(Boolean.TRUE);
                oSpec.getSelectSet().add(dcHostFolderTraversal);

            } else if ("Folder".equalsIgnoreCase(mor.getType())) {
                pSpec = new PropertySpec();
                pSpec.setAll(false);
                pSpec.setType("ManagedEntity");
                pSpec.getPathSet().add("name");

                TraversalSpec folderChildrenTraversal = new TraversalSpec();
                folderChildrenTraversal.setType("Folder");
                folderChildrenTraversal.setPath("childEntity");
                folderChildrenTraversal.setName("folderChildrenTraversal");

                oSpec = new ObjectSpec();
                oSpec.setObj(mor);
                oSpec.setSkip(Boolean.TRUE);
                oSpec.getSelectSet().add(folderChildrenTraversal);

            } else if ("ClusterComputeResource".equalsIgnoreCase(mor.getType())) {
                pSpec = new PropertySpec();
                pSpec.setType("ManagedEntity");
                pSpec.getPathSet().add("name");

                TraversalSpec clusterHostTraversal = new TraversalSpec();
                clusterHostTraversal.setType("ClusterComputeResource");
                clusterHostTraversal.setPath("host");
                clusterHostTraversal.setName("folderChildrenTraversal");

                oSpec = new ObjectSpec();
                oSpec.setObj(mor);
                oSpec.setSkip(Boolean.TRUE);
                oSpec.getSelectSet().add(clusterHostTraversal);

            } else {
                s_logger.error("Invalid inventory path, path element can only be datacenter and folder");
                return null;
            }

            PropertyFilterSpec pfSpec = new PropertyFilterSpec();
            pfSpec.getPropSet().add(pSpec);
            pfSpec.getObjectSet().add(oSpec);
            List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
            pfSpecArr.add(pfSpec);
            ocs = getService().retrieveProperties(getPropertyCollector(), pfSpecArr);

            if (ocs != null && ocs.size() > 0) {
                boolean found = false;
                for (ObjectContent oc : ocs) {
                    String name = oc.getPropSet().get(0).getVal().toString();
                    if (name.equalsIgnoreCase(token) || "host".equalsIgnoreCase(name)) {
                        mor = oc.getObj();
                        found = true;
                        if ("host".equalsIgnoreCase(name)) {
                            i--;
                        }
                        break;
                    }
                }
                if (!found) {
                    s_logger.error("Path element points to an un-existing inventory entity");
                    return null;
                }
            } else {
                s_logger.error("Path element points to an un-existing inventory entity");
                return null;
            }
        }
        return mor;
    }

    // path in format of <datacenter name>/<datastore name>
    public ManagedObjectReference getDatastoreMorByPath(String inventoryPath) throws Exception {
        assert (inventoryPath != null);

        String[] tokens;
        if (inventoryPath.startsWith("/")) {
            tokens = inventoryPath.substring(1).split("/");
        } else {
            tokens = inventoryPath.split("/");
        }

        if (tokens == null || tokens.length != 2) {
            s_logger.error("Invalid datastore inventory path. path: " + inventoryPath);
            return null;
        }

        DatacenterMO dcMo = new DatacenterMO(this, tokens[0]);
        if (dcMo.getMor() == null) {
            s_logger.error("Unable to locate the datacenter specified in path: " + inventoryPath);
            return null;
        }

        return dcMo.findDatastore(tokens[1]);
    }

    public void waitForTaskProgressDone(ManagedObjectReference morTask) throws Exception {
        while (true) {
            TaskInfo tinfo = (TaskInfo) vimClient.getDynamicProperty(morTask, "info");
            Integer progress = tinfo.getProgress();
            if (progress == null) {
                break;
            }

            if (progress.intValue() >= 100) {
                break;
            }

            Thread.sleep(1000);
        }
    }

    public void getFile(String urlString, String localFileFullName) throws Exception {
        HttpURLConnection conn = getHttpConnection(urlString);

        InputStream in = conn.getInputStream();
        OutputStream out = new FileOutputStream(new File(localFileFullName));
        byte[] buf = new byte[CHUNK_SIZE];
        int len = 0;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public void uploadFile(String urlString, String localFileFullName) throws Exception {
        uploadFile(urlString, new File(localFileFullName));
    }

    public void uploadFile(String urlString, File localFile) throws Exception {
        HttpURLConnection conn = getHttpConnection(urlString, "PUT");
        OutputStream out = null;
        InputStream in = null;
        BufferedReader br = null;

        try {
            out = conn.getOutputStream();
            in = new FileInputStream(localFile);
            byte[] buf = new byte[CHUNK_SIZE];
            int len = 0;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), getCharSetFromConnection(conn)));
            String line;
            while ((line = br.readLine()) != null) {
                if (s_logger.isTraceEnabled()) {
                    s_logger.trace("Upload " + urlString + " response: " + line);
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }

            if (br != null) {
                br.close();
            }
        }
    }

    private Charset getCharSetFromConnection(HttpURLConnection conn) {
        String charsetName = conn.getContentEncoding();
        Charset charset;
        try {
            charset = Charset.forName(charsetName);
        } catch (IllegalArgumentException e) {
            s_logger.warn("Illegal/unsupported/null charset name from connection. charsetname from connection is " + charsetName);
            charset = StringUtil.getPreferredCharset();
        }
        return charset;
    }

    public void uploadVmdkFile(String httpMethod, String urlString, String localFileName, long totalBytesUpdated, ActionDelegate<Long> progressUpdater) throws Exception {

        HttpURLConnection conn = getRawHttpConnection(urlString);

        conn.setDoOutput(true);
        conn.setUseCaches(false);

        conn.setChunkedStreamingMode(CHUNK_SIZE);
        conn.setRequestMethod(httpMethod);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/x-vnd.vmware-streamVmdk");
        conn.setRequestProperty("Content-Length", Long.toString(new File(localFileName).length()));
        connectWithRetry(conn);

        BufferedOutputStream bos = null;
        BufferedInputStream is = null;
        try {
            bos = new BufferedOutputStream(conn.getOutputStream());
            is = new BufferedInputStream(new FileInputStream(localFileName));
            int bufferSize = CHUNK_SIZE;
            byte[] buffer = new byte[bufferSize];
            while (true) {
                int bytesRead = is.read(buffer, 0, bufferSize);
                if (bytesRead == -1) {
                    break;
                }
                bos.write(buffer, 0, bytesRead);
                totalBytesUpdated += bytesRead;
                bos.flush();
                if (progressUpdater != null) {
                    progressUpdater.action(new Long(totalBytesUpdated));
                }
            }
            bos.flush();
        } finally {
            if (is != null) {
                is.close();
            }
            if (bos != null) {
                bos.close();
            }

            conn.disconnect();
        }
    }

    public long downloadVmdkFile(String urlString, String localFileName, long totalBytesDownloaded, ActionDelegate<Long> progressUpdater) throws Exception {
        HttpURLConnection conn = getRawHttpConnection(urlString);

        String cookie = vimClient.getServiceCookie();
        if (cookie == null) {
            s_logger.error("No cookie is found in vwware web service request context!");
            throw new Exception("No cookie is found in vmware web service request context!");
        }
        conn.addRequestProperty("Cookie", cookie);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(true);
        connectWithRetry(conn);

        long bytesWritten = 0;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = conn.getInputStream();
            out = new FileOutputStream(new File(localFileName));

            byte[] buf = new byte[CHUNK_SIZE];
            int len = 0;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
                bytesWritten += len;
                totalBytesDownloaded += len;

                if (progressUpdater != null) {
                    progressUpdater.action(new Long(totalBytesDownloaded));
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }

            conn.disconnect();
        }
        return bytesWritten;
    }

    public byte[] getResourceContent(String urlString) throws Exception {
        HttpURLConnection conn = getHttpConnection(urlString);
        InputStream in = conn.getInputStream();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[CHUNK_SIZE];
        int len = 0;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        return out.toByteArray();
    }

    public void uploadResourceContent(String urlString, byte[] content) throws Exception {
        // vSphere does not support POST
        HttpURLConnection conn = getHttpConnection(urlString, "PUT");

        OutputStream out = conn.getOutputStream();
        out.write(content);
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), getCharSetFromConnection(conn)));
        String line;
        while ((in.ready()) && (line = in.readLine()) != null) {
            if (s_logger.isTraceEnabled()) {
                s_logger.trace("Upload " + urlString + " response: " + line);
            }
        }
        out.close();
        in.close();
    }

    /*
     * Sample content returned by query a datastore directory
     *
     * Url for the query
     *     https://vsphere-1.lab.vmops.com/folder/Fedora-clone-test?dcPath=cupertino&dsName=NFS+datastore
     *
     * Returned conent from vSphere
     *
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
        <html>
          <head>
            <meta http-equiv="content-type" content="text/html; charset=utf-8">
            <title>Index of Fedora-clone-test on datastore NFS datastore in datacenter cupertino</title></head>
          <body>
        <h1>Index of Fedora-clone-test on datastore NFS datastore in datacenter cupertino</h1>
        <table>
            <tr><th>Name</th><th>Last modified</th><th>Size</th></tr><tr><th colspan="3"><hr></th></tr>
            <tr><td><a href="/folder?dcPath=cupertino&amp;dsName=NFS%20datastore">Parent Directory</a></td><td>&nbsp;</td><td align="right">  - </td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/Fedora%2dclone%2dtest%2da2013465%2ehlog?dcPath=cupertino&amp;dsName=NFS%20datastore">
            Fedora-clone-test-a2013465.hlog</a></td><td align="right">15-Aug-2010 00:13</td><td align="right">1</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/Fedora%2dclone%2dtest%2da2013465%2evswp?dcPath=cupertino&amp;dsName=NFS%20datastore">
            Fedora-clone-test-a2013465.vswp</a></td><td align="right">14-Aug-2010 23:01</td><td align="right">402653184</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/Fedora%2dclone%2dtest%2dflat%2evmdk?dcPath=cupertino&amp;dsName=NFS%20datastore">
            Fedora-clone-test-flat.vmdk</a></td><td align="right">26-Aug-2010 18:43</td><td align="right">17179869184</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/Fedora%2dclone%2dtest%2envram?dcPath=cupertino&amp;dsName=NFS%20datastore">
            Fedora-clone-test.nvram</a></td><td align="right">15-Aug-2010 00:13</td><td align="right">8684</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/Fedora%2dclone%2dtest%2evmdk?dcPath=cupertino&amp;dsName=NFS%20datastore">
            Fedora-clone-test.vmdk</a></td><td align="right">15-Aug-2010 00:13</td><td align="right">479</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/Fedora%2dclone%2dtest%2evmsd?dcPath=cupertino&amp;dsName=NFS%20datastore">
            Fedora-clone-test.vmsd</a></td><td align="right">14-Aug-2010 16:59</td><td align="right">0</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/Fedora%2dclone%2dtest%2evmx?dcPath=cupertino&amp;dsName=NFS%20datastore">
            Fedora-clone-test.vmx</a></td><td align="right">15-Aug-2010 00:13</td><td align="right">3500</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/Fedora%2dclone%2dtest%2evmxf?dcPath=cupertino&amp;dsName=NFS%20datastore">
            Fedora-clone-test.vmxf</a></td><td align="right">15-Aug-2010 00:13</td><td align="right">272</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/test%2etxt?dcPath=cupertino&amp;dsName=NFS%20datastore">test.txt</a></td>
            <td align="right">24-Aug-2010 01:03</td><td align="right">12</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/vmware%2d2%2elog?dcPath=cupertino&amp;dsName=NFS%20datastore">
            vmware-2.log</a></td><td align="right">14-Aug-2010 16:51</td><td align="right">80782</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/vmware%2d3%2elog?dcPath=cupertino&amp;dsName=NFS%20datastore">
            vmware-3.log</a></td><td align="right">14-Aug-2010 19:07</td><td align="right">58573</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/vmware%2d4%2elog?dcPath=cupertino&amp;dsName=NFS%20datastore">
            vmware-4.log</a></td><td align="right">14-Aug-2010 23:00</td><td align="right">49751</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/vmware%2d5%2elog?dcPath=cupertino&amp;dsName=NFS%20datastore">
            vmware-5.log</a></td><td align="right">15-Aug-2010 00:04</td><td align="right">64024</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/vmware%2d6%2elog?dcPath=cupertino&amp;dsName=NFS%20datastore">
            vmware-6.log</a></td><td align="right">15-Aug-2010 00:11</td><td align="right">59742</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/vmware%2d7%2elog?dcPath=cupertino&amp;dsName=NFS%20datastore">
            vmware-7.log</a></td><td align="right">15-Aug-2010 00:13</td><td align="right">59859</td></tr>
            <tr><td><a href="/folder/Fedora%2dclone%2dtest/vmware%2elog?dcPath=cupertino&amp;dsName=NFS%20datastore">
            vmware.log</a></td><td align="right">15-Aug-2010 00:23</td><td align="right">47157</td></tr>
            <tr><th colspan="5"><hr></th></tr>
        </table>
          </body>
        </html>
     */
    public String[] listDatastoreDirContent(String urlString) throws Exception {
        List<String> fileList = new ArrayList<String>();
        String content = new String(getResourceContent(urlString),"UTF-8");
        String marker = "</a></td><td ";
        int parsePos = -1;
        do {
            parsePos = content.indexOf(marker, parsePos < 0 ? 0 : parsePos);
            if (parsePos > 0) {
                int beginPos = content.lastIndexOf('>', parsePos - 1);
                if (beginPos < 0) {
                    beginPos = 0;
                }

                fileList.add((content.substring(beginPos + 1, parsePos)));
                parsePos += marker.length();
            } else {
                break;
            }
        } while (parsePos > 0);
        return fileList.toArray(new String[0]);
    }

    public String composeDatastoreBrowseUrl(String dcName, String fullPath) {
        DatastoreFile dsFile = new DatastoreFile(fullPath);
        return composeDatastoreBrowseUrl(dcName, dsFile.getDatastoreName(), dsFile.getRelativePath());
    }

    public String composeDatastoreBrowseUrl(String dcName, String datastoreName, String relativePath) {
        assert (relativePath != null);
        assert (datastoreName != null);

        StringBuffer sb = new StringBuffer();
        sb.append("https://");
        sb.append(serverAddress);
        sb.append("/folder/");
        sb.append(relativePath);
        try {
            sb.append("?dcPath=").append(URLEncoder.encode(dcName, "UTF-8"));
            sb.append("&dsName=").append(URLEncoder.encode(datastoreName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            s_logger.error("Unable to encode URL. dcPath : " + dcName + ", dsName :" + datastoreName, e);
        }
        return sb.toString();
    }

    public HttpURLConnection getHttpConnection(String urlString) throws Exception {
        return getHttpConnection(urlString, "GET");
    }

    public HttpURLConnection getHttpConnection(String urlString, String httpMethod) throws Exception {
        String cookie = vimClient.getServiceCookie();
        if (cookie == null) {
            s_logger.error("No cookie is found in vmware web service request context!");
            throw new Exception("No cookie is found in vmware web service request context!");
        }
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(true);
        conn.addRequestProperty("Cookie", cookie);
        conn.setRequestMethod(httpMethod);
        connectWithRetry(conn);
        return conn;
    }

    public HttpURLConnection getRawHttpConnection(String urlString) throws Exception {
        URL url = new URL(urlString);
        return (HttpURLConnection)url.openConnection();
    }

    private static void connectWithRetry(HttpURLConnection conn) throws Exception {
        boolean connected = false;
        for (int i = 0; i < MAX_CONNECT_RETRY && !connected; i++) {
            try {
                conn.connect();
                connected = true;
                s_logger.info("Connected, conn: " + conn.toString() + ", retry: " + i);
            } catch (Exception e) {
                s_logger.warn("Unable to connect, conn: " + conn.toString() + ", message: " + e.toString() + ", retry: " + i);

                try {
                    Thread.sleep(CONNECT_RETRY_INTERVAL);
                } catch (InterruptedException ex) {
                    s_logger.debug("[ignored] interupted while connecting.");
                }
            }
        }

        if (!connected) {
            throw new Exception("Unable to connect to " + conn.toString());
        }
    }

    public void close() {
        clearStockObjects();
        try {
            s_logger.info("Disconnecting VMware session");
            vimClient.disconnect();
        } catch(SOAPFaultException sfe) {
            s_logger.debug("Tried to disconnect a session that is no longer valid");
        } catch (Exception e) {
            s_logger.warn("Unexpected exception: ", e);
        } finally {
            if (pool != null) {
                pool.unregisterContext(this);
            }
            unregisterOutstandingContext();
        }
    }

    public static class TrustAllManager implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
            return;
        }
    }

    /**
     * Returns all the MOREFs of the specified type that are present under the
     * folder
     *
     * @param folder    {@link com.vmware.vim25.ManagedObjectReference} of the folder to begin the search
     *                  from
     * @param morefType Type of the managed entity that needs to be searched
     * @return Map of name and MOREF of the managed objects present. If none
     *         exist then empty Map is returned
     * @throws com.vmware.vim25.InvalidPropertyFaultMsg
     *
     * @throws com.vmware.vim25.RuntimeFaultFaultMsg
     *
     */
    public List<Pair<ManagedObjectReference, String>> inFolderByType(
            final ManagedObjectReference folder, final String morefType, final RetrieveOptions retrieveOptions
    ) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        final PropertyFilterSpec[] propertyFilterSpecs = propertyFilterSpecs(folder, morefType, "name");

        // reuse this property collector again later to scroll through results
        final ManagedObjectReference propertyCollector = getPropertyCollector();

        RetrieveResult results = getService().retrievePropertiesEx(
                propertyCollector,
                Arrays.asList(propertyFilterSpecs),
                retrieveOptions);

        final List<Pair<ManagedObjectReference, String>> tgtMoref =
                new ArrayList<>();
        while(results != null && !results.getObjects().isEmpty()) {
            resultsToTgtMorefList(results, tgtMoref);
            final String token = results.getToken();
            // if we have a token, we can scroll through additional results, else there's nothing to do.
            results =
                    (token != null) ?
                            getService().continueRetrievePropertiesEx(propertyCollector,token) : null;
        }

        return tgtMoref;
    }

    public List<Pair<ManagedObjectReference, String>> inFolderByType(ManagedObjectReference folder, String morefType) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        return inFolderByType(folder,morefType, new RetrieveOptions());
    }

    /**
     * Returns all the MOREFs of the specified type that are present under the
     * container
     *
     * @param container       {@link ManagedObjectReference} of the container to begin the
     *                        search from
     * @param morefType       Type of the managed entity that needs to be searched
     * @param morefProperties Array of properties to be fetched for the moref
     * @return Map of MOREF and Map of name value pair of properties requested of
     *         the managed objects present. If none exist then empty Map is
     *         returned
     * @throws InvalidPropertyFaultMsg
     * @throws RuntimeFaultFaultMsg
     */
    public Map<ManagedObjectReference, Map<String, Object>> inContainerByType(
            ManagedObjectReference container, String morefType,
            String[] morefProperties, RetrieveOptions retrieveOptions) throws InvalidPropertyFaultMsg,
            RuntimeFaultFaultMsg {
        List<ObjectContent> oCont = containerViewByType(container, morefType, retrieveOptions, morefProperties).getObjects();

        Map<ManagedObjectReference, Map<String, Object>> tgtMoref =
                new HashMap<ManagedObjectReference, Map<String, Object>>();

        if (oCont != null) {
            for (ObjectContent oc : oCont) {
                Map<String, Object> propMap = new HashMap<String, Object>();
                List<DynamicProperty> dps = oc.getPropSet();
                if (dps != null) {
                    for (DynamicProperty dp : dps) {
                        propMap.put(dp.getName(), dp.getVal());
                    }
                }
                tgtMoref.put(oc.getObj(), propMap);
            }
        }
        return tgtMoref;
    }

    /**
     * Returns all the MOREFs of the specified type that are present under the
     * container
     *
     * @param folder    {@link ManagedObjectReference} of the container to begin the
     *                  search from
     * @param morefType Type of the managed entity that needs to be searched
     * @return Map of name and MOREF of the managed objects present. If none
     *         exist then empty Map is returned
     * @throws InvalidPropertyFaultMsg
     * @throws RuntimeFaultFaultMsg
     */
    public List<Pair<ManagedObjectReference, String>> inContainerByType(
            ManagedObjectReference folder, String morefType, RetrieveOptions retrieveOptions)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        RetrieveResult rslts = containerViewByType(folder, morefType, retrieveOptions);
        return toList(rslts);
    }

    public List<Pair<ManagedObjectReference, String>> toList(RetrieveResult rslts) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        final List<Pair<ManagedObjectReference, String>> tgtMoref = new ArrayList<>();
        String token = null;
        token = populate(rslts, tgtMoref);

        while ( token != null && !token.isEmpty() ) {
            // fetch results based on new token
            rslts = getService().continueRetrievePropertiesEx(
                    getServiceContent().getPropertyCollector(), token);

            token = populate(rslts, tgtMoref);
        }

        return tgtMoref;
    }


    public List<Pair<ManagedObjectReference, String>> inContainerByType(ManagedObjectReference container, String morefType) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return inContainerByType(container, morefType, new RetrieveOptions());
    }

    /**
     * Returns the raw RetrieveResult object for the provided container filtered on properties list
     *
     * @param container       - container to look in
     * @param morefType       - type to filter for
     * @param morefProperties - properties to include
     * @return com.vmware.vim25.RetrieveResult for this query
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     */
    public RetrieveResult containerViewByType(
            final ManagedObjectReference container,
            final String morefType,
            final RetrieveOptions retrieveOptions,
            final String... morefProperties
    ) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        PropertyFilterSpec[] propertyFilterSpecs = propertyFilterSpecs(container, morefType, morefProperties);

        return containerViewByType(container,morefType,morefProperties,retrieveOptions,propertyFilterSpecs);
    }

    public RetrieveResult containerViewByType(
            final ManagedObjectReference container,
            final String morefType,
            final String[] morefProperties,
            final RetrieveOptions retrieveOptions,
            final PropertyFilterSpec... propertyFilterSpecs
    ) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        return getService().retrievePropertiesEx(
                getServiceContent().getPropertyCollector(),
                Arrays.asList(propertyFilterSpecs),
                retrieveOptions
        );
    }

    public RetrieveResult containerViewByType(
            final ManagedObjectReference container,
            final String morefType,
            final RetrieveOptions retrieveOptions
    ) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        return this.containerViewByType(container,morefType,retrieveOptions,"name");
    }

    private void resultsToTgtMorefMap(RetrieveResult results, Map<String, ManagedObjectReference> tgtMoref) {
        List<ObjectContent> oCont = (results != null) ? results.getObjects() : null;

        if (oCont != null) {
            for (ObjectContent oc : oCont) {
                ManagedObjectReference mr = oc.getObj();
                String entityNm = null;
                List<DynamicProperty> dps = oc.getPropSet();
                if (dps != null) {
                    for (DynamicProperty dp : dps) {
                        entityNm = (String) dp.getVal();
                    }
                }
                tgtMoref.put(entityNm, mr);
            }
        }
    }

    private void resultsToTgtMorefList(RetrieveResult results, List<Pair<ManagedObjectReference, String>> tgtMoref) {
        List<ObjectContent> oCont = (results != null) ? results.getObjects() : null;

        if (oCont != null) {
            for (ObjectContent oc : oCont) {
                ManagedObjectReference mr = oc.getObj();
                String entityNm = null;
                List<DynamicProperty> dps = oc.getPropSet();
                if (dps != null) {
                    for (DynamicProperty dp : dps) {
                        entityNm = (String) dp.getVal();
                    }
                }
                tgtMoref.add(new Pair<>(mr, entityNm));
            }
        }
    }

    public PropertyFilterSpec[] propertyFilterSpecs(
            ManagedObjectReference container,
            String morefType,
            String... morefProperties
    ) throws RuntimeFaultFaultMsg {

        ManagedObjectReference viewManager = getServiceContent().getViewManager();
        ManagedObjectReference containerView =
                getService().createContainerView(viewManager, container,
                        Arrays.asList(morefType), true);

        return new PropertyFilterSpec[]{
                new PropertyFilterSpecBuilder()
                        .propSet(
                                new PropertySpecBuilder()
                                        .all(Boolean.FALSE)
                                        .type(morefType)
                                        .pathSet(morefProperties)
                        )
                        .objectSet(
                        new ObjectSpecBuilder()
                                .obj(containerView)
                                .skip(Boolean.TRUE)
                                .selectSet(
                                        new TraversalSpecBuilder()
                                                .name("view")
                                                .path("view")
                                                .skip(false)
                                                .type("ContainerView")
                                )
                )
        };
    }

    public static String populate(final RetrieveResult rslts, final List<Pair<ManagedObjectReference, String>> tgtMoref) {
        String token = null;
        if (rslts != null) {
            token = rslts.getToken();
            for(ObjectContent oc : rslts.getObjects()) {
                ManagedObjectReference mr = oc.getObj();
                String entityNm = null;
                List<DynamicProperty> dps = oc.getPropSet();
                if (dps != null) {
                    for (DynamicProperty dp : dps) {
                        entityNm = (String) dp.getVal();
                    }
                }
                tgtMoref.add(new Pair<>(mr, entityNm));
            }
        }
        return token;
    }
}
