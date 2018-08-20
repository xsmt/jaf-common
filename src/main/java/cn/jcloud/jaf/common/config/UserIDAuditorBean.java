package cn.jcloud.jaf.common.config;

import cn.jcloud.jaf.common.handler.UserHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class UserIDAuditorBean implements AuditorAware<Long> {
    @Override
    public Long getCurrentAuditor() {
        String userIdForRest = UserHandler.getUser();
        if (StringUtils.isEmpty(userIdForRest)) {
            return null;
        }

        return NumberUtils.toLong(userIdForRest,Long.MIN_VALUE);
    }
}
