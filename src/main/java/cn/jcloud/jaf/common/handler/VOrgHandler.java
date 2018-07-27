package cn.jcloud.jaf.common.handler;

import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;

/**
 * 
 * @author Wulj
 *
 */
public class VOrgHandler {

    private static ThreadLocal<String> VOrgThreadLocal = new NamedThreadLocal<String>("VOrg");

    private static ThreadLocal<String> VOrgIdThreadLocal = new NamedThreadLocal<String>("VOrgId");

    public static String getVOrgName() {

        String VOrgName = VOrgThreadLocal.get();

        if (!StringUtils.isEmpty(VOrgName)) {
            return VOrgName;
        }

        return "";
    }

    public static void setVOrgName(String VOrgName) {
        VOrgThreadLocal.set(VOrgName);
    }

    public static void removeVOrgName() {
        VOrgThreadLocal.remove();
    }

    public static String getVOrgId() {

        String VOrgId = VOrgIdThreadLocal.get();

        if (!StringUtils.isEmpty(VOrgId)) {
            return VOrgId;
        }

        return "";
    }

    public static void setVOrgId(String VOrgId) {
        VOrgIdThreadLocal.set(VOrgId);
    }

    public static void removeVOrgId() {
        VOrgIdThreadLocal.remove();
    }

    public static void clear(){
        removeVOrgName();
        removeVOrgId();
    }
}
