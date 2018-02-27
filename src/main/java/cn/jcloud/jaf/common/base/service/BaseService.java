package cn.jcloud.jaf.common.base.service;

import cn.jcloud.jaf.common.util.JafJsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.jcloud.jaf.common.base.domain.BaseDomain;
import cn.jcloud.jaf.common.base.domain.Module;
import cn.jcloud.jaf.common.base.repository.BaseRepository;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.query.Items;
import cn.jcloud.jaf.common.query.ListParam;
import cn.jcloud.jaf.common.util.ValidatorUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BaseService
 * Created by closer on 2016/1/5.
 */
@Transactional
public abstract class BaseService<T extends BaseDomain<I>, I extends Serializable> {

    private ObjectMapper objectMapper = JafJsonMapper.getMapper();

    @Autowired
    private BaseRepository<T, I> baseRepository;

    protected abstract Module module();

    public T findOne(I id) {
        checkId(id);
        return baseRepository.findOne(id);
    }

    /**
     * 严格的详情获取，当数据不存在是，会报出 ErrorCode.DATA_NOT_FOUND
     *
     * @param id 主键
     * @return 详情
     */
    public T findStrictOne(I id) {
        T t = findOne(id);
        if (t == null) {
            throw module().notFound();
        }
        return t;
    }

    public List<T> findAll() {
        return baseRepository.findAll();
    }

    public Items<T> list(ListParam<T> listParam) {
        return baseRepository.list(listParam);
    }

    public T add(T t) {
        if (null != t.getId() && exists(t.getId())) {
            throw module().existed();
        }
        ValidatorUtil.validateAndThrow(t);
        beforeAdd(t);
        return baseRepository.save(t);
    }

    protected void beforeAdd(T t) {

    }

    /**
     * 更新操作<br/>
     * 应该尽力保护该方法，外部如果需要修改局部字段，应该在Service方法中增加相应的方法，<br/>
     * 底层调用update方法,而不是直接暴露update方法
     *
     * @param t 需要更新的实体实例
     * @return 更新后的实体实例
     */
    protected T update(T t) {
        checkId(t.getId());
        if (!exists(t.getId())) {
            throw module().notFound();
        }
        return baseRepository.save(t);
    }

    public T update(I id, Map<String, Object> map) {
        T oldDomain = findStrictOne(id);
        //body中的id会覆盖掉url中的id，将会引起将修改后的url.id指向的实体覆盖到body.id指向的实体
        map.remove("id");
        T newDomain = createDomainFromOldAndMap(oldDomain, map);
        ValidatorUtil.validateAndThrow(newDomain);
        beforeUpdate(oldDomain, newDomain);
        return update(newDomain);
    }

    protected void beforeUpdate(T oldDomain, T newDomain) {

    }

    private T createDomainFromOldAndMap(T oldDomain, Map<String, Object> map) {
        try {
            T newDomain = (T) oldDomain.getClass().newInstance();
            BeanUtils.copyProperties(oldDomain, newDomain);
            String json = objectMapper.writeValueAsString(map);
            return objectMapper.readerForUpdating(newDomain).readValue(json);
        } catch (IllegalArgumentException | IOException e) {
            throw JafI18NException.of(ErrorCode.INVALID_ARGUMENT);
        } catch (InstantiationException | IllegalAccessException e) {
            String message = "类[" + oldDomain.getClass().getCanonicalName()
                    + "]找不到可访问无参构造器";
            throw JafI18NException.of(message, ErrorCode.FAIL);
        }
    }

    protected void delete(T t) {
        if (t == null) {
            throw module().notFound();
        }
        beforeDelete(t);
        baseRepository.delete(t);
    }

    protected void beforeDelete(T t) {
    }

    public void delete(I id) {
        T t = findStrictOne(id);
        delete(t);
    }

    public boolean exists(I id) {
        return findOne(id) != null;
    }

    protected void checkId(I id) {
        if (id == null) {
            throw module().nullId();
        }
    }

}
