package cn.jcloud.jaf.common.template.web;

import com.nd.social.common.approuter.domain.AppRouter;
import com.nd.social.common.approuter.service.AppRouterService;
import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import com.nd.social.common.handler.TenantHandler;
import com.nd.social.common.security.BearerApi;
import com.nd.social.common.security.SuidRequired;
import com.nd.social.common.template.event.ConfigTemplateSaveEvent;
import com.nd.social.common.template.vo.ConfigTemplateVo;
import com.nd.social.common.tenant.domain.Tenant;
import com.nd.social.common.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 模板控制器
 * 
 * @author hasayaki(125473)
 *
 */
@RestController
@BearerApi(group = "config_templates", suidRequired = SuidRequired.IGNORE)
@RequestMapping({"/${version}/config_templates", "/${version}/{bizType}/config_templates"})
public class ConfigTemplateController {

	@Autowired
    private ApplicationEventPublisher publisher;
	@Autowired
    private AppRouterService appRouterService;
    @Autowired
    private TenantService tenantService;

    /**
     * 保存配置模板 [POST] /config_templates
     * 
     * 本接口只负责发布一个保存配置模板事件（ConfigTemplateSaveEvent），并对事件处理异常进行捕获处理；
     * 各组件需监听该事件，并同步进行保存配置模板操作
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTemplate(@RequestBody @Valid ConfigTemplateVo template) {
    	
    	AppRouter appRouter = appRouterService.findStrictOne(template.getAppRouterId());
    	Tenant tenant = tenantService.findStrictOne(appRouter.getServiceTenantId());
    	tenant.setTemplateId(template.getTemplateId());
    	TenantHandler.setTenant(tenant);
    	
    	try {
			publisher.publishEvent(new ConfigTemplateSaveEvent(tenant));
		} catch (Exception e) {
			throw WafI18NException.of(ErrorCode.SAVE_CONFIG_TEMPLATE_NG);
		}
    }
}
