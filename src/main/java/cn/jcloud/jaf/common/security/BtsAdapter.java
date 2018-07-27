package cn.jcloud.jaf.common.security;

import com.nd.gaea.WafProperties;
import com.nd.gaea.client.support.CConfKeys;
import com.nd.gaea.rest.security.authens.*;
import com.nd.gaea.rest.security.services.WafUserDetailsService;
import com.nd.social.common.config.SafContext;
import com.nd.social.common.constant.AuthorizationTypePrefix;
import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import com.nd.social.common.handler.UserHandler;
import com.nd.social.common.util.ReflectUtil;
import com.nd.social.common.util.UCHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: Module Information         </p>
 * <p>Description: Function Description </p>
 * <p>Copyright: Copyright (c) 2017     </p>
 * <p>Company: ND Co., Ltd.       </p>
 * <p>Create Time: 2017年1月26日           </p>
 * @author Linhua(831008)
 * <p>Update Time:                      </p>
 * <p>Updater:                          </p>
 * <p>Update Comments:                  </p>
 */
public class BtsAdapter implements SecurityAdapter {

    @Autowired
    private WafUserDetailsService wafUserDetailsService;

    /**
     * 返回true，判定为bts请求
     */
    @Override
    public boolean isPermit(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || "anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }
        String requestMethod = ReflectUtil.getMethodSignature((HandlerMethod) handler);
        boolean btsReq = authentication instanceof BtsTokenAuthentication;
        boolean btsEnabled = StringUtils.isNotBlank(WafProperties.getProperty(CConfKeys.WAF_BTS_URI.getKey()));
        if (BtsHandler.isBtsMethod(requestMethod) && btsEnabled) {
            if (!btsReq) {
                throw WafI18NException.of(ErrorCode.WRONG_AUTH_TYPE, AuthorizationTypePrefix.BTS);
            }
        }
        if (btsReq) { // bts请求，强制要求suid参数
            String suid = request.getParameter("suid");
            if (StringUtils.isBlank(suid)) {
                throw WafI18NException.of(ErrorCode.MISSING_SUID);
            }
            replaceUserInfo(suid);
            BtsTokenAuthentication btsTokenAuthentication = (BtsTokenAuthentication) authentication;
            Long appId = btsTokenAuthentication.getTokenEntity().getAppId();
            UserHandler.setBearerUser(appId.toString()); // BTS请求的beareruid记录为appId
            return true;
        }
        return false;
    }

    private void replaceUserInfo(String suid) {
        UserInfo userInfo;
        try {
            userInfo = wafUserDetailsService.getUserInfo(suid, SafContext.getProjectName());
        } catch (Exception e) {
            throw WafI18NException.of(ErrorCode.USER_NOT_EXIST);
        }
        
        UCHelper.getUserOrgId(userInfo);
        
        // 修改用户
        List<UserCenterRoleDetails> roleDetailsCollection = new ArrayList<>();
        UserCenterUserDetails userDetails = new UserCenterUserDetails(userInfo, roleDetailsCollection);
        // 管理接口调用跳过权限验证时使用
        userDetails.getUserInfo().setUserType(AuthorizationTypePrefix.BTS);
        WafUserAuthentication wafAuthentication = new WafUserAuthentication(userDetails.getAuthorities(),
                WafAbstractAuthenticationToken.AUTH_TYPE_BTS);
        wafAuthentication.setAuthenticated(true);
        wafAuthentication.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(wafAuthentication);
    }
}
