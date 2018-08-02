package cn.jcloud.jaf.common.security;

import cn.jcloud.gaea.rest.security.services.visitor.OrganizationVisitorsAdapter;
import cn.jcloud.jaf.common.util.ReflectUtil;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 扩展游客模式,自定义是否放行当前api请求
 *
 * @author Wei Han
 */
public class SocialOrganizationVisitorsAdapter extends OrganizationVisitorsAdapter {


    @Override
    public boolean isPermit(HttpServletRequest request,
                            HttpServletResponse response, Object handler) {


        if (RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        // 获取当前请求的method
        if (handler instanceof HandlerMethod) {
            String requestMethod = ReflectUtil.getMethodSignature((HandlerMethod) handler);
            if (SecretHandler.isSecretMethod(requestMethod)) {
                return true;
            }
            if (GuestHandler.isCommonGuestMethod(requestMethod)) {
                return true;
            }
        }

        return super.isPermit(request, response, handler);
    }
}
