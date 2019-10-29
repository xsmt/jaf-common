package cn.jcloud.jaf.common.im.core;

import cn.jcloud.jaf.common.config.JafContext;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 菜单权限注册条件
 * Created by Wei Han on 2016/6/16.
 */
public class IMSupportCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return null != JafContext.class.getClassLoader().getResource("im.properties");
    }
}
