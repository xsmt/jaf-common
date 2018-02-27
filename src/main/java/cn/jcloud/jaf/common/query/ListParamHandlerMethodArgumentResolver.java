package cn.jcloud.jaf.common.query;

import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ListParam参数解析类
 * Created by Wei Han on 2016/1/15.
 *
 * @since 1.0
 */
public class ListParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DEFAULT_OFFSET_PARAMETER = "$offset";
    private static final String DEFAULT_LIMIT_PARAMETER = "$limit";
    private static final String DEFAULT_COUNT_PARAMETER = "$count";
    private static final String DEFAULT_FILTER_PARAMETER = "$filter";
    private static final String DEFAULT_ORDER_BY_PARAMETER = "$orderby";

    private String offsetParameter = DEFAULT_OFFSET_PARAMETER;
    private String limitParameter = DEFAULT_LIMIT_PARAMETER;
    private String countParameter = DEFAULT_COUNT_PARAMETER;
    private String filterParameter = DEFAULT_FILTER_PARAMETER;
    private String orderByParameter = DEFAULT_ORDER_BY_PARAMETER;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return SlicePage.class.equals(parameter.getParameterType())
                || ListParam.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        Class entityClass = getEntityClazz(parameter);
        if (entityClass == null) {
            throw JafI18NException.of("未指定查询的实体类型", ErrorCode.INVALID_QUERY);
        }
        int offset = NumberUtils.toInt(request.getParameter(this.offsetParameter), 0);
        int limit = NumberUtils.toInt(request.getParameter(this.limitParameter), 20);
        boolean count = BooleanUtils.toBoolean(request.getParameter(this.countParameter));

        if (SlicePage.class.equals(parameter.getParameterType())) {
            return slicePage(request, entityClass, offset, limit, count);
        } else {
            return listParam(request, entityClass, offset, limit, count);
        }
    }

    private <T> Object listParam(NativeWebRequest request, Class<T> entityClass, int offset, int limit, boolean count) {
        ListParam<T> listParam = new ListParam<>(entityClass);
        listParam.setOffset(offset);
        listParam.setLimit(limit);
        listParam.setCount(count);
        listParam.parseOrderBys(request.getParameter(this.orderByParameter));
        listParam.parseConditions(request.getParameter(this.filterParameter));
        return listParam;
    }

    private <T> Object slicePage(NativeWebRequest request, Class<T> entityClass, int offset, int limit, boolean count) {
        SlicePage<T> slicePage = new SlicePage<>(entityClass);
        slicePage.setOffset(offset);
        slicePage.setLimit(limit);
        slicePage.setCount(count);
        slicePage.parseOrderBys(request.getParameter(this.orderByParameter));
        return slicePage;
    }

    private static Class getEntityClazz(MethodParameter parameter) {
        Type type = parameter.getGenericParameterType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) parameter.getGenericParameterType();
            Type[] entity = parameterizedType.getActualTypeArguments();
            if (entity.length > 0) {
                return (Class) entity[0];
            }
        }
        return null;
    }

    public String getOffsetParameter() {
        return this.offsetParameter;
    }

    public void setOffsetParameter(String offsetParameter) {
        this.offsetParameter = offsetParameter;
    }

    public String getLimitParameter() {
        return this.limitParameter;
    }

    public void setLimitParameter(String limitParameter) {
        this.limitParameter = limitParameter;
    }

    public String getCountParameter() {
        return this.countParameter;
    }

    public void setCountParameter(String countParameter) {
        this.countParameter = countParameter;
    }

    public String getFilterParameter() {
        return this.filterParameter;
    }

    public void setFilterParameter(String filterParameter) {
        this.filterParameter = filterParameter;
    }

    public String getOrderByParameter() {
        return this.orderByParameter;
    }

    public void setOrderByParameter(String orderByParameter) {
        this.orderByParameter = orderByParameter;
    }
}
