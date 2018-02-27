package cn.jcloud.jaf.common.query;

import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Condition {
    private String field;

    private Operator operator;

    private Object value;

    private Class valueType;

    private Condition() {
    }

    public static Condition of(String field, Operator operator, Object value, Class valueType) {
        if (StringUtils.isBlank(field) || operator == null) {
            throw JafI18NException.of(ErrorCode.INVALID_QUERY);
        }
        Condition condition = new Condition();
        condition.field = field;
        condition.operator = operator;
        condition.value = value;
        condition.valueType = valueType;
        return condition;
    }

    /**
     * 创建一个等于操作的条件
     *
     * @param field     字段名称，与实体字段名一致
     * @param value     字段值
     * @param valueType 字段类型
     * @return
     */
    public static Condition eq(String field, Object value, Class valueType) {
        return Condition.of(field, Operator.EQ, value, valueType);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class getValueType() {
        return valueType;
    }

    public void setValueType(Class valueType) {
        this.valueType = valueType;
    }

    @Override
    public String toString() {
        return field + " " + operator + " " + value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, field, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Condition other = (Condition) obj;
        return operator == other.operator
                && Objects.equals(field, other.field)
                && Objects.equals(value, other.value);
    }
}
