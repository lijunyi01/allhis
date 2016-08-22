package com.allhis.filter;

import org.apache.mina.filter.ssl.KeyStoreFactory;
import org.apache.mina.filter.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.security.KeyStore;

/**
 * Created by ljy on 15/10/27.
 * ok
 */
public class SSLContextGenerator {
    private static Logger logger = LoggerFactory.getLogger(SSLContextGenerator.class);

    public SSLContext getSslContext()
    {
        SSLContext sslContext = null;
        try
        {
            File keyStoreFile = new File("/appconf/authcenter/certificates/keystore.jks");
            File trustStoreFile = new File("/appconf/authcenter/certificates/truststore.jks");

            if (keyStoreFile.exists() && trustStoreFile.exists()) {
                final KeyStoreFactory keyStoreFactory = new KeyStoreFactory();
                logger.info("Url is: " + keyStoreFile.getAbsolutePath());
                keyStoreFactory.setDataFile(keyStoreFile);
                keyStoreFactory.setPassword("123456");

                final KeyStoreFactory trustStoreFactory = new KeyStoreFactory();
                trustStoreFactory.setDataFile(trustStoreFile);
                trustStoreFactory.setPassword("123456");

                final SslContextFactory sslContextFactory = new SslContextFactory();
                final KeyStore keyStore = keyStoreFactory.newInstance();
                sslContextFactory.setKeyManagerFactoryKeyStore(keyStore);

                final KeyStore trustStore = trustStoreFactory.newInstance();
                sslContextFactory.setTrustManagerFactoryKeyStore(trustStore);
                sslContextFactory.setKeyManagerFactoryKeyStorePassword("123456");
                sslContext = sslContextFactory.newInstance();
                logger.info("SSL provider is: " + sslContext.getProvider());
            } else {
                logger.error("Keystore or Truststore file does not exist");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sslContext;
    }
}
