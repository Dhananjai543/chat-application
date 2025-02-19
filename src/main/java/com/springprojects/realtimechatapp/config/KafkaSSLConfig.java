package com.springprojects.realtimechatapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
}