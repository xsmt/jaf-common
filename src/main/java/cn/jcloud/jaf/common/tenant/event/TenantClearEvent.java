package cn.jcloud.jaf.common.tenant.event;


import com.nd.social.common.tenant.domain.Tenant;

/**
 * 公司新增事件
 * Created by closer on 2016/1/5.
 */
public class TenantClearEvent extends TenantEvent {

    public TenantClearEvent(Tenant tenant) {
        super(tenant);
    }
}
