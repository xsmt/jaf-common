package cn.jcloud.jaf.common.taskschedule.domain;

/**
 * 任务调度：消息类型
 * Created by Wei Han on 2016/6/15.
 */
public enum MsgType {
    //直接进行URL重发的简单业务，无需再解析addition的专用业务信息
    REPOST,
    //im业务消息专用,需解析addition的信息并处理。非REPOST类型的消息，如有url字段，也会对url字段进行相应的转发处理
    IM,
    //多语言im业务消息专用,需解析addition的信息并处理
    IM_MULTI_LANG;
}
