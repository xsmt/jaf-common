package cn.jcloud.jaf.common.security;

import java.util.*;

/**
 * Created by Wei Han on 2016/4/20.
 */
public class GuestHandler implements IWebAnnotationHandler<GuestApi> {

    private static List<String> commonWhiteList = Collections.emptyList();
    private static Map<String, String> optionalWhiteMap = Collections.emptyMap();

    public static boolean isCommonGuestMethod(String methodSignature) {
        return commonWhiteList.contains(methodSignature);
    }

    /**
     * 获取可选白名单列表，key为Controller方法，value为描述
     */
    public static Map<String, String> getOptionalWhiteMap() {
        return optionalWhiteMap;
    }

    @Override
    public void init() {
        commonWhiteList = new LinkedList<>();
        optionalWhiteMap = new HashMap<>();
    }

    public void handle(GuestApi guestApi, String methodSignature) {
        if (guestApi.common()) {
            commonWhiteList.add(methodSignature);
        } else {
            optionalWhiteMap.put(methodSignature, guestApi.value());
        }
    }

    @Override
    public void complete() {
        commonWhiteList = Collections.unmodifiableList(commonWhiteList);
        optionalWhiteMap = Collections.unmodifiableMap(optionalWhiteMap);
    }

    @Override
    public Class<GuestApi> annotationClass() {
        return GuestApi.class;
    }
}
