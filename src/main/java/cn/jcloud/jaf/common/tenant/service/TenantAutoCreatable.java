package cn.jcloud.jaf.common.tenant.service;

import com.nd.social.common.tenant.domain.Tenant;

/**
 * Created by Wei Han on 2016-09-05.
 */
public interface TenantAutoCreatable {

    Tenant getByOrgId(String orgId);
}
