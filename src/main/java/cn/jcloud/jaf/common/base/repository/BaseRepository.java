package cn.jcloud.jaf.common.base.repository;

import cn.jcloud.jaf.common.base.domain.BaseDomain;
import cn.jcloud.jaf.common.query.Items;
import cn.jcloud.jaf.common.query.ListParam;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by closer on 2016/1/5.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseDomain<I>, I extends Serializable> extends PagingAndSortingRepository<T, I> {

    Items<T> list(ListParam<T> listParam);

    @Override
    List<T> findAll();
}
