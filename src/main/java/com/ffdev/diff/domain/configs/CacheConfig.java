package com.ffdev.diff.domain.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.util.Collections.singletonMap;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

/**
 * {@link CacheConfig} configures a Redis Cache for distributed caching.
 */
@EnableCaching
@Configuration
public class CacheConfig {

    // It is public so we can safely refer to it in other components
    public static final String DIFF_CACHE = "diff";

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connFactory,
            @Value("${diff.cache.ttl-minutes}") long timeToLive
    ) {
        // We define a TTL so we can reclaim memory in our redis instance
        var duration = Duration.of(timeToLive, ChronoUnit.MINUTES);
        var diffCacheConfig = defaultCacheConfig().entryTtl(duration);

        return RedisCacheManager.builder(connFactory)
                .withInitialCacheConfigurations(singletonMap(DIFF_CACHE, diffCacheConfig))
                .build();
    }
}
