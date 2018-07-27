package cn.jcloud.jaf.common.tenant.repository;

import com.nd.social.common.base.repository.BizRepository;
import com.nd.social.common.tenant.domain.Tenant;
import org.springframework.stereotype.Repository;

/**
 * Created by closer on 2016/1/27.
 */
@Repository
public interface TenantRepository extends BizRepository<Tenant, Long> {
}
