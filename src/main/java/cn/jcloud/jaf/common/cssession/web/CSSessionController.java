package cn.jcloud.jaf.common.cssession.web;

import cn.jcloud.gaea.WafException;
import cn.jcloud.gaea.rest.security.authens.UserInfo;
import cn.jcloud.jaf.common.cssession.core.CSSessionSupportCondition;
import cn.jcloud.jaf.common.cssession.domain.CSSession;
import cn.jcloud.jaf.common.cssession.service.CSSessionService;
import cn.jcloud.jaf.common.handler.TenantHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Conditional(CSSessionSupportCondition.class)
@RestController
@RequestMapping("/${version}/cs")
public class CSSessionController {

    @Autowired
    private CSSessionService service;

    /**
     * 从内容服务获取session
     */
    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public CSSession getSessionFromCs(@AuthenticationPrincipal UserInfo userInfo) throws WafException {

        String userId = userInfo.getUserId();
        long orgId = TenantHandler.getStrictTenant();
        return service.getSession(userId, String.valueOf(orgId));
    }
}