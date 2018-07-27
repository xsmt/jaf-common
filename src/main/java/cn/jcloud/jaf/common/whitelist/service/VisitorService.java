package cn.jcloud.jaf.common.whitelist.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.nd.gaea.rest.security.services.impl.CacheUtil;
import com.nd.social.common.handler.SpringContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hasayaki(125473)
 *
 */
@Service
public class VisitorService {

    private static LoadingCache<String, List<String>> guestApiCache;

    public VisitorService() {
        initCustomizableWhiteApiCache();
    }

    /**
     * 获取非基础白名单，后台配置的访客接口列表
     */
    public List<String> getCustomizableWhiteApiList(String tenantId) {
        
        return CacheUtil.get(guestApiCache, tenantId);
    }

    /**
     * 清空缓存
     */
    public static void cleanCustomizableWhiteApiCache(long tenantId) {
    	
        if (guestApiCache != null) {
            guestApiCache.invalidate(String.valueOf(tenantId));
        }
    }

    /**
     * 初始化缓存
     */
    private void initCustomizableWhiteApiCache() {
    	
        guestApiCache = CacheBuilder.newBuilder().expireAfterWrite(12, TimeUnit.HOURS)
        		.build(new CacheLoader<String, List<String>>() {

                    @Override
                    public List<String> load(String tenantId) throws Exception {
                        
                        GuestWhiteListService guestWhiteListService = SpringContextHolder.getBean(GuestWhiteListService.class);
                        return guestWhiteListService.findAllAccess();
                    }
                });
    }
}
