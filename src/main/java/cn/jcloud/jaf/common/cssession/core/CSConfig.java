package cn.jcloud.jaf.common.cssession.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by Wei Han on 2016-08-11.
 */
@Conditional(CSSessionSupportCondition.class)
@Configuration
@PropertySource(value = "classpath:cs.properties")
public class CSConfig {

    @Value("${cs.serviceId}")
    private String serviceId;

    @Value("${cs.path}")
    private String path;

    @Value("${cs.sessionUrl}")
    private String sessionUrl;

    @Value("${cs.role:user}")
    private String role;

    @Value("${cs.expires:1800}")
    private long expires = 0;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPath() {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    @Bean
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
