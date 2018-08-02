package cn.jcloud.jaf.common.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;

/**
 * 当前用户上下文
 * @author Wei Han
 */
public class UserHandler {

    private static ThreadLocal<String> userThreadLocal = new NamedThreadLocal<>("user");
    private static ThreadLocal<String> bearerUserThreadLocal = new NamedThreadLocal<>("bearerUser");

    private UserHandler() {
    }

    public static String getUser() {
        String user = userThreadLocal.get();

        if (!StringUtils.isEmpty(user)) {
            return user;
        }

        return "";
    }

    public static void setUser(String userId) {
        userThreadLocal.set(userId);
    }

    public static void removeUser() {
        userThreadLocal.remove();
    }

    public static void setBearerUser(String userId) {
        bearerUserThreadLocal.set(userId);
    }

    public static String getBearerUser() {
        String bearerUser = bearerUserThreadLocal.get();
        return StringUtils.isEmpty(bearerUser) ? "" : bearerUser;
    }

    public static void removeBearerUser() {
        bearerUserThreadLocal.remove();
    }

    public static void clear() {
        removeUser();
        removeBearerUser();
    }
}
