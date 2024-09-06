package br.gov.go.mago.geobasereferencia.config.jpa;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.Getter;
import lombok.Setter;

@Primary
@Configuration(value="dataSourceJpaPropertiesConfig")
@ConfigurationProperties(prefix = "spring.jpa")
@Getter
@Setter
 public class DataSourceJpaPropertiesConfig  extends JpaProperties {

}
