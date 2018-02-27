package cn.jcloud.jaf.common.cache.core;

import cn.jcloud.jaf.common.config.JafContext;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 本地缓存组件注册条件
 * Created by Wei Han on 2016/6/20.
 */
public class LocalCacheSupportCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !JafContext.isRedisSupport();
    }
}
