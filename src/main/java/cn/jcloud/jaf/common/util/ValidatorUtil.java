package cn.jcloud.jaf.common.util;

import com.google.common.annotations.VisibleForTesting;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.handler.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author wuzj(971643)
 */
public class ValidatorUtil {

    private static Validator validator;

    private ValidatorUtil() {
    }

    public static String getErrorMessageStr(BindingResult result) {
        if (result == null || !result.hasErrors()) {
            return "";
        }
        StringBuilder errorMessage = new StringBuilder();
        boolean start = true;
        for (FieldError fieldError : result.getFieldErrors()) {
            if (start) {
                start = false;
            } else {
                errorMessage.append(',');
            }
            errorMessage.append(fieldError.getDefaultMessage());
        }
        return errorMessage.toString();
    }

    private static Validator getValidator() {
        if (null == validator) {
            synchronized (ValidatorUtil.class) {
                if (null != validator) {
                    return validator;
                }
                if (SpringContextHolder.getApplicationContext() != null
                        && SpringContextHolder.existsBean("validator")) {
                    validator = SpringContextHolder
                            .getBean("validator", ValidatorFactory.class)
                            .getValidator();
                } else {
                    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                    validator = factory.getValidator();
                }
            }
        }
        return validator;
    }

    public static <T> String validate(T t) {
        StringBuilder errorMessage = new StringBuilder();
        Set<ConstraintViolation<T>> constraintViolations = getValidator().validate(t);
        boolean start = true;
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            if (start) {
                start = false;
            } else {
                errorMessage.append(',');
            }
            errorMessage.append(constraintViolation.getMessage());
        }
        return errorMessage.toString();
    }

    public static <T> String validateAndThrow(T t) {
        String validateError = validate(t);
        if (StringUtils.isNotBlank(validateError)) {
            throw JafI18NException.of(validateError, ErrorCode.INVALID_ARGUMENT);
        }
        return validateError;
    }

    @VisibleForTesting
    public static void mock(Validator mockValidator) {
        validator = mockValidator;
    }

    @VisibleForTesting
    public static void mock() {
        validator = Mockito.mock(Validator.class);
    }
}