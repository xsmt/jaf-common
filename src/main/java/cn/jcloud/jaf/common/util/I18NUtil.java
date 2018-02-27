package cn.jcloud.jaf.common.util;

import cn.jcloud.jaf.common.config.JafContext;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 国际化工具类
 * Created by Wei Han on 2016/4/29.
 */
public class I18NUtil {

    private static MessageSource messageSource;

    static {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        String[] commonBasenames = new String[]{"common-module", "error-code"};
        String[] customBasenames = JafContext.getI18NBasenames();
        String[] basenames = ArrayUtils.addAll(commonBasenames, customBasenames);
        resourceBundleMessageSource.setBasenames(basenames);
        messageSource = resourceBundleMessageSource;
    }

    private I18NUtil() {
    }

    public static MessageSource messageSource() {
        return messageSource;
    }

    /**
     * 获取代码对应的国际化信息
     *
     * @param code 代码
     * @return 代码对应的国际化信息，当不存在时返回null
     */
    public static String getI18NMsg(String code, Object... args) {
        return messageSource.getMessage(code, args, null, LocaleContextHolder.getLocale());
    }

    /**
     * 获取代码对应的国际化信息
     *
     * @param code           代码
     * @param defaultMessage 默认国际化信息
     * @return 代码对应的国际化信息，当code无对应国际化信息时，返回默认国际化信息
     */
    public static String getDefaultI18NMsg(String code, String defaultMessage, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }
}
