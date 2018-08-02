package cn.jcloud.jaf.common.whitelist.repository;

import cn.jcloud.jaf.common.base.repository.BaseTenantRepository;
import cn.jcloud.jaf.common.whitelist.domain.GuestWhiteList;
import org.springframework.stereotype.Repository;

/**
 * 白名单 Dao
 * Created by Wei Han on 2016/3/4.
 */
@Repository
public interface GuestWhiteListRepository extends BaseTenantRepository<GuestWhiteList, Long> {

}
