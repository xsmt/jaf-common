package cn.jcloud.jaf.common.cssession.core;

import cn.jcloud.jaf.common.config.JafContext;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by Wei Han on 2016-08-11.
 */
public class CSSessionSupportCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return null != JafContext.class.getClassLoader().getResource("cs.properties");
    }
}
