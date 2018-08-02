package cn.jcloud.jaf.common.security;

import cn.jcloud.jaf.common.config.JafContext;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Wei Han@ND on 2016/5/9.
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
            String secret = "nd" + DateFormatUtils.format(new Date(), "yyyyMMdd") + JafContext.getProjectName();
            if (StringUtils.equals(secret, request.getParameter("secret"))) {
                return true;
            }
            throw JafI18NException.of(ErrorCode.NO_PERMISSION);
        }
        return false;
    }
}
