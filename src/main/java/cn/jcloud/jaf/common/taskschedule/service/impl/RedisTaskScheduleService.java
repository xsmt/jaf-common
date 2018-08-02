package cn.jcloud.jaf.common.taskschedule.service.impl;

import cn.jcloud.gaea.client.http.WafSecurityHttpClient;
import cn.jcloud.jaf.common.config.JafContext;
import cn.jcloud.jaf.common.handler.TenantHandler;
import cn.jcloud.jaf.common.taskschedule.core.TaskScheduleSupportCondition;
import cn.jcloud.jaf.common.taskschedule.domain.MsgInfo;
import cn.jcloud.jaf.common.taskschedule.domain.TsRegisterReq;
import cn.jcloud.jaf.common.taskschedule.service.TaskScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * Social任务调度基于Redis
 * Created by Wei Han on 2016/6/15.
 */
@Service
@Primary
@Conditional(TaskScheduleSupportCondition.class)
public class RedisTaskScheduleService implements TaskScheduleService {

    private static final String TS_REGISTER_URI = "list_infos";

    @Autowired
    @Qualifier("taskScheduleRedisTemplate")
    private RedisTemplate<String, MsgInfo> taskScheduleRedisTemplate;

    @Autowired
    private WafSecurityHttpClient httpClient;

    private String tsName;
    private String ticketUri;
    private String ticketPwd;

    public RedisTaskScheduleService() {
        this(JafContext.getProjectName(),
                JafContext.getTsTicketUri(),
                JafContext.getTsTicketPwd());
    }

    public RedisTaskScheduleService(String tsName) {
        this(tsName, null, null);
    }

    public RedisTaskScheduleService(String tsName, String ticketUri, String ticketPwd) {
        this.tsName = tsName;
        this.ticketUri = ticketUri;
        this.ticketPwd = ticketPwd;
    }

    public void setTaskScheduleRedisTemplate(RedisTemplate<String, MsgInfo> taskScheduleRedisTemplate) {
        this.taskScheduleRedisTemplate = taskScheduleRedisTemplate;
    }

    public void setHttpClient(WafSecurityHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * 向任务调度注册，如果为Spring管理的Bean则会自动注册
     */
    @PostConstruct
    public void registerTS() {
        String tsUri = JafContext.getTsUri();
        if (null == tsUri) {
            return;
        }

        TsRegisterReq req = TsRegisterReq.create()
                .name(tsName)
                .ticketUri(ticketUri)
                .ticketPwd(ticketPwd)
                .projectName(JafContext.getProjectName())
                .build();
        httpClient.post(tsUri + TS_REGISTER_URI, req);
    }

    @Override
    public void push(MsgInfo msgInfo) {
        if (null == msgInfo) {
            return;
        }
        String key = this.tsName + "_" + TenantHandler.getStrictTenant();
        taskScheduleRedisTemplate.opsForList().rightPush(key, msgInfo);
    }

    @Override
    public void pushAll(Collection<MsgInfo> msgInfoList) {
        if (CollectionUtils.isEmpty(msgInfoList)) {
            return;
        }
        String key = this.tsName + "_" + TenantHandler.getStrictTenant();
        taskScheduleRedisTemplate.opsForList().rightPushAll(key, msgInfoList);
    }
}
