package cn.jcloud.jaf.common.tenant.service;

import cn.jcloud.jaf.common.tenant.domain.Tenant;

/**
 * Created by Wei Han on 2016-09-05.
 */
public interface TenantAutoCreatable {

    Tenant getByOrgId(String orgId);
}
