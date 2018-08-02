package cn.jcloud.jaf.common.taskschedule.service;

import cn.jcloud.jaf.common.taskschedule.domain.MsgInfo;

import java.util.Collection;

/**
 * 任务队列
 * Created by Wei Han on 2016/6/15.
 */
public interface TaskScheduleService {
    void push(MsgInfo msgInfo);

    void pushAll(Collection<MsgInfo> msgInfoList);
}
