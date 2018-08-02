/* =============================================================
 * Created: [2015/7/21 11:11] by Wei Han
 * =============================================================
 *
 * Copyright 2014-2015 NetDragon Websoft Inc. All Rights Reserved
 *
 * =============================================================
 */
package cn.jcloud.jaf.common.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wei Han.
 * @since 0.2
 */
public class JoinPointUtil {
    public static List<Map> getMethodParameters(JoinPoint joinPoint) {
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Object[] parameterValues = joinPoint.getArgs();
        List<Map> params = new ArrayList<>();
        for (int i = 0; i < parameterNames.length; i++) {
            Map map = new HashMap();
            map.put("name", parameterNames[i]);
            map.put("type", parameterTypes[i]);
            map.put("value", parameterValues[i]);
            params.add(map);
        }
        return params;
    }

    public static RequestMethod getRequestMethod(JoinPoint joinPoint) {
        RequestMethod[] requestMethods = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(RequestMapping.class).method();
        if (requestMethods.length == 0) return RequestMethod.GET;
        else return requestMethods[0];

    }

    private static String getRequestMappingValue(JoinPoint joinPoint){
        String[] values = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(RequestMapping.class).value();
        return values.length > 0 ? values[0] : "";
    }

    public static String getClassName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getSimpleName();
    }

    public static String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

    public static String getRequestUrl(JoinPoint joinPoint){
        try{
            RequestMapping requestMapping =  joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
            return requestMapping.value()[0].trim() + getRequestMappingValue(joinPoint).trim();
        }catch (NullPointerException e){
            return getRequestMappingValue(joinPoint);
        }
    }
}
