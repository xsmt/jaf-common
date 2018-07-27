package cn.jcloud.jaf.common.security;

import java.lang.annotation.*;

/**
 * 白名单Api注解
 * Created by Wei Han on 2016/4/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface GuestApi {
    /**
     * 白名单描述，建议当common=false时，填写此值
     */
    String value() default "";

    /**
     * 是否为基础白名单，默认true
     * 基础白名单接口没有租户信息，不在后台配置
     * 非基础白名单必需有租户信息
     */
    boolean common() default true;
    
    /**
     * 是否后台可配置，只针对非基础白名单配置有效，默认true
     */
    boolean customizable() default true;
}
