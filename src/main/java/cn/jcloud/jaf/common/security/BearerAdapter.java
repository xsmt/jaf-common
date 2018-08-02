package cn.jcloud.jaf.common.security;

import cn.jcloud.gaea.rest.security.authens.UserCenterRoleDetails;
import cn.jcloud.gaea.rest.security.authens.UserCenterUserDetails;
import cn.jcloud.gaea.rest.security.authens.UserInfo;
import cn.jcloud.gaea.rest.security.authens.WafUserAuthentication;
import cn.jcloud.gaea.rest.security.services.WafUserDetailsService;
import cn.jcloud.gaea.rest.support.WafContext;
import cn.jcloud.jaf.common.config.JafContext;
import cn.jcloud.jaf.common.constant.AuthorizationTypePrefix;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.handler.UserHandler;
import cn.jcloud.jaf.common.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wei Han@ND on 2016/5/9.
 */
public class BearerAdapter implements SecurityAdapter {

    @Autowired
    private WafUserDetailsService wafUserDetailsService;

    @Override
    public boolean isPermit(HttpServletRequest request,
                            HttpServletResponse response, Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || "anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }

        String requestMethod = ReflectUtil.getMethodSignature((HandlerMethod) handler);

        if (!AuthorizationTypePrefix.BEARER.equalsIgnoreCase(WafContext.getCurrentUserInfo().getUserType())) {
            if (BearerHandler.isBearerMethod(requestMethod)) {
                throw JafI18NException.of(ErrorCode.WRONG_AUTH_TYPE, AuthorizationTypePrefix.BEARER);
            } else {
                return false;
            }
        }

        String bearerUid = WafContext.getCurrentUserInfo().getUserId();
        if (BearerHandler.isBearerUidPermit(requestMethod, bearerUid)) {
            UserHandler.setBearerUser(bearerUid);
        } else {
            throw JafI18NException.of(ErrorCode.NO_PERMISSION);
        }

        if (BearerHandler.isIgnoreSuid(requestMethod)) {
            return true;
        }

        String suid = request.getParameter("suid");

        if (StringUtils.isEmpty(suid)) {
            if (BearerHandler.isOptionalSuid(requestMethod)) {
                return true;
            }
            throw JafI18NException.of(ErrorCode.MISSING_SUID);
        }
        replaceUserInfo(suid);
        return false;
    }

    private void replaceUserInfo(String suid) {
        UserInfo userInfo;
        try {
            userInfo = wafUserDetailsService.getUserInfo(suid, JafContext.getProjectName());
        } catch (Exception e) {
            throw JafI18NException.of(ErrorCode.USER_NOT_EXIST);
        }
        if (userInfo == null) {
            throw JafI18NException.of(ErrorCode.USER_NOT_LOGIN);
        }
        Map<String, Object> orgExInfo = userInfo.getOrgExinfo();
        if (!orgExInfo.containsKey("org_id") || StringUtils.isEmpty(orgExInfo.get("org_id").toString())) {
            throw JafI18NException.of(ErrorCode.MISSING_ORG_ID);
        }

        // 修改用户
        List<UserCenterRoleDetails> roleDetailsCollection = new ArrayList<>();
        UserCenterUserDetails userDetails = new UserCenterUserDetails(userInfo, roleDetailsCollection);
        // 管理接口调用跳过权限验证时使用
        userDetails.getUserInfo().setUserType(AuthorizationTypePrefix.BEARER);

        WafUserAuthentication wafAuthentication = new WafUserAuthentication(userDetails.getAuthorities());
        wafAuthentication.setAuthenticated(true);
        wafAuthentication.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(wafAuthentication);
    }
}
