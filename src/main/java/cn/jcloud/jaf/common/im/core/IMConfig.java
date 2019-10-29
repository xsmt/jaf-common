package cn.jcloud.jaf.common.im.core;

import cn.jcloud.gaea.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Conditional(IMSupportCondition.class)
@Configuration
@PropertySource(value = "classpath:im.properties")
public class IMConfig {

    @Value("${im.path}")
    private String path;

    @Value("${im.host}")
    private String host;

    @Value("${im.target_url:/v0.1/message/target/{system_code}}")
    private String targetUrl;

    @Value("${im.message.instant_url:/v0.1/message/instant/{system_code}}")
    private String instantUrl;

    @Value("${im.message.task_url:/v0.1/message/task/{system_code}}")
    private String taskUrl;

    @Value("${im.message.cancel_url:/v0.1/message/task/{message_id}}")
    private String cancelUrl;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getInstantUrl() {
        return instantUrl;
    }

    public void setInstantUrl(String instantUrl) {
        this.instantUrl = instantUrl;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public String formatTargetCreateUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.targetUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.targetUrl);
    }

    public String formatSendInstantMessageUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.instantUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.instantUrl);
    }

    public String formatSendTaskMessageUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.taskUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.taskUrl);
    }

    public String formatCalcelkMessageUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.cancelUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.cancelUrl);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
