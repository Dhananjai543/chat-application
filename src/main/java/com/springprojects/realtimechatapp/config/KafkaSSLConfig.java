package com.springprojects.realtimechatapp.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaSSLConfig {

    @Value("${spring.kafka.bootstrap-servers:#{null}}")
    private String bootstrapServers;

    @Value("${spring.kafka.security.protocol:#{null}}")
    private String securityProtocol;

    @Value("${spring.kafka.ssl.truststore-location:#{null}}")
    private String truststoreLocation;

    @Value("${spring.kafka.ssl.truststore-password:#{null}}")
    private String truststorePassword;

    @Value("${spring.kafka.ssl.keystore-location:#{null}}")
    private String keystoreLocation;

    @Value("${spring.kafka.ssl.keystore-password:#{null}}")
    private String keystorePassword;

    @Value("${spring.kafka.ssl.key-password:#{null}}")
    private String keyPassword;

    @Value("${spring.kafka.properties.sasl.jaas.config:#{null}}")
    private String jaasConfig;

    @Value("${spring.kafka.properties.sasl.mechanism:#{null}}")
    private String saslMechanism;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public String getTruststoreLocation() {
        return truststoreLocation;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public String getKeystoreLocation() {
        return keystoreLocation;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public String getJaasConfig() {
        return jaasConfig;
    }

    public String getSaslMechanism() {
        return saslMechanism;
    }

    public Properties getSimpleKafkaProperties() {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.getBootstrapServers());
        config.put(SSLConfig.SSL_SECURITY_PROTOCOL_CONFIG, this.getSecurityProtocol());
        config.put(SSLConfig.SSL_TRUSTSTORE_LOCATION_CONFIG, this.getTruststoreLocation());
        config.put(SSLConfig.SSL_TRUSTSTORE_PASSWORD_CONFIG, this.getTruststorePassword());
        config.put(SSLConfig.SSL_KEYSTORE_LOCATION_CONFIG, this.getKeystoreLocation());
        config.put(SSLConfig.SSL_KEYSTORE_PASSWORD_CONFIG, this.getKeystorePassword());
        config.put(SSLConfig.SSL_KEY_PASSWORD_CONFIG, this.getKeyPassword());
        return config;
    }

    public Map<String, Object> getSimpleKafkaPropertiesMap() {
        Map<String, Object> config = new HashMap<>();

        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.getBootstrapServers());
        config.put(SSLConfig.SSL_SECURITY_PROTOCOL_CONFIG, this.getSecurityProtocol());
        config.put(SSLConfig.SSL_TRUSTSTORE_LOCATION_CONFIG, this.getTruststoreLocation());
        config.put(SSLConfig.SSL_TRUSTSTORE_PASSWORD_CONFIG, this.getTruststorePassword());
        config.put(SSLConfig.SSL_KEYSTORE_LOCATION_CONFIG, this.getKeystoreLocation());
        config.put(SSLConfig.SSL_KEYSTORE_PASSWORD_CONFIG, this.getKeystorePassword());
        config.put(SSLConfig.SSL_KEY_PASSWORD_CONFIG, this.getKeyPassword());

        return config;
    }
}