package cn.jcloud.jaf.common.security;

import cn.jcloud.jaf.common.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Wei Han on 2016/5/3.
 */
public class WebAnnotationParser implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)
    private List<IWebAnnotationHandler> handlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        init();
        handle(event.getApplicationContext());
        complete();
    }

    private void init() {
        for (IWebAnnotationHandler handler : handlers) {
            handler.init();
        }
    }

    private void handle(ApplicationContext applicationContext) {
        String[] controllerBeanNames = applicationContext.getBeanNamesForAnnotation(Controller.class);
        for (String beanName : controllerBeanNames) {
            Class controllerType = ClassUtils.getUserClass(applicationContext.getType(beanName));
            for (IWebAnnotationHandler handler : handlers) {
                handleClass(controllerType, handler);
            }

        }
    }

    private void handleClass(Class controllerType, IWebAnnotationHandler handler) {
        Annotation classLevelAnnotation = AnnotationUtils.findAnnotation(controllerType, handler.annotationClass());
        Method[] controllerMethods = controllerType.getDeclaredMethods();
        for (Method method : controllerMethods) {
            handleMethod(handler, classLevelAnnotation, method);
        }
    }

    private void handleMethod(IWebAnnotationHandler handler, Annotation classLevelAnnotation, Method method) {
        Annotation methodLevelAnnotation = AnnotationUtils.findAnnotation(method, handler.annotationClass());
        if (methodLevelAnnotation == null) {
            if (classLevelAnnotation == null) {
                return;
            }
            methodLevelAnnotation = classLevelAnnotation;
        }
        handler.handle(methodLevelAnnotation, ReflectUtil.getMethodSignature(method));
    }

    private void complete() {
        for (IWebAnnotationHandler handler : handlers) {
            handler.complete();
        }
    }
}
