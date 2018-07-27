package cn.jcloud.jaf.common.security;

import java.util.*;

/**
 * Created by Wei Han on 2016/4/20.
 */
public class GuestHandler implements IWebAnnotationHandler<GuestApi> {

    private static List<String> commonWhiteList = Collections.emptyList();
    private static List<String> uncustomizableWhiteList = Collections.emptyList();
    private static Map<String, String> optionalWhiteMap = Collections.emptyMap();

    /**
     * 是否为基础白名单接口
     */
    public static boolean isCommonGuestMethod(String methodSignature) {
        return commonWhiteList.contains(methodSignature);
    }
    
    /**
     * 是否为后台不可配置的非基础白名单接口
     */
    public static boolean isUncustomizableWhiteMethod(String methodSignature) {
    	return uncustomizableWhiteList.contains(methodSignature);
    }

    /**
     * 后台可配置的非基础白名单接口Map
     * key为Controller方法，value为描述
     */
    public static Map<String, String> getOptionalWhiteMap() {
        return optionalWhiteMap;
    }

    @Override
    public void init() {
        commonWhiteList = new LinkedList<>();
        uncustomizableWhiteList = new LinkedList<>();
        optionalWhiteMap = new HashMap<>();
    }

    public void handle(GuestApi guestApi, String methodSignature) {
        if (guestApi.common()) {
            commonWhiteList.add(methodSignature);
        } else {
        	if (guestApi.customizable()) {
        		optionalWhiteMap.put(methodSignature, guestApi.value());
        	} else {
        		uncustomizableWhiteList.add(methodSignature);
        	}
        }
    }

    @Override
    public void complete() {
        commonWhiteList = Collections.unmodifiableList(commonWhiteList);
        uncustomizableWhiteList = Collections.unmodifiableList(uncustomizableWhiteList);
        optionalWhiteMap = Collections.unmodifiableMap(optionalWhiteMap);
    }

    @Override
    public Class<GuestApi> annotationClass() {
        return GuestApi.class;
    }
}
