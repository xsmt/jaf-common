package cn.jcloud.jaf.common.tenant.service;

import com.nd.social.common.approuter.domain.AppRouter;
import com.nd.social.common.base.domain.Module;
import com.nd.social.common.base.service.BaseService;
import com.nd.social.common.constant.CommonModules;
import com.nd.social.common.tenant.domain.Distribution;
import com.nd.social.common.tenant.domain.Tenant;
import com.nd.social.common.tenant.repository.DistributionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * 租房分库分表Service
 * Created by Wei Han on 2016/2/4.
 */
@Service
public class DistributionService extends BaseService<Distribution, Integer> {

	@Autowired
    private DistributionRepository distributionRepository;
	
    private static final int TENANCY_MASK = ~(-1 << 3); // 7
    public static final String DEFAULT_DB_CONN = "default";

    @EventListener(ContextRefreshedEvent.class)
    public void initOne() {
        if (!exists(1)) {
            Distribution distribution = new Distribution();
            distribution.setId(1);
            distribution.setDbConn(DEFAULT_DB_CONN);
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
    
    /**
     * 开通应用分库分表处理
     */
    public void dealWithDistribution(AppRouter appRouter, Tenant tenant){
    	
    	// deal with db_conn
        if(StringUtils.isBlank(appRouter.getDbConn())){
        	tenant.setDbConn(DEFAULT_DB_CONN);
        	dealWithTenancy(appRouter, tenant);
        }else{
        	int maxDistributionId = 0;
    		Distribution distribution = null;
    		Iterable<Distribution> distributions = distributionRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
    		for(Distribution item : distributions){
    			if(StringUtils.equals(appRouter.getDbConn(), item.getDbConn())){
    				distribution = item;
    				break;
    			}
    			maxDistributionId = item.getId();
    		}
    		
    		if(distribution == null){
    			distribution = new Distribution();
                distribution.setId(maxDistributionId + 1);
                distribution.setDbConn(appRouter.getDbConn());
                distribution.setTenancyAmount(TENANCY_MASK + 1);
                distribution.setTenancy(0);
                add(distribution);
    		}
    		
        	tenant.setDbConn(appRouter.getDbConn());
        	dealWithTenancy(appRouter, tenant);
        }
    }
    
    private void dealWithTenancy(AppRouter appRouter, Tenant tenant){
    	
    	if(appRouter.getTenancy() == null){
    		Distribution distribution = distributionRepository.findByDbConn(tenant.getDbConn());
    		int tenancy = distribution.getTenancy();
            tenant.setTenancy((long) tenancy);
            // 计算新的表序号，(TENANCY_MASK+1)个表进行循环
            tenancy = (tenancy + 1) & TENANCY_MASK;
            distribution.setTenancy(tenancy);
            update(distribution);
        }else{
        	tenant.setTenancy(appRouter.getTenancy());
        }
    }
}
