package cn.jcloud.jaf.common.taskschedule.domain;

/**
 * 任务调度：日志级别
 * Created by Wei Han on 2016/6/15.
 */
public enum LogLevel {
    /**
     * 不管最终处理成功或失败，都记录文件日志
     */
    NORMAL,
    /**
     * 最终处理失败才记录文件日志
     */
    ERROR
}
