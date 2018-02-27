package cn.jcloud.jaf.common.base.repository;


import cn.jcloud.jaf.common.base.domain.BizDomain;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wei Han on 2016/1/8.
 */
@NoRepositoryBean
public interface BizRepository<T extends BizDomain<I>, I extends Serializable> extends BaseRepository<T, I> {

    <T extends BizDomain<I>> List<T> findByDeletedIsFalse();

}
