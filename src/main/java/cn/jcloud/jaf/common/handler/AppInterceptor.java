package cn.jcloud.jaf.common.handler;

import com.nd.gaea.rest.security.authens.UserInfo;
import com.nd.gaea.rest.support.WafContext;
import com.nd.social.common.approuter.service.AppRouterService;
import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import com.nd.social.common.security.BearerAdapter;
import com.nd.social.common.security.BtsAdapter;
import com.nd.social.common.security.SecretAdapter;
import com.nd.social.common.tenant.service.TenantService;
import com.nd.social.common.util.RequestUtil;
import com.nd.social.common.util.UCHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hasayaki(125473)
 *
 */
public class AppInterceptor implements HandlerInterceptor {

	@Autowired
	private AppRouterService appRouterService;
    @Autowired
    private TenantService tenantService;
    private BearerAdapter bearerAdapter;
    private SecretAdapter secretAdapter;
    private BtsAdapter btsAdapter;

    @Autowired(required = false)
    public void setBearerAdapter(BearerAdapter bearerAdapter) {
        this.bearerAdapter = bearerAdapter;
    }

    @Autowired(required = false)
    public void setSecretAdapter(SecretAdapter secretAdapter) {
        this.secretAdapter = secretAdapter;
    }

    @Autowired(required = false)
    public void setBtsAdapter(BtsAdapter btsAdapter) {
        this.btsAdapter = btsAdapter;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
    	
    	// 跨域请求
		if(request.getMethod().equals(RequestMethod.OPTIONS.toString())){
			return true;
		}

    	RequestUtil.dealBizType(request);
    	
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
        BizTypeHandler.clear();
        AppRouterHandler.clear();
    }

    private boolean validateRequest(HttpServletRequest request, HttpServletResponse response, Object handler) {

    	// deal with secret API
        if (secretAdapter.isPermit(request, response, handler)) {
            return true;
        }
        
        // deal with guest mode
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization)) {
            return true;
        }
        
        String appId = request.getHeader("sdp-appid");
        if(StringUtils.isNotBlank(appId)){
        	// new way
        	// deal with bts request
            if (btsAdapter.isPermit(request, response, handler)) {
            	// bts请求，不需要再经过bearerAdapter判断，但是需要设置租户信息
            	setTenantInNewWay(request);
                return true;
            }
            
            // deal with bearer request
            if (bearerAdapter.isPermit(request, response, handler)) {
                return true;
            }
            
            setTenantInNewWay(request);
            
            return true;
        }else{
        	// old way
        	// deal with bts request
            if (btsAdapter.isPermit(request, response, handler)) {
            	// bts请求，不需要再经过bearerAdapter判断，但是需要设置租户信息
                setTenantInOldWay(request);
                return true;
            }
            
            // deal with bearer request
            if (bearerAdapter.isPermit(request, response, handler)) {
                return true;
            }
            
            setTenantInOldWay(request);
            
            return true;
        }
    }
    
    private void setTenantInNewWay(HttpServletRequest request){
    	
    	String appId = request.getHeader("sdp-appid");
    	String orgId = UCHelper.getUserOrgId(WafContext.getCurrentUserInfo()); // 这里userInfo是替换suid后的userInfo
        String bizType = BizTypeHandler.get();
        appRouterService.appRouteAndSetTenant(appId, orgId, bizType);
        
        UserHandler.setUser(WafContext.getCurrentUserInfo().getUserId());
    }

    private void setTenantInOldWay(HttpServletRequest request) {
    	
        // 有Authentication，但没有userInfo的请求是异常请求
        // 这里要记住userInfo需要再bearerAdapter.isPermit之后再获取，
        // 因为内部有替换userInfo操作
        UserInfo userInfo = WafContext.getCurrentUserInfo(); // 这里userInfo是替换suid后的userInfo
        if (null == userInfo) {
            throw WafI18NException.of(ErrorCode.USER_NOT_LOGIN);
        }
        
        String vOrgName = request.getHeader("vorg");
        String pOrgId = request.getParameter("orgId");
        
        tenantService.setUserTenant(vOrgName, pOrgId, userInfo);
        
        UserHandler.setUser(userInfo.getUserId());
    }
}
