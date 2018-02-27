package cn.jcloud.jaf.common.cache.core;

/**
 * 为支持模块化、可扩展添加CacheManager
 * Created by Wei Han on 2016/7/8.
 */
public interface CacheSupport<T> {

    boolean isCollection();

    Class<T> cacheType();

    String cacheName();

    long expireTime();
}
