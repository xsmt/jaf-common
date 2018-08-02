package cn.jcloud.jaf.common.whitelist.service;

import cn.jcloud.gaea.rest.security.services.impl.CacheUtil;
import cn.jcloud.gaea.rest.security.services.visitor.VistorsService;
import cn.jcloud.jaf.common.handler.SpringContextHolder;
import cn.jcloud.jaf.common.handler.TenantHandler;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Wei Han
 */
public class VisitorService implements VistorsService {

    private static LoadingCache<String, List<String>> whiteListCache;

    public VisitorService() {
        initWhiteListCache();
    }

    @Override
    public List<String> getWhiteRequestMappings(String key) {
        TenantHandler.setTenant(key);
        return CacheUtil.get(whiteListCache, key);
    }

    /**
     * 清空缓存
     */
    public static void cleanWhiteListCache(long orgId) {
        if (whiteListCache != null) {
            whiteListCache.invalidate(String.valueOf(orgId));
        }
    }

    /**
     * 初始化缓存
     */
    private void initWhiteListCache() {
        whiteListCache = CacheBuilder.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .build(new CacheLoader<String, List<String>>() {

                    @Override
                    public List<String> load(String key) throws Exception {
                        TenantHandler.setTenant(key);
                        GuestWhiteListService guestWhiteListService
                                = SpringContextHolder.getBean(GuestWhiteListService.class);
                        return guestWhiteListService.findAllAccess();
                    }
                });
    }

}
