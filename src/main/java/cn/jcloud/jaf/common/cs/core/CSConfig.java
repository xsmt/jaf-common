package cn.jcloud.jaf.common.cs.core;

import cn.jcloud.gaea.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Conditional(CSSupportCondition.class)
@Configuration
@PropertySource(value = "classpath:cs.properties")
public class CSConfig {

    @Value("${cs.host}")
    private String host;

    @Value("${cs.enum.path}")
    private String enumPath;

    @Value("${cs.enum.init_url:/v0.1/enums}")
    private String enumUrl;

    @Value("${cs.sequence.init_url:/v0.1/sequences}")
    private String sequenceInitUrl;

    @Value("${im.sequence.next_url:/v0.1/sequences/{code}}")
    private String sequenceNextUrl;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEnumPath() {
        return enumPath;
    }

    public void setEnumPath(String enumPath) {
        this.enumPath = enumPath;
    }

    public String getEnumUrl() {
        return enumUrl;
    }

    public void setEnumUrl(String enumUrl) {
        this.enumUrl = enumUrl;
    }

    public String getSequenceInitUrl() {
        return sequenceInitUrl;
    }

    public void setSequenceInitUrl(String sequenceInitUrl) {
        this.sequenceInitUrl = sequenceInitUrl;
    }

    public String getSequenceNextUrl() {
        return sequenceNextUrl;
    }

    public void setSequenceNextUrl(String sequenceNextUrl) {
        this.sequenceNextUrl = sequenceNextUrl;
    }

    public String formatEnumCreateUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.enumUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.enumUrl);
    }

    public String formatSequenceCreateUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.sequenceInitUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.sequenceInitUrl);
    }

    public String formatSequenceNextUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.sequenceNextUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.sequenceNextUrl);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
