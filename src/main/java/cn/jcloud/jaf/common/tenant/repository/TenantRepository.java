package cn.jcloud.jaf.common.tenant.repository;

import cn.jcloud.jaf.common.base.repository.BizRepository;
import cn.jcloud.jaf.common.tenant.domain.Tenant;
import org.springframework.stereotype.Repository;

/**
 * Created by Wei Han on 2016/1/27.
 */
@Repository
public interface TenantRepository extends BizRepository<Tenant, Long> {
}
