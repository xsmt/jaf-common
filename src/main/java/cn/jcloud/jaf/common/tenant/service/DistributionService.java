package cn.jcloud.jaf.common.tenant.service;

import cn.jcloud.jaf.common.base.domain.Module;
import cn.jcloud.jaf.common.base.service.BaseService;
import cn.jcloud.jaf.common.constant.CommonModules;
import cn.jcloud.jaf.common.tenant.domain.Distribution;
import cn.jcloud.jaf.common.tenant.domain.Tenant;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * 租房分库分表Service
 * Created by Wei Han on 2016/2/4.
 */
@Service
public class DistributionService extends BaseService<Distribution, Integer> {

    //7
    private static final int TENANCY_MASK = ~(-1 << 3);

    @EventListener(ContextRefreshedEvent.class)
    public void initOne() {
        if (!exists(1)) {
            Distribution distribution = new Distribution();
            distribution.setId(1);
            distribution.setDbConn("default");
            distribution.setTenancyAmount(TENANCY_MASK + 1);
            distribution.setTenancy(0);
            add(distribution);
        }
    }

    @Override
    protected Module module() {
        return CommonModules.DISTRIBUTION;
    }

    /**
     * 给租户设置分库分表信息
     *
     * @param tenant 租户
     */
    public void wrapDistributionInfo(Tenant tenant) {
        if (tenant.getDbConn() == null) {
            Distribution distribution = findOne(1);
            tenant.setDbConn(distribution.getDbConn());
            int tenancy = distribution.getTenancy();
            tenant.setTenancy((long) tenancy);
            //计算新的表序号,(TENANCY_MASK+1)个表进行循环
            tenancy = (tenancy + 1) & TENANCY_MASK;
            distribution.setTenancy(tenancy);
            update(distribution);
        }
    }
}
