package cn.jcloud.jaf.common.base.service;

import cn.jcloud.jaf.common.base.domain.BaseTenantDomain;
import cn.jcloud.jaf.common.base.repository.BaseTenantRepository;
import cn.jcloud.jaf.common.handler.TenantHandler;
import cn.jcloud.jaf.common.query.Condition;
import cn.jcloud.jaf.common.query.Items;
import cn.jcloud.jaf.common.query.ListParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * Created by closer on 2016/1/27.
 */
public abstract class BaseTenantService<T extends BaseTenantDomain<I>, I extends Serializable>
        extends BaseService<T, I> {

    @Autowired
    private BaseTenantRepository<T, I> baseTenantRepository;

    @Override
    public T findOne(I id) {
        T t = super.findOne(id);
        //这个还是必须要有，才能保证数据安全
        if (t == null || TenantHandler.getStrictTenant() != t.getTenant()) {
            return null;
        }
        return t;
    }

    @Override
    public List<T> findAll() {
        return baseTenantRepository.findByTenant(TenantHandler.getStrictTenant());
    }

    @Override
    public Items<T> list(ListParam<T> listParam) {
        listParam.addCondition(Condition.eq("tenant", TenantHandler.getStrictTenant(), Long.class));
        return super.list(listParam);
    }

    @Override
    public T add(T t) {
        t.setTenant(TenantHandler.getStrictTenant());
        return super.add(t);
    }

    @Override
    protected T update(T t) {
        t.setTenant(TenantHandler.getStrictTenant());
        return super.update(t);
    }
}
