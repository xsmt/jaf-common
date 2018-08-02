package cn.jcloud.jaf.common.config;

import cn.jcloud.jaf.common.cache.core.LocalCacheSupportCondition;
import cn.jcloud.jaf.common.exception.JafI18NException;
import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置基础类
 * Created by Wei Han on 2016/4/12.
 */
@Conditional(LocalCacheSupportCondition.class)
@Configuration
public class LocalCacheConfigurerAdapter extends AbstractCacheConfigurerAdapter {

    public LocalCacheConfigurerAdapter() {
        if (JafContext.isRedisSupport()) {
            throw JafI18NException.of("when redis cache is available,your cache config must extends RedisCacheConfigurerAdapter");
        }
    }

    /**
     * 通过缓存存储的类型、缓存名、过期时间(单位秒)创建一个CacheManager
     *
     * @param type       缓存存储的类型
     * @param cacheName  缓存名
     * @param expireSeconds 过期时间(单位秒)
     * @return CacheManager
     */
    @Override
    protected CacheManager newCacheManager(Class type, String cacheName, long expireSeconds) {
        return newCacheManager(cacheName, expireSeconds);
    }

    /**
     * 通过缓存存储的类型、缓存名、过期时间(单位秒)创建一个存放集合的CacheManager
     *
     * @param type       缓存存储的类型（即集合的泛型）
     * @param cacheName  缓存名
     * @param expireSeconds 过期时间(单位秒)
     * @return CacheManager
     */
    @Override
    protected CacheManager newCollectionCacheManager(Class type, String cacheName, long expireSeconds) {
        return newCacheManager(cacheName, expireSeconds);
    }

    private static GuavaCacheManager newCacheManager(String cacheName, long expireSeconds) {
        GuavaCacheManager guavaCacheManager = new GuavaCacheManager(cacheName);
        guavaCacheManager.setCacheBuilder(CacheBuilder.newBuilder().expireAfterWrite(expireSeconds, TimeUnit.SECONDS));
        return guavaCacheManager;
    }
}
