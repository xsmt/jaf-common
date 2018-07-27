package cn.jcloud.jaf.common.security;

import java.lang.annotation.*;

/**
 * 限制Api仅能使用Bearer授权才能访问
 * Created by Wei Han on 2016/5/3.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface BearerApi {

    /**
     * bearer授权的用户(suid)集合所在的分组.
     * 当{code isNoRequireSuid=false}时该参数无效
     */
    String group() default "common";

    /**
     * bearer授权时，是否需要指定suid,即是否需要代理成suid指定的用户，
     * REQUIRED suid必须有，请求会代理成suid指定的用户
     * OPTIONAL suid可选，当有时，请求会代理成suid指定的用户
     * IGNORE suid忽略，请求不会代理成suid指定的用户
     */
    SuidRequired suidRequired() default SuidRequired.REQUIRED;
}
