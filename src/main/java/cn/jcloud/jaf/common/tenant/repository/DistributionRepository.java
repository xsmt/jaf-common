package cn.jcloud.jaf.common.tenant.repository;

import com.nd.social.common.base.repository.BaseRepository;
import com.nd.social.common.tenant.domain.Distribution;
import org.springframework.stereotype.Repository;

/**
 * Created by Wei Han on 2016/2/4.
 */
@Repository
public interface DistributionRepository extends BaseRepository<Distribution, Integer> {
	
	Distribution findByDbConn(String dbConn);
}
