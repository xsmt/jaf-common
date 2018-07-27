package cn.jcloud.jaf.common.security;

import com.nd.social.common.config.SafContext;
import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import com.nd.social.common.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Wulj@ND on 2016/5/9.
 */
public class SecretAdapter implements SecurityAdapter {
    @Override
    public boolean isPermit(HttpServletRequest request,
                            HttpServletResponse response, Object handler) {
        // 获取当前请求的method
        if (handler instanceof HandlerMethod) {
            String requestMethod = ReflectUtil.getMethodSignature((HandlerMethod) handler);
            if (!SecretHandler.isSecretMethod(requestMethod)) {
                return false;
            }
            String secret = "nd" + DateFormatUtils.format(new Date(), "yyyyMMdd") + SafContext.getProjectName();
            if (StringUtils.equals(secret, request.getParameter("secret"))) {
                return true;
            }
            throw WafI18NException.of(ErrorCode.NO_PERMISSION);
        }
        return false;
    }
}
