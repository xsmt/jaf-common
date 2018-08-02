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
     * 是否为基础白名单，true为基础白名单，false为可选白名单，默认true
     */
    boolean common() default true;
}
