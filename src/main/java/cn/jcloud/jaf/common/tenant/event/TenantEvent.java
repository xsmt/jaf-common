package cn.jcloud.jaf.common.tenant.event;

import com.nd.social.common.tenant.domain.Tenant;

/**
 * Created by Wei Han on 2016/6/27.
 */
public class TenantEvent {

    private Tenant tenant;

    public TenantEvent(Tenant tenant) {
        this.tenant = tenant;
    }

    public Tenant getTenant() {
        return this.tenant;
    }
}
