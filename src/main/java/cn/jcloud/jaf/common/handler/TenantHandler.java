package cn.jcloud.jaf.common.handler;

import cn.jcloud.jaf.common.config.JafContext;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.tenant.domain.Tenant;
import cn.jcloud.jaf.common.tenant.event.TenantClearEvent;
import cn.jcloud.jaf.common.tenant.event.TenantSetEvent;
import cn.jcloud.jaf.common.tenant.service.TenantService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.NamedThreadLocal;

/**
 * 租户上下文处理类
 * Created by Wei Han on 2016/5/30.
 */
public class TenantHandler {

    public static final String DEFAULT_PREFIX = "default";
    public static final String PREFIX = "#prefix#";
    public static final String TABLE_PREFIX = JafContext.getTablePrefix();

    private static ThreadLocal<Tenant> tenantThreadLocal = new NamedThreadLocal<>("tenant");
    private static TenantService tenantService;
    public static ApplicationEventPublisher eventPublisher;

    public static void init() {
        tenantService = SpringContextHolder.getBean(TenantService.class);
        eventPublisher = SpringContextHolder.getApplicationContext();
    }

    private TenantHandler() {
    }

    public static String getTablePrefix() {
        Tenant tenant = tenantThreadLocal.get();
        if (tenant == null) {
            return DEFAULT_PREFIX;
        }
        return TABLE_PREFIX + getStrict().getTenancy() + "_";
    }

    public static Long getTenant() {
        Tenant tenant = tenantThreadLocal.get();
        if (tenant != null) {
            return tenant.getId();
        } else {
            return null;
        }
    }

    public static long getStrictTenant() {
        return getStrict().getId();
    }

    public static void setTenant(String orgId) {
        Tenant tenant = tenantService.findStrictOne(orgId);
        setTenant(tenant);
    }

    public static void setTenant(long orgId) {
        Tenant tenant = tenantService.findStrictOne(orgId);
        setTenant(tenant);
    }

    public static void setTenant(Tenant tenant) {
        tenantThreadLocal.set(tenant);
        if (null != eventPublisher) {
            eventPublisher.publishEvent(new TenantSetEvent(tenant));
        }
    }

    public static String getDbConn() {
        Tenant tenant = tenantThreadLocal.get();
        if (tenant == null) {
            return null;
        }
        return tenant.getDbConn();
    }

    public static long getTableNum() {
        return getStrict().getTenancy();
    }

    public static void clear() {
        Tenant tenant = tenantThreadLocal.get();
        if (null == tenant) {
            return;
        }
        tenantThreadLocal.remove();
        if (null != eventPublisher) {
            eventPublisher.publishEvent(new TenantClearEvent(tenant));
        }
    }

    private static Tenant getStrict() {
        Tenant tenant = tenantThreadLocal.get();
        if (tenant == null) {
            throw JafI18NException.of("未标识您请求的是租户id", ErrorCode.MISSING_ORG_ID);
        }
        return tenant;
    }
}
