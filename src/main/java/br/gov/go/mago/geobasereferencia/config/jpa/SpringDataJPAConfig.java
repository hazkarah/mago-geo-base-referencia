package br.gov.go.mago.geobasereferencia.config.jpa;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import br.gov.go.mago.geobasereferencia.MagoGeoBaseReferenciaApplication;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableConfigurationProperties(value = { DataSourcePropertiesConfig.class, DataSourceJpaPropertiesConfig.class })
public class SpringDataJPAConfig {

        public final static String DEFAULT_SCHEMA = "public";
        public final static String SCHEMA_DOMAIN = "domain";

        @Autowired
        private Environment env;

        @Autowired
        private ApplicationContext ctx;

        @Autowired
        private DataSourcePropertiesConfig dataSourcePropertiesConfig;

        @Autowired
        private DataSourceJpaPropertiesConfig jpaPropertiesConfig;

        @PostConstruct
        public void init() {
                log.info("============= SIGCAR-DATASOURCE =============");

        }

        @Primary
        @Bean(name = "dataSource", destroyMethod = "close")
        public DataSource dataSource() {
                DataSourcePropertiesConfig dataSourcePropertiesConfig = dataSourcePropertiesConfig();

                if (dataSourcePropertiesConfig.getUrl() == null) {
                        log.error("Your database connection pool configuration is incorrect! The application" +
                                        " cannot start. Please check your Spring profile, current profiles are: {}",
                                        Arrays.toString(env.getActiveProfiles()));

                        throw new ApplicationContextException("Database connection pool is not configured correctly");
                }

                HikariDataSource dataSourcePrimario = (HikariDataSource) DataSourceBuilder
                                .create(dataSourcePropertiesConfig.getClassLoader())
                                .type(HikariDataSource.class)
                                .driverClassName(dataSourcePropertiesConfig.getDriverClassName())
                                .url(dataSourcePropertiesConfig.getUrl())
                                .username(dataSourcePropertiesConfig.getUsername())
                                .password(dataSourcePropertiesConfig.getPassword())
                                .build();

                dataSourcePrimario.setMaxLifetime(dataSourcePropertiesConfig.getHikari().getMaxLifetime());
                dataSourcePrimario
                                .setMaximumPoolSize(dataSourcePropertiesConfig.getHikari().getMaximumPoolSize());
                dataSourcePrimario.setMinimumIdle(dataSourcePropertiesConfig.getHikari().getMinimumIdle());
                dataSourcePrimario.setIdleTimeout(dataSourcePropertiesConfig.getHikari().getIdleTimeout());
                dataSourcePrimario.setConnectionTestQuery("/* ping */ SELECT 1");




                if (this.env.getProperty("spring.jpa.properties.hibernate.default_catalog") != null)
                        dataSourcePrimario.setCatalog(
                                        this.env.getProperty(
                                                        "spring.jpa.properties.hibernate.default_catalog"));

                if (this.env.getProperty("spring.jpa.properties.hibernate.default_schema") != null)
                        dataSourcePrimario.setSchema(
                                        this.env.getProperty(
                                                        "spring.jpa.properties.hibernate.default_schema"));

                return dataSourcePrimario;

        }

        @Primary
        @Bean(name = "entityManagerFactory")
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
                LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
                localContainerEntityManagerFactoryBean.setDataSource(dataSource());
                localContainerEntityManagerFactoryBean.setPackagesToScan(MagoGeoBaseReferenciaApplication.class.getPackageName());
                localContainerEntityManagerFactoryBean.setPersistenceUnitName("default");
                localContainerEntityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);

                // Explicitly set the EntityManagerFactory interface to avoid conflict between
                // the EntityManagerFactory interfaces used by Spring and Hibernate.
                localContainerEntityManagerFactoryBean.setEntityManagerFactoryInterface(EntityManagerFactory.class);

                // this.env.setProperty("spring.jpa.properties.hibernate.search.indexing.plan.synchronization.strategy","");


                JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

                localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
                localContainerEntityManagerFactoryBean.setJpaProperties(hibernateAdditionalProperties());
                return localContainerEntityManagerFactoryBean;
        }

        Properties hibernateAdditionalProperties() {
                Properties properties = new Properties();

                properties.setProperty("open-in-view",
                                Optional.ofNullable(this.jpaPropertiesConfig.getProperties().get("open-in-view"))
                                                .orElse("false"));
                properties.setProperty("defer-datasource-initialization",
                                Optional.ofNullable(this.jpaPropertiesConfig.getProperties()
                                                .get("defer-datasource-initialization"))
                                                .orElse("true"));

                properties.setProperty("database-platform",
                                Optional.ofNullable(this.jpaPropertiesConfig.getProperties()
                                                .get("database-platform"))
                                                .orElse("org.hibernate.dialect.PostgreSQLDialect"));

                properties.setProperty("hibernate.generate-ddl",
                                Optional.ofNullable(this.jpaPropertiesConfig.getProperties()
                                                .get("hibernate.generate-ddl"))
                                                .orElse("false"));
                properties.setProperty("hibernate.default_schema",
                                Optional.ofNullable(this.jpaPropertiesConfig.getProperties()
                                                .get("hibernate.default_schema"))
                                                .orElse("public"));
                properties.setProperty("hibernate.hbm2ddl.auto",
                                Optional.ofNullable(this.jpaPropertiesConfig.getProperties()
                                                .get("hibernate.hbm2ddl.auto"))
                                                .orElse("none"));
                properties.setProperty("hibernate.dialect",
                                Optional.ofNullable(this.jpaPropertiesConfig.getDatabasePlatform())
                                                .orElse("org.hibernate.dialect.PostgreSQLDialect"));

                /*
                 * Hibernate Search
                 */
                this.jpaPropertiesConfig.getProperties()
                                .keySet().forEach(k -> {
                                        properties.put(k, this.jpaPropertiesConfig.getProperties().get(k));
                                });

                return properties;
        }

        @Bean(name = "sessionFactory")
        public SessionFactory sessionFactory(
                        @Qualifier(value = "entityManagerFactory") EntityManagerFactory emf) {
                if (emf.unwrap(SessionFactory.class) == null) {
                        throw new NullPointerException("factory is not a hibernate factory");
                }
                SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
                return sessionFactory;
        }

        @Primary
        @Order(value = Ordered.HIGHEST_PRECEDENCE - 10)
        @Bean(name = "entityManager")
        public EntityManager entityManager(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
                return SharedEntityManagerCreator.createSharedEntityManager(emf);
        }

        // @Primary
        // @Bean(name = "transactionManager")
        // @Autowired
        // public HibernateTransactionManager transactionManager(
        //                 @Qualifier(value = "sessionFactory") SessionFactory sessionFactory) {
        //         HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        //         hibernateTransactionManager.setSessionFactory(sessionFactory);
        //         return hibernateTransactionManager;
        // }

        /**
         * Repository
         */
        @Primary
        @Bean(name = { "transactionManager", "jpaTransactionManager"})
        public PlatformTransactionManager jpaTransactionManager( @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory, @Qualifier("dataSource") DataSource dataSource )
        {
            JpaTransactionManager txManager = new JpaTransactionManager();
            txManager.setEntityManagerFactory( entityManagerFactory );
            txManager.setDataSource( dataSource );
            txManager.setJpaProperties(hibernateAdditionalProperties());
            txManager.setPersistenceUnitName("default");
            return txManager;
        }



        @Primary
        @Bean
        public DataSourcePropertiesConfig dataSourcePropertiesConfig() {
                if (this.dataSourcePropertiesConfig == null)
                        return this.env.getRequiredProperty("spring.datasource",
                                        DataSourcePropertiesConfig.class);
                return this.dataSourcePropertiesConfig;
        }

}
