package cn.jcloud.jaf.common.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 私密Api处理类
 * Created by Wei Han on 2016/5/5.
 */
public class SecretHandler implements IWebAnnotationHandler<SecretApi> {

    private static Set<String> secretMap = Collections.emptySet();

    public static boolean isSecretMethod(String methodSignature) {
        return secretMap.contains(methodSignature);
    }

    @Override
    public void init() {
        secretMap = new HashSet<>();
    }

    @Override
    public void handle(SecretApi annotation, String methodSignature) {
        secretMap.add(methodSignature);
    }

    @Override
    public void complete() {
        secretMap = Collections.unmodifiableSet(secretMap);
    }

    @Override
    public Class<SecretApi> annotationClass() {
        return SecretApi.class;
    }
}
