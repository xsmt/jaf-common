package cn.jcloud.jaf.common.im.service;

import cn.jcloud.gaea.client.http.WafSecurityHttpClient;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.im.core.IMConfig;
import cn.jcloud.jaf.common.im.core.IMSupportCondition;
import cn.jcloud.jaf.common.im.core.MessageTemplateParser;
import cn.jcloud.jaf.common.im.domain.Message;
import cn.jcloud.jaf.common.im.domain.MessageTemplate;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Conditional(IMSupportCondition.class)
public class IMMessageService {

    private static Logger LOGGER = LoggerFactory.getLogger(IMMessageService.class);

    @Autowired
    private WafSecurityHttpClient httpClient;

    @Autowired
    private IMConfig imConfig;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MessageTemplateParser messageTemplateParser;

    private final static Map<String, MessageTemplate> TEMPLATE_CACHE = new HashMap<>();

    /**
     * 初始化缓存
     */
    private void initTemplateCache() throws IOException {

        Resource messageTemplateResource = new ClassPathResource(imConfig.getPath());
        if (!messageTemplateResource.exists()) {
            throw JafI18NException.of(ErrorCode.DATA_NOT_FOUND);
        }

        List<MessageTemplate> messageTemplates = messageTemplateParser.parse(messageTemplateResource.getInputStream());
        for (MessageTemplate messageTemplate : messageTemplates) {
            httpClient.postForObject(imConfig.formatTargetCreateUrl(), messageTemplate, Map.class, messageTemplate.getSystemCode());
            TEMPLATE_CACHE.put(messageTemplate.getAction(), messageTemplate);
        }
    }


    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            initTemplateCache();
            LOGGER.info("message template create success");
        } catch (IOException e) {
            LOGGER.error("message template format error:", e);
        }
    }

    public Long sendMessageToOrg(String action, Long receiveOrgId) {
        return sendMessageToOrg(action, null, Collections.EMPTY_MAP, receiveOrgId);
    }

    public Long sendMessageToOrg(String action, String target, Map<String, Object> param, Long receiveOrgId) {
        return sendMessageToOrg(action, target, param, Collections.singletonList(receiveOrgId));
    }

    public Long sendMessageToOrg(String action, String target, Map<String, Object> param, List<Long> receiveOrgIds) {
        return sendMessage(action, target, null, null, null, null, param, Collections.EMPTY_LIST, receiveOrgIds);
    }

    public Long sendMessageToUser(String action, Long receiver) {
        return sendMessageToUser(action, null, Collections.EMPTY_MAP, receiver);
    }

    public Long sendMessageToUser(String action, String target, Map<String, Object> param, Long receiver) {
        return sendMessageToUser(action, target, param, Collections.singletonList(receiver));
    }

    public Long sendMessageToUser(String action, String target, Map<String, Object> param, List<Long> receivers) {
        return sendMessage(action, target, null, null, null, null, param, receivers, Collections.EMPTY_LIST);
    }

    public Long sendTaskMessageToOrg(String action, Date sendTime, String checkUrl, Long receiveOrgId) {
        return sendTaskMessageToOrg(action, null, sendTime, checkUrl, Collections.EMPTY_MAP, receiveOrgId);
    }

    public Long sendTaskMessageToOrg(String action, String target, Date sendTime, String checkUrl, Map<String, Object> param, Long receiveOrgId) {
        return sendTaskMessageToOrg(action, target, sendTime, checkUrl, param, ImmutableList.of(receiveOrgId));
    }

    public Long sendTaskMessageToOrg(String action, String target, Date sendTime, String checkUrl, Map<String, Object> param, List<Long> receiveOrgIds) {
        return sendMessage(action, target, null, checkUrl, sendTime, null, param, null, receiveOrgIds);
    }

    public Long sendTaskMessageToUser(String action, Date sendTime, String checkUrl, Long receiver) {
        return sendTaskMessageToUser(action, null, sendTime, checkUrl, Collections.EMPTY_MAP, receiver);
    }

    public Long sendTaskMessageToUser(String action, String target, Date sendTime, String checkUrl, Map<String, Object> param, Long receiver) {
        return sendTaskMessageToUser(action, target, sendTime, checkUrl, param, Collections.singletonList(receiver));
    }

    public Long sendTaskMessageToUser(String action, String target, Date sendTime, String checkUrl, Map<String, Object> param, List<Long> receivers) {
        return sendMessage(action, target, null, checkUrl, sendTime, null, param, receivers, Collections.EMPTY_LIST);
    }
    public Long sendMessage(String action, String target, String hrefUrl, String checkUrl, Date sendTime, String callBackUrl, Map<String, Object> param, List<Long> receivers, List<Long> receiveOrgIds) {
        Message message = Message.create()
                .action(action)
                .target(target)
                .hrefUrl(hrefUrl)
                .checkUrl(checkUrl)
                .callBackUrl(callBackUrl)
                .sendTime(sendTime)
                .param(param)
                .receivers(receivers)
                .receiveOrgIds(receiveOrgIds)
                .build();
        return sendMessage(message);
    }

    public Long sendMessage(Message message) {
        if (!TEMPLATE_CACHE.containsKey(message.getAction())) {
            throw JafI18NException.of("消息模板\"{0}\"未定义", message.getAction());
        }
        MessageTemplate messageTemplate = TEMPLATE_CACHE.get(message.getAction());
        String sendUrl = (message.getSendTime() != null && message.getSendTime().after(new Date())) ? imConfig.formatSendTaskMessageUrl() : imConfig.formatSendInstantMessageUrl();
        Map result = httpClient.postForObject(sendUrl, message, Map.class, messageTemplate.getSystemCode());
        return NumberUtils.toLong(result.get("id").toString(), 0L);
    }

    public void cancelTaskMessage(Long messageId) {
        httpClient.deleteForObject(imConfig.formatCalcelkMessageUrl(), Map.class, messageId);
    }
}
