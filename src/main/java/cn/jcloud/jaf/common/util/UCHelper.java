package cn.jcloud.jaf.common.util;

import cn.jcloud.gaea.rest.security.authens.UserInfo;
import cn.jcloud.gaea.rest.security.services.WafUserDetailsService;
import cn.jcloud.jaf.common.handler.SpringContextHolder;
import cn.jcloud.jaf.common.handler.TenantHandler;
import cn.jcloud.jaf.common.handler.VOrgHandler;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by Wei Han on 2016-09-18.
 */
public class UCHelper {

    public static boolean isSameTenant(String uid) {
        Assert.notNull(uid, "[uid] must not null");
        WafUserDetailsService wafUserDetailsService
                = SpringContextHolder.getBean("wafUserDetailsService", WafUserDetailsService.class);
        String vOrgId = VOrgHandler.getVOrgId();
        Long tenant = TenantHandler.getTenant();
        if (null == tenant) {
            return false;
        }
        if (tenant.toString().equals(vOrgId)) {
            UserInfo userInfo = wafUserDetailsService.getVorgUserInfo(String.valueOf(uid), null, vOrgId);
            return userInfo != null;
        } else {
            UserInfo userInfo = wafUserDetailsService.getUserInfo(uid, null);
            Map orgExInfo = userInfo.getOrgExinfo();
            return orgExInfo != null
                    && orgExInfo.containsKey("org_id")
                    && tenant.toString().equals(orgExInfo.get("org_id").toString());
        }
    }
}
