package cn.jcloud.jaf.common.cs.core;

import cn.jcloud.jaf.common.config.JafContext;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 内容中心注册条件
 * Created by Wei Han on 2019/12/09.
 */
public class CSSupportCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return null != JafContext.class.getClassLoader().getResource("cs.properties");
    }
}
