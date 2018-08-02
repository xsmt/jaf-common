package cn.jcloud.jaf.common.taskschedule.core;

import cn.jcloud.jaf.common.config.JafContext;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 任务调度组件注册条件
 * Created by Wei Han on 2016/6/16.
 */
public class TaskScheduleSupportCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return JafContext.isTaskScheduleSupport();
    }
}
