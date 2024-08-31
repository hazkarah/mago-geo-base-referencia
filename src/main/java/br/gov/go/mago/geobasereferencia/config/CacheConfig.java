package br.gov.go.mago.geobasereferencia.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Primary;

@EnableCaching
@Primary
// @Configuration(value = "geoportalBaseReferenciaCacheConfig")
public class CacheConfig {


    // @Bean(name = "ehCacheManager")
    // @Primary
    // public JCacheCacheManager ehCacheManager() {
    //     return new JCacheCacheManager(getCacheManager());
    // }


    // @Bean(name = {"geoportalBaseReferenciaCacheManager"})
    // @Primary
    // CacheManager getCacheManager() {
    //     final CachingProvider provider = Caching.getCachingProvider();
    //     final CacheManager cacheManager = provider.getCacheManager();

    //     final CacheConfigurationBuilder<String, Object> configurationBuilder =
    //             CacheConfigurationBuilder.newCacheConfigurationBuilder(
    //                     String.class, Object.class,
    //                     ResourcePoolsBuilder.heap(50)
    //                             .offheap(30, MemoryUnit.MB))
    //                     .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(30)));


    //     List<String> cacheNames = Arrays.asList("listaDeMunicipiosPorUfComCache", "readByCodigoIBGE");
    //     cacheNames.forEach(cn -> cacheManager.createCache(cn, Eh107Configuration.fromEhcacheCacheConfiguration(configurationBuilder.build())));

    //     // cacheManager.createCache("md5-cache",Eh107Configuration.fromEhcacheCacheConfiguration(configurationBuilder.withService(asynchronousListener)));

    //     return cacheManager;
    // }
}

