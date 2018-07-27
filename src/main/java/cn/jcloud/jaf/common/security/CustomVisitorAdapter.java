package cn.jcloud.jaf.common.security;

import com.nd.gaea.WafException;
import com.nd.gaea.rest.security.authens.Organization;
import com.nd.gaea.rest.security.services.visitor.OrganizationService;
import com.nd.gaea.rest.security.services.visitor.VisitorsAdapter;
import com.nd.social.common.approuter.domain.AppRouter;
import com.nd.social.common.approuter.service.AppRouterService;
import com.nd.social.common.handler.BizTypeHandler;
import com.nd.social.common.handler.VOrgHandler;
import com.nd.social.common.tenant.service.TenantService;
import com.nd.social.common.util.ReflectUtil;
import com.nd.social.common.util.RequestUtil;
import com.nd.social.common.whitelist.service.VisitorService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author hasayaki(125473)
 *
 */
@Service
public class CustomVisitorAdapter implements VisitorsAdapter{
	
	@Autowired
	private AppRouterService appRouterService;
	@Autowired
	private TenantService tenantService;
	@Autowired
	private VisitorService visitorService;
	@Autowired
	private OrganizationService organizationService;
	
	@Override
	public boolean isPermit(HttpServletRequest request, HttpServletResponse response, Object handler) {

		// 跨域请求
		if(request.getMethod().equals(RequestMethod.OPTIONS.toString())){
			return true;
		}
		
		RequestUtil.dealBizType(request);
		
		if (handler instanceof HandlerMethod) {
			// 获取当前请求的method
			String requestMethod = ReflectUtil.getMethodSignature((HandlerMethod) handler);
            
            // 这个判断不可去掉，虽然拦截器已对secret API进行处理
            if (SecretHandler.isSecretMethod(requestMethod)) {
                return true;
            }
            
            // 基础白名单接口
            if (GuestHandler.isCommonGuestMethod(requestMethod)){
            	return true;
            }
            
            boolean forbidden = true;
    		String authorization = request.getHeader("Authorization");
    		if(StringUtils.isBlank(authorization)) {
    			// 访客模式
    			String appId = request.getHeader("sdp-appid");
    	        if(StringUtils.isNotBlank(appId)){
    	        	// 新方式，必须带Orgname头部
    	        	String orgName = request.getHeader("Orgname");
    	        	if(orgName != null){
    	        		String orgId = "0";
    	        		if(StringUtils.isNotBlank(orgName)){
    	        			try {
        	        			Organization organization = organizationService.query(orgName);
        	        			orgId = organization.getOrgId();
        					} catch(Exception ex) {}
    	        		}    	        		
    	        		String bizType = BizTypeHandler.get();
        	        	AppRouter appRouter = appRouterService.appRouteAndSetTenant(appId, orgId, bizType);
        	        	
        	        	// 非基础白名单后台不可配置接口
        	            if (GuestHandler.isUncustomizableWhiteMethod(requestMethod)) {
        	                return true;
        	            }
        	        	
        	        	forbidden = !this.isCustomizableWhiteApi(requestMethod, appRouter.getServiceTenantId());
    	        	}
    	        }else{
    	        	// 旧方式
    	        	boolean chkVorgGuest = true;
    				
    				// validate orgName
    				String orgName = request.getHeader("Orgname");
    				if(StringUtils.isNotBlank(orgName)) {
    					Organization organization = null;
    					try {
    						organization = organizationService.query(orgName);
    					} catch(Exception ex) {}
    					if(organization != null && tenantService.isValidOrg(organization.getOrgId())) {
    						// 非基础白名单后台不可配置接口
            	            if (GuestHandler.isUncustomizableWhiteMethod(requestMethod)) {
            	                return true;
            	            }
    			            
    						chkVorgGuest = false;
    						forbidden = !this.isCustomizableWhiteApi(requestMethod, organization.getOrgId());
    					}
    				}
    				
    				// validate vorg
    				String vorg = request.getHeader("vorg");
    				if(chkVorgGuest && StringUtils.isNotBlank(vorg)) {
    					Organization organization = null;
    					try {
    						organization = organizationService.query(vorg);
    					} catch(Exception ex) {}
    					if(organization != null && tenantService.isValidOrg(organization.getOrgId())) {
    						// 非基础白名单后台不可配置接口
            	            if (GuestHandler.isUncustomizableWhiteMethod(requestMethod)) {
            	                return true;
            	            }
    			            
    						VOrgHandler.setVOrgName(vorg);
    	                    VOrgHandler.setVOrgId(organization.getOrgId());
    						forbidden = !this.isCustomizableWhiteApi(requestMethod, organization.getOrgId());
    					}
    				}
    	        }
    		} else {
    			// 非访客模式
    			forbidden = false;
    		}
    		
    		if (forbidden) {
    			VOrgHandler.clear(); // 防止内存泄漏
    			throw new WafException("WAF/GUEST_ACCESS_DENIED", "游客访问受限", HttpStatus.FORBIDDEN);
    		}
    		
    		return true;
		}
		
		return true;
	}
	
	private boolean isCustomizableWhiteApi(String requestMethod, String tenantId){
		
		List<String> guestApiList = visitorService.getCustomizableWhiteApiList(tenantId);
		if (CollectionUtils.isNotEmpty(guestApiList)) {
			for(String guestApi : guestApiList){
				if(StringUtils.equals(requestMethod, guestApi)){
					return true;
				}
			}
		}
		return false;
	}
}
