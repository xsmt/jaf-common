package cn.jcloud.jaf.common.security;

import java.lang.annotation.Annotation;

/**
 * Created by Wei Han on 2016/5/3.
 */
public interface IWebAnnotationHandler<T extends Annotation> {

    void init();

    void handle(T annotation, String methodSignature);

    void complete();

    Class<T> annotationClass();
}
