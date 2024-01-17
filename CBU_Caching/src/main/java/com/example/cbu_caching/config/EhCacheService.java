package com.example.cbu_caching.config;


import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.*;
import org.springframework.stereotype.Component;
import com.example.cbu_caching.entity.Currency;

import java.time.Duration;
import java.util.stream.StreamSupport;

@Component
public class EhCacheService {
    private final Cache<Integer, Currency> entries;
    public EhCacheService() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();

        entries = cacheManager.createCache("currencies", CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Integer.class, Currency.class, ResourcePoolsBuilder.heap(200))
                .withDefaultEventListenersThreadPool()
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(30))).build());
    }


    public void clearCache() {
        entries.clear();
    }

    public boolean isEmpty() {
        return StreamSupport.stream(entries.spliterator(), false).findAny().isEmpty();
    }

    public Currency findByCode(String ccyCode) {
        for (Cache.Entry<Integer, Currency> entry : entries) {
            if (entry.getValue().getCode().equals(ccyCode))
                return entry.getValue();
        }
        return null;
    }

    public void addCache(Currency entity) {
        entries.putIfAbsent(entity.getId(), entity);
    }

}
