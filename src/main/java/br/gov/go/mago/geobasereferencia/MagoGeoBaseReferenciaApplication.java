package br.gov.go.mago.geobasereferencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.scheduling.annotation.EnableScheduling;

import br.gov.go.mago.car.core.CoreApplication;
import br.gov.go.mago.car.domain.CarCoreDomain;
import br.gov.go.mago.geobasereferencia.config.ApplicationPropertiesConfig;



@SpringBootApplication(exclude = { ElasticsearchRestClientAutoConfiguration.class, LiquibaseAutoConfiguration.class })
@Import(value = { CoreApplication.class, CarCoreDomain.class})
@EnableScheduling
@EnableCaching
@EnableConfigurationProperties( value = { ApplicationPropertiesConfig.class } )
@EnableJpaRepositories(basePackages = {"br.gov.go.mago.geobasereferencia.jpa"},repositoryImplementationPostfix = "Impl", queryLookupStrategy=QueryLookupStrategy.Key.USE_DECLARED_QUERY )
// @EnableElasticsearchRepositories(basePackages= {"br.gov.go.mago.geobasereferencia.repository.elasticsearch"}, repositoryImplementationPostfix = "Impl", queryLookupStrategy=QueryLookupStrategy.Key.USE_DECLARED_QUERY)
public class MagoGeoBaseReferenciaApplication {


    public static void main(String[] args) {
        SpringApplication.run(MagoGeoBaseReferenciaApplication.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MagoGeoBaseReferenciaApplication.class);
    }

}
