package cn.jcloud.jaf.common.query;

import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.util.JafJsonMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import cn.jcloud.jaf.common.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * List规范查询条件
 * Created by Wei Han on 2016/1/15.
 */
public class ListParam<T> extends SlicePage<T> {

    private static LoadingCache<Pair<Class, String>, Class> fieldTypeCache;

    static {
        fieldTypeCache = CacheBuilder.newBuilder().maximumSize(200)
                .expireAfterWrite(12, TimeUnit.HOURS)
                .build(new FieldTypeCacheLoader());
    }

    /**
     * 过滤条件
     */
    private List<Condition> conditions = Collections.emptyList();

    public ListParam(Class<T> entityClass) {
        super(entityClass);
    }


    public ListParam<T> parseConditions(String filter) {
        if (!StringUtils.isBlank(filter)) {
            String[] conditionArray = filter.split("\\s+and\\s+");
            conditions = new LinkedList<>();
            for (String conditionStr : conditionArray) {
                conditions.add(parseCondition(conditionStr, getEntityClass()));
            }
        }
        return this;
    }

    public ListParam<T> addCondition(Condition condition) {
        if (condition == null) {
            return this;
        }
        if (this.conditions.isEmpty()) {
            this.conditions = new LinkedList<>();
        }
        this.conditions.add(condition);
        return this;
    }

    /**
     * 替换掉相同Field及Operator的条件，如果原不存在，不抛错
     */
    public ListParam<T> replaceCondition(Condition condition) {
        if (condition == null) {
            return this;
        }
        if (this.conditions.isEmpty()) {
            this.conditions = new LinkedList<>();
            this.conditions.add(condition);
        } else {
            removeCondition(condition.getField(), condition.getOperator());
            this.conditions.add(condition);
        }
        return this;
    }

    public ListParam removeCondition(Condition condition) {
        if (!conditions.isEmpty()) {
            conditions.remove(condition);
        }
        return this;
    }

    /**
     * 删除指定Field及Operator的条件
     */
    public ListParam removeCondition(String field, Operator operator) {
        if (conditions.isEmpty()) {
            return this;
        }
        Iterator<Condition> iterator = conditions.iterator();
        while (iterator.hasNext()) {
            Condition condition = iterator.next();
            if (condition.getField().equals(field)
                    && condition.getOperator().equals(operator)) {
                iterator.remove();
            }
        }
        return this;
    }

    /**
     * 删除指定Field的条件
     */
    public ListParam removeCondition(String field) {
        if (conditions.isEmpty()) {
            return this;
        }
        Iterator<Condition> iterator = conditions.iterator();
        while (iterator.hasNext()) {
            Condition condition = iterator.next();
            if (condition.getField().equals(field)) {
                iterator.remove();
            }
        }
        return this;
    }

    public boolean containCondition(Condition condition) {
        return conditions.contains(condition);
    }

    public int conditionSize() {
        return conditions.size();
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    private Condition parseCondition(String conditionStr, Class entityClass) {
        String[] filterValues = StringUtils.split(conditionStr, " ", 3);
        if (filterValues.length != 3) {
            throw JafI18NException.of(ErrorCode.INVALID_QUERY);
        }
        String fieldName = getFieldName(entityClass, filterValues[0]);
        if (!Operator.isValid(filterValues[1].toUpperCase())) {
            throw JafI18NException.of(ErrorCode.INVALID_QUERY);
        }

        Operator operator = Operator.valueOf(filterValues[1].toUpperCase());
        Class fieldType = getFieldType(entityClass, fieldName);
        Object valueObj = getValue(operator, filterValues[2], fieldType);
        return Condition.of(fieldName, operator, valueObj, fieldType);
    }

    /**
     * 通过缓存的方法提高效率
     */
    protected Class getFieldType(Class entityClass, String fieldName) {
        try {
            return fieldTypeCache.get(Pair.of(entityClass, fieldName));
        } catch (ExecutionException e) {
            throw JafI18NException.of(ErrorCode.INVALID_QUERY, e);
        }
    }

    private Object getValue(Operator operator, String filterValue, Class fieldType) {
        if (Operator.LIKE == operator) {
            return StringUtils.strip(filterValue, "'");
        }

        if (!Number.class.isAssignableFrom(fieldType)) {
            filterValue = StringUtils.strip(filterValue, "'");
            filterValue = '"' + filterValue + '"';
        }
        try {
            return JafJsonMapper.getMapper().readValue(filterValue, fieldType);
        } catch (IOException e) {
            throw JafI18NException.of(e.getMessage(), ErrorCode.INVALID_QUERY);
        }
    }

    /**
     * Pair的left为实体对像，right为字段名，字段名为映射后的
     */
    public static class FieldTypeCacheLoader extends CacheLoader<Pair<Class, String>, Class> {

        @Override
        public Class load(Pair<Class, String> key) throws Exception {
            Class clazz = key.getLeft();
            String fieldName = key.getRight();
            Class result;
            result = getMappingFieldType(clazz, fieldName);
            if (null == result) {
                Field field = FieldUtils.getField(clazz, fieldName, true);
                if (null != field) {
                    result = field.getType();
                }
            }
            if (null != result) {
                if ("id".equals(fieldName) && Serializable.class.equals(result)) {
                    return getGenericIdType(clazz);
                }
                return primitive2Box(result);
            }
            throw JafI18NException.of(ErrorCode.FIELD_NOT_FOUND, fieldName);
        }

        private Class getMappingFieldType(Class clazz, String fieldName) {
            FieldsMapping fieldsMapping = (FieldsMapping) clazz.getAnnotation(FieldsMapping.class);
            if (fieldsMapping == null
                    || fieldsMapping.types().length == 0
                    || fieldsMapping.mappings().length == 0) {
                return null;
            }
            String[] mappings = fieldsMapping.mappings();
            int length = Math.min(mappings.length, fieldsMapping.types().length);
            for (int i = 0; i < length; i++) {
                if (fieldName.equals(mappings[i])) {
                    return fieldsMapping.types()[i];
                }
            }
            return null;
        }

        private Class getGenericIdType(Class clazz) {
            do {
                Type[] genericParameters = ReflectUtil.getGenericParameter(clazz);
                if (genericParameters.length > 0) {
                    return (Class) genericParameters[0];
                }
                clazz = clazz.getSuperclass();
            } while (Object.class != clazz);
            throw JafI18NException.of(ErrorCode.INVALID_QUERY, "[id]字段类型无法推断");
        }

        private Class<?> primitive2Box(Class fieldType) {
            if (fieldType.equals(Long.TYPE)) {
                return Long.class;
            }
            if (fieldType.equals(Integer.TYPE)) {
                return Integer.class;
            }
            if (fieldType.equals(Boolean.TYPE)) {
                return Boolean.class;
            }
            if (fieldType.equals(Byte.TYPE)) {
                return Byte.class;
            }
            if (fieldType.equals(Double.TYPE)) {
                return Double.class;
            }
            if (fieldType.equals(Float.TYPE)) {
                return Float.class;
            }
            return fieldType;
        }
    }
}
