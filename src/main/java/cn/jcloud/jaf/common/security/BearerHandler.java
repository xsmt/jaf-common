package cn.jcloud.jaf.common.security;

import com.nd.social.common.config.SafContext;

import java.util.*;

/**
 * 注解BearerAccessApi处理类
 * Created by Wei Han on 2016/5/3.
 */
public class BearerHandler implements IWebAnnotationHandler<BearerApi> {

    private static Map<String, String> bearerMap = Collections.emptyMap();
    private static Set<String> optionalSuidMap = Collections.emptySet();
    private static Set<String> ignoreSuidMap = Collections.emptySet();

    public static boolean isBearerUidPermit(String methodSignature, String uid) {
        String bearerUid = bearerMap.get(methodSignature);
        if (bearerUid == null) {
            return true;
        }
        return bearerUid.contains(uid);
    }

    public static boolean isBearerMethod(String methodSignature) {
        return bearerMap.containsKey(methodSignature);
    }

    public static boolean isOptionalSuid(String methodSignature) {
        return optionalSuidMap.contains(methodSignature);
    }

    public static boolean isIgnoreSuid(String methodSignature) {
        return ignoreSuidMap.contains(methodSignature);
    }

    @Override
    public void init() {
        bearerMap = new HashMap<>();
        optionalSuidMap = new HashSet<>();
        ignoreSuidMap = new HashSet<>();
    }

    @Override
    public void handle(BearerApi annotation, String methodSignature) {
        String uid = SafContext.defaultBearerUid(annotation.group());
        bearerMap.put(methodSignature, uid);
        if (SuidRequired.OPTIONAL.equals(annotation.suidRequired())) {
            optionalSuidMap.add(methodSignature);
        } else if (SuidRequired.IGNORE.equals(annotation.suidRequired())) {
            ignoreSuidMap.add(methodSignature);
        }
    }

    @Override
    public void complete() {
        bearerMap = Collections.unmodifiableMap(bearerMap);
    }

    @Override
    public Class<BearerApi> annotationClass() {
        return BearerApi.class;
    }
}
