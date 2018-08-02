package cn.jcloud.jaf.common.tenant.event;


import cn.jcloud.jaf.common.tenant.domain.Tenant;

/**
 * 公司新增事件
 * Created by Wei Han on 2016/1/5.
 */
public class TenantSetEvent extends TenantEvent {

    public TenantSetEvent(Tenant tenant) {
        super(tenant);
    }
}
