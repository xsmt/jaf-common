package cn.jcloud.jaf.common.tenant.service;

import cn.jcloud.jaf.common.tenant.domain.Tenant;

/**
 * Created by Wei Han on 2016-09-05.
 */
public class DefaultTenantAutoCreateImpl implements TenantAutoCreatable {

    @Override
    public Tenant getByOrgId(String orgId) {
        Tenant tenant = new Tenant();
        tenant.setOrgId(orgId);
        tenant.setName(orgId);
        tenant.setAppName(orgId);
        tenant.setRealm(orgId + ".sdp.nd");
        return tenant;
    }
}
