package cn.jcloud.jaf.common.util;

import com.nd.gaea.rest.security.authens.UserInfo;
import com.nd.gaea.rest.security.services.WafUserDetailsService;
import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import com.nd.social.common.handler.SpringContextHolder;
import com.nd.social.common.handler.TenantHandler;
import com.nd.social.common.handler.VOrgHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by Wei Han on 2016-09-18.
 */
public class UCHelper {

    public static boolean isSameTenant(String uid) {
        Assert.notNull(uid, "[uid] must not null");
        WafUserDetailsService wafUserDetailsService = SpringContextHolder.getBean("wafUserDetailsService",
                WafUserDetailsService.class);
        String vOrgId = VOrgHandler.getVOrgId();
        Long tenant = TenantHandler.getTenant();
        if (null == tenant) {
            return false;
        }
        Map<String, Object> orgExinfo = null;
        UserInfo userInfo = wafUserDetailsService.getUserInfo(uid, null);
        try {
            orgExinfo = userInfo.getOrgExinfo();
        } catch (Exception e) {
            throw WafI18NException.of(ErrorCode.USER_NOT_EXIST);
        }
        if (StringUtils.isNotBlank(vOrgId)) {
            try {
                userInfo = wafUserDetailsService.getVorgUserInfo(uid, null, vOrgId);
                orgExinfo = userInfo.getOrgExinfo();
            } catch (Exception e) {
                return false;
            }
        }
        if (tenant.toString().equals(vOrgId)) {
            return userInfo != null;
        }
        else {
            return orgExinfo != null && orgExinfo.containsKey("org_id")
                    && tenant.toString().equals(orgExinfo.get("org_id").toString());
        }
    }
    
    public static String getUserOrgId(UserInfo userInfo) {

    	if(userInfo == null){
    		throw WafI18NException.of(ErrorCode.USER_NOT_LOGIN);
    	}
    	
        Map<String, Object> orgExinfo = userInfo.getOrgExinfo();
        if (orgExinfo != null && orgExinfo.get("org_id") != null) {
            return orgExinfo.get("org_id").toString();
        } else {
            throw WafI18NException.of(ErrorCode.MISSING_ORG_ID);
        }
    }
}
