package br.gov.go.mago.geobasereferencia.config.jpa;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;

import lombok.Getter;
import lombok.Setter;



@Primary
@Configuration(value="dataSourcePropertiesConfig")
@ConfigurationProperties(prefix = "spring.datasource")
// @PropertySource(value = "classpath:spring-data-config.yaml", factory = PropertySourceFactoryYaml.class)
@Getter
@Setter
 public class DataSourcePropertiesConfig  extends DataSourceProperties {

    private HikariConfig hikari;


}
