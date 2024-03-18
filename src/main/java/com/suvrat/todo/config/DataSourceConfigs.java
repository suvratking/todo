package com.suvrat.todo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource", ignoreInvalidFields = true)
@Data
public class DataSourceConfigs {
    private String url;
    private String userName;
    private String password;
    private String driverClassName;

}
