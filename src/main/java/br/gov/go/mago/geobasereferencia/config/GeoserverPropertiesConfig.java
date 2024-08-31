package br.gov.go.mago.geobasereferencia.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "geoserver")
public class GeoserverPropertiesConfig {

    private String url;


    private String authToken;
}
