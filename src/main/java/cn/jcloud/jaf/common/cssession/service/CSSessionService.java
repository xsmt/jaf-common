package cn.jcloud.jaf.common.cssession.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import cn.jcloud.gaea.client.auth.WafBearerTokenService;
import cn.jcloud.gaea.client.http.WafSecurityHttpClient;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.cssession.core.CSConfig;
import cn.jcloud.jaf.common.cssession.core.CSSessionSupportCondition;
import cn.jcloud.jaf.common.cssession.domain.CSSession;
import cn.jcloud.jaf.common.exception.JafI18NException;

/**
 * Created by Wei Han on 2016-08-11.
 */
@Conditional(CSSessionSupportCondition.class)
@Service
public class CSSessionService {

    @Autowired
    private CSConfig csConfig;

    @Autowired
    private WafSecurityHttpClient httpClient;

    @Autowired
    private WafBearerTokenService wafBearerTokenService;

    public CSSession getSession() {
        return getSession("");
    }

    public CSSession getSession(String appendPath) {
        String userId = wafBearerTokenService.getBearerToken().getUserId();
        return getSession(userId, appendPath);
    }

    public CSSession getAdminSession() {
        String userId = wafBearerTokenService.getBearerToken().getUserId();
        return getSession(userId, "", "admin");
    }

    public CSSession getSession(String userId, String appendPath) {
        return getSession(userId, appendPath, csConfig.getRole());
    }

    public CSSession getSession(String userId, String appendPath, String role) {

        Map<String, Object> param = new HashMap<>();
        param.put("path", csConfig.getPath() + appendPath);
        param.put("uid", userId);
        param.put("role", role);
        param.put("service_id", csConfig.getServiceId());
        param.put("expires", csConfig.getExpires());

        try {
            CSSession csSession = httpClient.postForObject(csConfig.getSessionUrl(), param, CSSession.class);
            if (csSession == null) {
                throw JafI18NException.of(ErrorCode.CS_SESSION_NG);
            }
            return csSession;
        } catch (Exception e) {
            throw JafI18NException.of(e.getMessage());
        }
    }
}
