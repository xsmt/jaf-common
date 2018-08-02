package cn.jcloud.jaf.common.tenant.event;


import cn.jcloud.jaf.common.tenant.domain.Tenant;

/**
 * 公司新增事件
 * Created by Wei Han on 2016/1/5.
 */
public class TenantClearEvent extends TenantEvent {

    public TenantClearEvent(Tenant tenant) {
        super(tenant);
    }
}
