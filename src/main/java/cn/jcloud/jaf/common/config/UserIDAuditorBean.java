package cn.jcloud.jaf.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class UserIDAuditorBean implements AuditorAware<Long> {
    @Override
    public Long getCurrentAuditor() {
        return 1L;
    }
}
