package cn.jcloud.jaf.common.query;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 支持offset/limit方式的Jpa分页查询
 * Created by Wei Han on 2016/2/26.
 */
public class SlicePage<T> implements Pageable {

    public static final Converter<String, String> CONVERTER
            = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

    private static LoadingCache<Pair<Class, String>, String> fieldNameCache;

    static {
        fieldNameCache = CacheBuilder.newBuilder().maximumSize(200)
                .expireAfterWrite(12, TimeUnit.HOURS)
                .build(new FieldNameCacheLoader());
    }

    private Class<T> entityClass;

    /**
     * 偏移量
     */
    private int offset = 0;

    /**
     * 每页限制
     */
    private int limit = 20;

    /**
     * 排序
     */
    private Sort sort;

    /**
     * 总数统计标志
     */
    private boolean count = false;

    public SlicePage(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public SlicePage<T> parseOrderBys(String orderBy) {
        if (!StringUtils.isBlank(orderBy)) {
            String[] sortArray = orderBy.split("\\s+and\\s+");
            List<Sort.Order> orders = new ArrayList<>(sortArray.length);
            for (String sortStr : sortArray) {
                orders.add(parseOrderBy(sortStr, entityClass));
            }
            sort = new Sort(orders);
        }
        return this;
    }

    public SlicePage<T> addSort(Sort sort) {
        if (sort == null) {
            return this;
        }
        if (this.sort == null) {
            this.sort = sort;
        } else {
            this.sort.and(sort);
        }
        return this;
    }

    public SlicePage<T> addDefaultSort(Sort sort) {
        if (this.sort == null) {
            this.sort = sort;
        }
        return this;
    }

    public SlicePage<T> forceSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    private Sort.Order parseOrderBy(String orderBy, Class entityClass) {
        String[] sortValues = orderBy.split("\\s+");
        String sortField;
        Sort.Direction direction;
        if (sortValues.length == 2) {
            sortField = sortValues[0];
            direction = Sort.Direction.fromStringOrNull(sortValues[1]);
            if (direction == null) {
                throw JafI18NException.of("不合法的排序" + sortValues[1], ErrorCode.INVALID_QUERY);
            }
        } else if (sortValues.length == 1) {
            sortField = sortValues[0];
            direction = Sort.Direction.ASC;
        } else {
            throw JafI18NException.of("排序语句格式不符合要求", ErrorCode.INVALID_QUERY);
        }
        return new Sort.Order(direction, getFieldName(entityClass, sortField));
    }

    /**
     * 通过缓存的方法提高效率
     */
    protected String getFieldName(Class entityClass, String fieldName) {
        try {
            return fieldNameCache.get(Pair.of(entityClass, fieldName));
        } catch (ExecutionException e) {
            throw JafI18NException.of(ErrorCode.INVALID_QUERY, e);
        } catch (UncheckedExecutionException e) {
            throw (RuntimeException) e.getCause();
        }
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void setOffset(int offset) {
        if (offset < 0) {
            return;
        }
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit < 1 || limit > 100) {
            return;
        }
        this.limit = limit;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    @Override
    public Pageable next() {
        SlicePage sp = new SlicePage<>(this.getEntityClass());
        sp.setCount(this.count);
        sp.setOffset(offset + limit);
        sp.setLimit(limit);
        sp.setSort(sort);
        return sp;
    }

    @Override
    public Pageable previousOrFirst() {
        if (offset < limit) {
            return first();
        }
        SlicePage sp = new SlicePage<>(this.getEntityClass());
        sp.setCount(this.count);
        sp.setOffset(offset - limit);
        sp.setLimit(limit);
        sp.setSort(sort);
        return sp;
    }

    @Override
    public Pageable first() {
        SlicePage sp = new SlicePage<>(this.getEntityClass());
        sp.setCount(this.count);
        sp.setOffset(0);
        sp.setLimit(limit);
        sp.setSort(sort);
        return sp;
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }

    public static class FieldNameCacheLoader extends CacheLoader<Pair<Class, String>, String> {
        @Override
        public String load(Pair<Class, String> key) throws Exception {
            String fieldName = key.getRight();
            if (StringUtils.isBlank(fieldName)) {
                throw JafI18NException.of(ErrorCode.FIELD_NOT_FOUND, fieldName);
            }
            fieldName = CONVERTER.convert(fieldName);
            Class clazz = key.getLeft();

            String mappingFieldName = getMappingFieldName(clazz, fieldName);
            if (mappingFieldName != null) {
                return mappingFieldName;
            }
            Field field = FieldUtils.getField(clazz, fieldName, true);
            if (field != null) {
                return fieldName;
            }
            throw JafI18NException.of(ErrorCode.FIELD_NOT_FOUND, fieldName);
        }

        public String getMappingFieldName(Class clazz, String fieldName) {
            FieldsMapping fieldsMapping = (FieldsMapping) clazz.getAnnotation(FieldsMapping.class);
            if (fieldsMapping == null || fieldsMapping.fields() == null || fieldsMapping.mappings() == null) {
                return null;
            }
            String[] fields = fieldsMapping.fields();
            int length = Math.min(fields.length, fieldsMapping.mappings().length);
            for (int i = 0; i < length; i++) {
                if (fieldName.equals(fields[i])) {
                    return fieldsMapping.mappings()[i];
                }
            }
            return null;
        }
    }
}
