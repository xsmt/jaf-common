package cn.jcloud.jaf.common.tenant.repository;

import cn.jcloud.jaf.common.base.repository.BaseRepository;
import cn.jcloud.jaf.common.tenant.domain.Distribution;
import org.springframework.stereotype.Repository;

/**
 * Created by Wei Han on 2016/2/4.
 */
@Repository
public interface DistributionRepository extends BaseRepository<Distribution, Integer> {
}
