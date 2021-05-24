package com.huawei.vcenter;

import com.huawei.vcenter.utils.FileUtil;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

@Configuration
public class Tomcatconfig {
    @Value(value="${deployer.port}")
    private int port;
    private static final String KEY = "tomcat.keystore";
    @Bean
    public TomcatEmbeddedServletContainerFactory containerFactory() {
        TomcatEmbeddedServletContainerFactory  tomcat = new TomcatEmbeddedServletContainerFactory ();
        tomcat.addAdditionalTomcatConnectors(createSslConnector());
        return tomcat;
    }

    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            File keystore = new File(KEY);
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(port);
            String keystorepass= FileUtil.getKey(FileUtil.KEYSTORE_PASS_NAME);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass(keystorepass);
            protocol.setKeyAlias("tomcat");
            protocol.setSSLEnabled(true);

            return connector;
        } catch (Exception ex) {
            throw new IllegalStateException("can't access keystore: [" + "keystore"
                    + "] or truststore: [" + "keystore" + "]", ex);
        }
    }
}
