package cn.jcloud.jaf.common.menu.service;

import cn.jcloud.gaea.WafProperties;
import cn.jcloud.gaea.client.http.WafSecurityHttpClient;
import cn.jcloud.gaea.rest.security.services.impl.CacheUtil;
import cn.jcloud.gaea.util.UrlUtil;
import cn.jcloud.jaf.common.config.JafContext;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.handler.UserHandler;
import cn.jcloud.jaf.common.menu.core.MenuParser;
import cn.jcloud.jaf.common.menu.core.MenuSupportCondition;
import cn.jcloud.jaf.common.menu.domain.Menu;
import cn.jcloud.jaf.common.menu.domain.MenuItem;
import cn.jcloud.jaf.common.menu.domain.MenuOperation;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.jcloud.gaea.rest.config.WafConstants.WAF_UC_VORG;

@Service
@Conditional(MenuSupportCondition.class)
public class MenuService {
    @Autowired
    private MenuParser parser;
    @Autowired
    private WafSecurityHttpClient httpClient;

    public static final String JAF_UC_USER_ALL_PRIVILEGE = "waf.uc.user.privilege";
    public static final String JAF_UC_USER_ALL_PRIVILEGE_VALUE = "roles/{system_code}/user/{user_id}/privilege?suid={user_id}";
    public static final String JAF_UC_USER_ALL_PRIVILEGE_URL;
    public static final String DEFAULT_MENU_NAME = "default";

    static {
        JAF_UC_USER_ALL_PRIVILEGE_URL = UrlUtil.combine(WafProperties.getProperty(WAF_UC_VORG),
                WafProperties.getProperty(JAF_UC_USER_ALL_PRIVILEGE, JAF_UC_USER_ALL_PRIVILEGE_VALUE));
    }
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
        Menu menuAll = getMenu(code, version);
        UserPrivilege userPrivilege = httpClient.getForObject(JAF_UC_USER_ALL_PRIVILEGE_URL, UserPrivilege.class, JafContext.getProjectName(), UserHandler.getUser(), UserHandler.getUser());
        if (userPrivilege.admin) {
            return menuAll;
        }
        return userMenuAuth(menuAll, userPrivilege.privileges);
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
                            menuResource = new ClassPathResource(JafContext.getMenuPath() + File.separator + DEFAULT_MENU_NAME + JafContext.getMenuType());
                        }
                        if (!menuResource.exists()) {
                            throw JafI18NException.of(ErrorCode.DATA_NOT_FOUND);
                        }

                        return parser.parse(menuResource.getInputStream());
                    }
                });
    }

    private Menu userMenuAuth(Menu menuAll, List<String> privileges) {
        Menu userMenu = new Menu();
        userMenu.setVersion(System.currentTimeMillis());
        userMenu.setNeedUpdate(true);
        userMenu.setActionExtension(menuAll.getActionExtension());
        menuItemAuth(userMenu.getMenuItems(), menuAll.getMenuItems(), privileges);

        return userMenu;
    }

    private void menuItemAuth(List<MenuItem> target, List<MenuItem> source, List<String> privileges) {
        for (MenuItem menuItem : source) {
            if (StringUtils.isEmpty(menuItem.getAuthCode()) || privileges.contains(menuItem.getAuthCode())) {
                MenuItem targetItem = new MenuItem();
                BeanUtils.copyProperties(menuItem, targetItem);
                targetItem.setChildren(new ArrayList<>());
                targetItem.setOperations(new ArrayList<>());
                if (menuItem.getChildren().size() > 0) {
                    menuItemAuth(targetItem.getChildren(), menuItem.getChildren(), privileges);
                }
                if (menuItem.getOperations().size() > 0) {
                    menuOperationAuth(targetItem.getOperations(), menuItem.getOperations(), privileges);
                }
                target.add(targetItem);
            }
        }
    }

    private void menuOperationAuth(List<MenuOperation> target, List<MenuOperation> source, List<String> privileges) {
        for (MenuOperation menuOperation : source) {
            if (StringUtils.isEmpty(menuOperation.getAuthCode()) || privileges.contains(menuOperation.getAuthCode())) {
                MenuOperation targetOperation = new MenuOperation();
                BeanUtils.copyProperties(menuOperation, targetOperation);
                target.add(targetOperation);

            }
        }
    }

    public static class UserPrivilege {
        private Boolean admin;
        private Boolean cache;
        private Long version;
        private List<String> privileges;

        public Boolean getAdmin() {
            return this.admin;
        }

        public void setAdmin(Boolean admin) {
            this.admin = admin;
        }

        public Boolean getCache() {
            return cache;
        }

        public void setCache(Boolean cache) {
            this.cache = cache;
        }

        public Long getVersion() {
            return version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        public List<String> getPrivileges() {
            return privileges;
        }

        public void setPrivileges(List<String> privileges) {
            this.privileges = privileges;
        }
    }
}
