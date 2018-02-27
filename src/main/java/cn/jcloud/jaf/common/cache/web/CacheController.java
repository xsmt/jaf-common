package cn.jcloud.jaf.common.cache.web;

import cn.jcloud.jaf.common.constant.CommonModules;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 缓存控制器
 * Created by Wei Han on 2016/5/6.
 */
@RestController
@RequestMapping({"/${version}/caches", "/${version}/{bizType}/caches"})
public class CacheController {

    @Autowired(required = false)
    private CacheManager cacheManager;

    @RequestMapping(value = "/names", method = RequestMethod.GET)
    public Object get() {
        return cacheManager.getCacheNames();
    }

    @RequestMapping(value = "/{cacheName}/{key}", method = RequestMethod.GET)
    public Object get(@PathVariable("cacheName") String cacheName,
                      @PathVariable("key") String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (null == cache) {
            throw CommonModules.CACHE.notFound();
        }
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (null == valueWrapper && NumberUtils.isNumber(key)) {
            valueWrapper = cache.get(NumberUtils.toLong(key));
        }
        if (null != valueWrapper) {
            return valueWrapper.get();
        }
        return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{cacheName}/{key}", method = RequestMethod.DELETE)
    public void evictKey(@PathVariable("cacheName") String cacheName,
                         @PathVariable("key") String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (null == cache) {
            throw CommonModules.CACHE.notFound();
        }
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (null != valueWrapper) {
            cache.evict(key);
            return;
        }
        if (NumberUtils.isNumber(key) && null != cache.get(NumberUtils.toLong(key))) {
            cache.evict(NumberUtils.toLong(key));
            return;
        }
        throw JafI18NException.of(ErrorCode.DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{cacheName}", method = RequestMethod.DELETE)
    public void clearCache(@PathVariable("cacheName") String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (null == cache) {
            throw CommonModules.CACHE.notFound();
        }
        cache.clear();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE)
    public void clearAll() {
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            cache.clear();
        }
    }
}
