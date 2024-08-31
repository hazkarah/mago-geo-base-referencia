package br.gov.go.mago.geobasereferencia.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "app")
@Data
public class ApplicationPropertiesConfig {

    String tenantId = "DEFAULT";

    Map<String,Task> tasks = new HashMap<>();

    @ConfigurationProperties(prefix = "tasks")
    @Data
    public static class Task {

        String tenantId;
        boolean enabled;
        String cron;
        Long fixedRate;
        Map<String,String> params;
    }
    
}
