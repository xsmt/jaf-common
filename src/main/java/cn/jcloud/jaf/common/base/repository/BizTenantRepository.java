package cn.jcloud.jaf.common.base.repository;

import cn.jcloud.jaf.common.base.domain.BizTenantDomain;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wei Han on 2016/1/27.
 */
@NoRepositoryBean
public interface BizTenantRepository<T extends BizTenantDomain<I>, I extends Serializable> extends BaseTenantRepository<T, I> {

    <T extends BizTenantDomain<I>> List<T> findByTenantAndDeletedIsFalse(long tenant);
}
