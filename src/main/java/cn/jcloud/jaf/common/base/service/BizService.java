package cn.jcloud.jaf.common.base.service;

import cn.jcloud.jaf.common.base.domain.BizDomain;
import cn.jcloud.jaf.common.base.repository.BizRepository;
import cn.jcloud.jaf.common.query.Condition;
import cn.jcloud.jaf.common.query.Items;
import cn.jcloud.jaf.common.query.ListParam;
import cn.jcloud.jaf.common.query.Operator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务Service基类
 * Created by Wei Han on 2016/1/8.
 */
public abstract class BizService<T extends BizDomain<I>, I extends Serializable> extends BaseService<T, I> {

    @Autowired
    private BizRepository<T, I> bizRepository;

    @Override
    public T findOne(I id) {
        T t = super.findOne(id);
        if (t == null || t.isDeleted()) {
            return null;
        }
        return t;
    }

    public T findOneWithDeleted(I id) {
        return super.findOne(id);
    }

    @Override
    public List<T> findAll() {
        return bizRepository.findByDeletedIsFalse();
    }

    @Override
    public Items<T> list(ListParam<T> listParam) {
        listParam.addCondition(Condition.of("deleted", Operator.NE, true, Boolean.class));
        return super.list(listParam);
    }

    @Override
    public T add(T t) {
        //这里明确赋值，是为了防止外部赋值
        //当id为外部指定是@CreateDate无法自动设置
        t.setCreateTime(new Date());
        t.setUpdateTime(null);
        t.setDeleted(false);
        return super.add(t);
    }

    @Override
    public T update(I id, Map<String, Object> map) {
        map.remove("create_time");
        map.remove("createTime");
        map.remove("update_time");
        map.remove("updateTime");
        map.remove("deleted");
        return super.update(id, map);
    }

    @Override
    protected void delete(T t) {
        if (t == null || t.isDeleted()) {
            throw module().notFound();
        }
        checkId(t.getId());

        beforeDelete(t);
        t.setDeleted(true);
        bizRepository.save(t);
    }
}
