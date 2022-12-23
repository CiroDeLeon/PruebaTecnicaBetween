package com.between.app;

import java.time.Duration;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@EnableCaching
public class CacheConfiguration {

  @Bean
  public CacheManager cacheManagerDef() {
    CacheConfigurationBuilder<Object, Object> builder =
        CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
            ResourcePoolsBuilder.heap(1000));
    builder.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(3600)));

    CacheManager cacheManager =
        CacheManagerBuilder.newCacheManagerBuilder().withCache("similarProductsCache", builder).build();
    cacheManager.init();

    return cacheManager;
  }
}
