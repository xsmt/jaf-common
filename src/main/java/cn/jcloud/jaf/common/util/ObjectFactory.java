package cn.jcloud.jaf.common.util;

import cn.jcloud.gaea.client.http.WafHttpClient;
import cn.jcloud.gaea.client.http.WafSecurityHttpClient;
import cn.jcloud.jaf.common.query.Condition;

/**
 * 工场类
 * Created by Wei Han on 2016/4/12.
 */
public class ObjectFactory {
    private static WafHttpClient wafHttpClient;
    private static WafSecurityHttpClient wafSecurityHttpClient;
    private static Condition deletedCondition;

    private ObjectFactory() {
    }

    /**
     * Waf已有将WafHttpClient注册成Bean，请直接使用
     */
    @Deprecated
    public static WafHttpClient wafHttpClient() {
        if (wafHttpClient == null) {
            synchronized (ObjectFactory.class) {
                if (wafHttpClient == null) {
                    wafHttpClient = new WafHttpClient();
                }
            }
        }
        return wafHttpClient;
    }

    /**
     * Waf已有将WafSecurityHttpClient注册成Bean，请直接使用
     */
    @Deprecated
    public static WafSecurityHttpClient wafSecurityHttpClient() {
        if (wafSecurityHttpClient == null) {
            synchronized (ObjectFactory.class) {
                if (wafSecurityHttpClient == null) {
                    wafSecurityHttpClient = new WafSecurityHttpClient();
                }
            }
        }
        return wafSecurityHttpClient;
    }

    public static Condition deletedCondition() {
        if (deletedCondition == null) {
            synchronized (ObjectFactory.class) {
                if (deletedCondition == null) {
                    deletedCondition =
                            Condition.eq("deleted", false, Boolean.class);
                }
            }
        }
        return deletedCondition;
    }

}
