package cn.jcloud.jaf.common.tenant.service;

import cn.jcloud.gaea.rest.security.authens.Organization;
import cn.jcloud.gaea.rest.security.authens.UserInfo;
import cn.jcloud.gaea.rest.security.services.RealmService;
import cn.jcloud.gaea.rest.security.services.WafUserDetailsService;
import cn.jcloud.gaea.rest.security.services.visitor.OrganizationService;
import cn.jcloud.jaf.common.base.domain.Module;
import cn.jcloud.jaf.common.base.service.BizService;
import cn.jcloud.jaf.common.constant.CommonModules;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.handler.TenantHandler;
import cn.jcloud.jaf.common.handler.VOrgHandler;
import cn.jcloud.jaf.common.tenant.domain.Tenant;
import cn.jcloud.jaf.common.tenant.event.TenantCreateEvent;
import cn.jcloud.jaf.common.tenant.repository.TenantRepository;
import cn.jcloud.jaf.common.virtualorg.service.VirtualOrgService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

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

    public void setGuestTenant(String vOrgName, String orgName) {
        String activeOrgName = StringUtils.defaultIfEmpty(vOrgName, orgName);
        if (StringUtils.isEmpty(activeOrgName)) {
            return;
        }
        Organization organization = organizationService.query(activeOrgName);
        setTenantByOrgId(organization.getOrgId());
        if (!StringUtils.isEmpty(vOrgName)) {
            VOrgHandler.setVOrgId(organization.getOrgId());
            VOrgHandler.setVOrgName(vOrgName);
        }
    }

    public void setUserTenant(String vOrgName, String orgId, UserInfo userInfo) {
        String activeOrgId;
        //如果不包含有vOrgName(头部vorg)则直接使用用户信息中的组织id
        if (StringUtils.isEmpty(vOrgName)) {
            activeOrgId = getUserOrgId(userInfo);
        } else {
            Organization organization = organizationService.query(vOrgName);
            activeOrgId = organization.getOrgId();
            //TODO 完善虚拟租户严重
//            wafUserDetailsService.getVorgUserInfo(userInfo.getUserId(), realmService.getRealm(null), activeOrgId);
            VOrgHandler.setVOrgId(activeOrgId);
            VOrgHandler.setVOrgName(vOrgName);
            //TODO 完善虚拟租户严重
            //如果指定的虚拟组织没开通服务
//            if (!exists(activeOrgId) && null == tenantAutoCreatable) {
//                //如果没有指定orgId，则通过用户上下文获取
//                if (StringUtils.isEmpty(orgId)) {
//                    activeOrgId = getUserOrgId(userInfo);
//                } else {
//                    //校验指定的orgId（Parameter中的orgId）是否在虚拟组织内
//                    virtualOrgService.getVoNodeInfo(activeOrgId, orgId);
//                    activeOrgId = orgId;
//                }
//            }
        }
        setTenantByOrgId(activeOrgId);
    }

    private void setTenantByOrgId(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return;
        }
        Tenant tenant = findOne(orgId);
        if (tenant == null) {
            if (tenantAutoCreatable == null) {
                throw module().notFound();
            } else {
                tenant = add(tenantAutoCreatable.getByOrgId(orgId));
            }
        }
        if (tenant.isPauseFlag()) {
            throw JafI18NException.of(tenant.getMsg(), ErrorCode.OUT_OF_SERVICE);
        }
        updateTenantAccessTime(tenant);
        TenantHandler.setTenant(tenant);
    }

    private String getUserOrgId(UserInfo userInfo) {
        Map<String, Object> orgExInfo = userInfo.getOrgExinfo();
        if (orgExInfo != null && orgExInfo.containsKey("org_id")) {
            return orgExInfo.get("org_id").toString();
        } else {
            throw JafI18NException.of(ErrorCode.MISSING_ORG_ID);
        }
    }
}
