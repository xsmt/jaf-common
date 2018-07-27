package cn.jcloud.jaf.common.tenant.service;

import com.nd.gaea.rest.security.authens.Organization;
import com.nd.gaea.rest.security.authens.UserInfo;
import com.nd.gaea.rest.security.services.RealmService;
import com.nd.gaea.rest.security.services.WafUserDetailsService;
import com.nd.gaea.rest.security.services.visitor.OrganizationService;
import com.nd.social.common.base.domain.Module;
import com.nd.social.common.base.service.BizService;
import com.nd.social.common.constant.CommonModules;
import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import com.nd.social.common.handler.TenantHandler;
import com.nd.social.common.handler.VOrgHandler;
import com.nd.social.common.tenant.domain.Tenant;
import com.nd.social.common.tenant.event.TenantCreateEvent;
import com.nd.social.common.tenant.repository.TenantRepository;
import com.nd.social.common.util.UCHelper;
import com.nd.social.common.virtualorg.service.VirtualOrgService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 租户Service
 * Created by Wei Han on 2016/1/27.
 */
@Service
public class TenantService extends BizService<Tenant, Long> {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private VirtualOrgService virtualOrgService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private WafUserDetailsService wafUserDetailsService;

    @Autowired
    private RealmService realmService;

    @Autowired(required = false)
    private TenantAutoCreatable tenantAutoCreatable;

    @EventListener(ContextRefreshedEvent.class)
    public void initTenantHandler() {
        TenantHandler.init();
    }

    @Override
    protected Module module() {
        return CommonModules.TENANT;
    }

    @Override
    public Tenant add(Tenant tenant) {
        tenant.setId(NumberUtils.toLong(tenant.getOrgId()));
        //如果之前只是逻辑删除，则更改下逻辑删除标志
        Tenant oldTenant = findOneWithDeleted(tenant.getId());
        if (oldTenant != null && !oldTenant.isDeleted()) {
            throw CommonModules.TENANT.existed();
        }
        if (oldTenant != null) {
            tenant.setDbConn(oldTenant.getDbConn());
            tenant.setTenancy(oldTenant.getTenancy());
            tenant = tenantRepository.save(tenant);
        } else {
            tenant.setCreateTime(new Date());
            //通过配置表来设置属于哪个分区
            distributionService.wrapDistributionInfo(tenant);
            tenant = super.add(tenant);
        }

        publisher.publishEvent(new TenantCreateEvent(tenant));
        return tenant;
    }
    
    public Tenant addAssociatedWithAppRouterAdd(Tenant tenant){
    	
    	String templateId = tenant.getTemplateId();
    	tenant = tenantRepository.save(tenant);
    	tenant.setTemplateId(templateId);
    	
    	try {
			publisher.publishEvent(new TenantCreateEvent(tenant));
		} catch (Exception e) {
			tenantRepository.delete(tenant);
			throw WafI18NException.of(ErrorCode.CREATE_TENANT_ASSOCIATE_OP_NG);
		}
    	return tenant;
    }

    public Tenant pause(Long id, boolean pauseFlag, String msg) {
        Tenant tenant = findStrictOne(id);
        tenant.setPauseFlag(pauseFlag);
        tenant.setMsg(msg);
        return update(tenant);
    }

    public Tenant findOne(String orgId) {
        return findOne(NumberUtils.toLong(orgId));
    }

    public Tenant findStrictOne(String orgId) {
        return findStrictOne(NumberUtils.toLong(orgId));
    }

    public boolean exists(String orgId) {
        return exists(NumberUtils.toLong(orgId));
    }

    public Tenant updateGuestMode(long orgId, boolean guestMode) {
        Tenant tenant = findStrictOne(orgId);
        tenant.setGuestMode(guestMode);
        return update(tenant);
    }

    public boolean isOpenGuestMode(long orgId) {
        Tenant tenant = findStrictOne(orgId);
        return tenant.isGuestMode();
    }

    private void updateTenantAccessTime(Tenant tenant) {
        Date now = new Date();
        Date lastAccessTime = tenant.getLastAccessTime();
        if (lastAccessTime != null && DateUtils.isSameDay(now, lastAccessTime)) {
            return;
        }
        tenant.setLastAccessTime(now);
        update(tenant);
    }

    public void setUserTenant(String vOrgName, String orgId, UserInfo userInfo) {
        String activeOrgId;
        //如果不包含有vOrgName(头部vorg)则直接使用用户信息中的组织id
        if (StringUtils.isEmpty(vOrgName)) {
            activeOrgId = UCHelper.getUserOrgId(userInfo);
        } else {
            Organization organization = organizationService.query(vOrgName);
            activeOrgId = organization.getOrgId();
            UserInfo vorgUserInfo = wafUserDetailsService.getVorgUserInfo(userInfo.getUserId(),
                    realmService.getRealm(null), activeOrgId);
            try {
                vorgUserInfo.getOrgExinfo();
                VOrgHandler.setVOrgId(activeOrgId);
                VOrgHandler.setVOrgName(vOrgName);
                // 如果指定的虚拟组织没开通服务
                if (!exists(activeOrgId) && null == tenantAutoCreatable) {
                    // 如果没有指定orgId，则通过用户上下文获取
                    if (StringUtils.isEmpty(orgId)) {
                        activeOrgId = UCHelper.getUserOrgId(vorgUserInfo);
                    } else {
                        // 校验指定的orgId（Parameter中的orgId）是否在虚拟组织内
                        virtualOrgService.getVoNodeInfo(activeOrgId, orgId);
                        activeOrgId = orgId;
                    }
                }
            } catch (Exception e) {
            	VOrgHandler.clear();
                activeOrgId = UCHelper.getUserOrgId(userInfo); // 实体组织访问
            }
        }
        setTenantByTenantId(activeOrgId);
    }

    public void setTenantByTenantId(String tenantId) {
    	
        Tenant tenant = findOne(tenantId);
        if (tenant == null) {
            if (tenantAutoCreatable == null) {
                throw module().notFound();
            } else {
                tenant = add(tenantAutoCreatable.getByOrgId(tenantId));
            }
        }else if (tenant.isPauseFlag()) {
            throw WafI18NException.of(tenant.getMsg(), ErrorCode.OUT_OF_SERVICE);
        }else{
        	updateTenantAccessTime(tenant);
        	TenantHandler.setTenant(tenant);
        }
    }
    
    public boolean isValidOrg(String orgId){
    	
    	Tenant tenant = findOne(orgId);
        if (tenant == null) {
            return false;
        }else if (tenant.isPauseFlag()) {
            throw WafI18NException.of(tenant.getMsg(), ErrorCode.OUT_OF_SERVICE);
        }else{
        	updateTenantAccessTime(tenant);
            TenantHandler.setTenant(tenant);
            return true;
        }
    }
}
