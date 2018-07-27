package cn.jcloud.jaf.common.security;

/**
 * Created by Wei Han on 2016-08-16.
 */
public enum SuidRequired {
    /**
     * suid必须要有
     */
    REQUIRED,
    /**
     * suid可选，当包含时，使用suid进行org解析
     */
    OPTIONAL,
    /**
     * suid忽略
     */
    IGNORE
}
