package cn.jcloud.jaf.common.menu.service;

import cn.jcloud.gaea.rest.security.services.impl.CacheUtil;
import cn.jcloud.jaf.common.config.JafContext;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.handler.UserHandler;
import cn.jcloud.jaf.common.menu.core.MenuParser;
import cn.jcloud.jaf.common.menu.core.MenuSupportCondition;
import cn.jcloud.jaf.common.menu.domain.Menu;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Service
@Conditional(MenuSupportCondition.class)
public class MenuService {

    @Autowired
    private MenuParser parser;

    private static LoadingCache<String, Menu> menuCache;

    public void setParser(MenuParser parser) {
        this.parser = parser;
    }

    public Menu getMenu(String code, Long version) {
        if (menuCache == null) {
            initMenuCache();
        }
        return CacheUtil.get(menuCache, code);
    }

    public Menu getUserMenu(String code, Long version) {
        Menu menu = getMenu(code, version);

        String userId = UserHandler.getUser();


        return menu;
    }

    /**
     * 初始化缓存
     */
    private void initMenuCache() {
        menuCache = CacheBuilder.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .build(new CacheLoader<String, Menu>() {

                    @Override
                    public Menu load(String key) throws Exception {
                        Resource menuResource = new ClassPathResource(JafContext.getMenuPath() + File.separator + key + JafContext.getMenuType());
                        if (!menuResource.exists()) {
                            throw JafI18NException.of(ErrorCode.DATA_NOT_FOUND);
                        }
                        return parser.parse(menuResource.getInputStream());
                    }
                });
    }

}
