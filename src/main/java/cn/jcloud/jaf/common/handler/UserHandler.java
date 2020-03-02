package cn.jcloud.jaf.common.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.NamedThreadLocal;

import java.util.*;

/**
 * 当前用户上下文
 * @author Wei Han
 */
public class UserHandler {

    private static String USER_PROCUREMENT_ID_KEY = "procurement_id";
    private static String USER_PROCUREMENT_CODE_KEY = "procurement_code";
    private static ThreadLocal<String> userThreadLocal = new NamedThreadLocal<>("user");
    private static ThreadLocal<String> bearerUserThreadLocal = new NamedThreadLocal<>("bearerUser");
    private static ThreadLocal<String> userProcurementLocal = new NamedThreadLocal<>("userProcurement");

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

    public static List<Long> getProcurementId() {
        String userProcurement = userProcurementLocal.get();
        if (StringUtils.isEmpty(userProcurement)) {
            return Collections.EMPTY_LIST;
        }
        String[] procurementIdArray = userProcurement.split("###")[0].split(",");
        List<Long> procurementIdList = new ArrayList<>(procurementIdArray.length);
        for (String procurementId : procurementIdArray) {
            procurementIdList.add(NumberUtils.toLong(procurementId));
        }

        return procurementIdList;
    }

    public static List<String> getProcurementCode() {
        String userProcurement = userProcurementLocal.get();
        if (StringUtils.isEmpty(userProcurement)) {
            return Collections.EMPTY_LIST;
        }
        String[] procurementIdArray = userProcurement.split("###")[1].split(",");
        return Arrays.asList(procurementIdArray);
    }

    public static void setProcurement(Map<String, Object> userExtInfo) {
        if (userExtInfo.containsKey(USER_PROCUREMENT_ID_KEY) && userExtInfo.containsKey(USER_PROCUREMENT_CODE_KEY)) {
            userProcurementLocal.set(userExtInfo.get(USER_PROCUREMENT_ID_KEY).toString() + "###" + userExtInfo.get(USER_PROCUREMENT_CODE_KEY).toString());
        }
    }

    public static void removeProcurement() {
        userProcurementLocal.remove();
    }

    public static void clear() {
        removeUser();
        removeBearerUser();
        removeProcurement();
    }
}
