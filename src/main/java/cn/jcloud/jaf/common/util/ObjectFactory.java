package cn.jcloud.jaf.common.util;


import cn.jcloud.jaf.common.http.JafHttpClient;
import cn.jcloud.jaf.common.http.JafSecurityHttpClient;
import cn.jcloud.jaf.common.query.Condition;

/**
 * 工场类
 * Created by Wei Han on 2016/4/12.
 */
public class ObjectFactory {
    private static JafHttpClient jafHttpClient;
    private static JafSecurityHttpClient jafSecurityHttpClient;
    private static Condition deletedCondition;

    private ObjectFactory() {
    }

    /**
     * Waf已有将WafHttpClient注册成Bean，请直接使用
     */
    @Deprecated
    public static JafHttpClient wafHttpClient() {
        if (jafHttpClient == null) {
            synchronized (ObjectFactory.class) {
                if (jafHttpClient == null) {
                    jafHttpClient = new JafHttpClient();
                }
            }
        }
        return jafHttpClient;
    }

    /**
     * Waf已有将WafSecurityHttpClient注册成Bean，请直接使用
     */
    @Deprecated
    public static JafSecurityHttpClient wafSecurityHttpClient() {
        if (jafSecurityHttpClient == null) {
            synchronized (ObjectFactory.class) {
                if (jafSecurityHttpClient == null) {
                    jafSecurityHttpClient = new JafSecurityHttpClient();
                }
            }
        }
        return jafSecurityHttpClient;
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
