package com.brandmaker.cs.bitzer.qrcodegenerator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class AppProperties {

    private Environment env;
    public AppProperties(Environment env) {
        this.env = env;
    }

    public String getAppName() {
        return env.getProperty("spring.application.name");
    }
    public String getActiveProfile() {
        return env.getProperty("spring.profiles.active");
    }
    public String getAppVersion() {
        return env.getProperty("app.version");
    }
    public String getIsDevActive() {
        return env.getProperty("dev.is.active");
    }
    public String getWebApiRoot() {
        return env.getProperty("server.url");
    }
    public String getWebApiUsername() {
        return env.getProperty("server.username");
    }
    public String getWebApiPassword() {
        return env.getProperty("server.password");
    }
    public String getCustomStructureId() { return env.getProperty("custom.structure.id"); }
    public String getBitzerUrl() { return env.getProperty("bitzer.home.url"); }
}
