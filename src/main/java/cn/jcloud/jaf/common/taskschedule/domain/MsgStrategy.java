package cn.jcloud.jaf.common.taskschedule.domain;

/**
 * 任务调度：消息策略
 * Created by Wei Han on 2016/6/15.
 */
public class MsgStrategy {
    private Integer times;
    private Integer interval;
    private Long deadline;
    private Long starttime;

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public Long getStarttime() {
        return starttime;
    }

    public void setStarttime(Long starttime) {
        this.starttime = starttime;
    }
}
