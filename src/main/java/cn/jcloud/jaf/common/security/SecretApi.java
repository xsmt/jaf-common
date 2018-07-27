package cn.jcloud.jaf.common.security;

import java.lang.annotation.*;

/**
 * 是否为私密Api
 * 在请求路径上增加“?secret={$key}”,
 * key的取值方式：nd+当前日期(yyyyMMddHH)+项目名，如nd2016032916shop
 * 注：此类接口禁止在浏览器地址栏中使用，防止网络爬虫记录
 * Created by Wei Han on 2016/5/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface SecretApi {
}
