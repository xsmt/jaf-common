package cn.jcloud.jaf.common.handler;

import cn.jcloud.gaea.rest.security.authens.UserInfo;
import cn.jcloud.gaea.rest.support.WafContext;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.security.BearerAdapter;
import cn.jcloud.jaf.common.security.SecretAdapter;
import cn.jcloud.jaf.common.tenant.service.TenantService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Wei Han
 */
public class AppInterceptor implements HandlerInterceptor {

    @Autowired
    private TenantService tenantService;

    private BearerAdapter bearerAdapter;

    private SecretAdapter secretAdapter;

    @Autowired(required = false)
    public void setBearerAdapter(BearerAdapter bearerAdapter) {
        this.bearerAdapter = bearerAdapter;
    }

    @Autowired(required = false)
    public void setSecretAdapter(SecretAdapter secretAdapter) {
        this.secretAdapter = secretAdapter;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        return validateRequest(request, response, handler);
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        TenantHandler.clear();
        UserHandler.clear();
        VOrgHandler.clear();
    }

    private boolean validateRequest(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (secretAdapter.isPermit(request, response, handler)) {
            return true;
        }

        String orgName = request.getHeader("Orgname");
        String vOrgName = request.getHeader("Vorg");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || "anonymousUser".equals(authentication.getPrincipal())) {
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(authorization)) {
                throw JafI18NException.of(ErrorCode.USER_NOT_LOGIN);
            }
            //访客模式，根据头部的orgName和vorg校验租户信息有效性
            tenantService.setGuestTenant(vOrgName, orgName);
            return true;
        }

        if (bearerAdapter.isPermit(request, response, handler)) {
            tenantService.setGuestTenant(vOrgName, orgName);
            return true;
        }

        //有Authentication，但没有userInfo的请求是异常请求
        //这里要记住userInfo需要再bearerAdapter.isPermit之后再获取，
        // 因为内部有替换userInfo操作
        UserInfo userInfo = WafContext.getCurrentUserInfo();
        if (null == userInfo) {
            throw JafI18NException.of(ErrorCode.USER_NOT_LOGIN);
        }
        UserHandler.setUser(userInfo.getUserId());

        String pOrgId = request.getParameter("orgId");
        tenantService.setUserTenant(vOrgName, pOrgId, userInfo);
        return true;
    }

}
