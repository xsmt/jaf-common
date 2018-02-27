package cn.jcloud.jaf.common.config;

import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Saf上下文
 * Created by Wei Han on 2016/4/22.
 */
public class JafContext {

    public static final String SAF_PROPERTIES_FILE_NAME = "jaf.properties";
    public static final String PROJECT_NAME = "project.name";
    public static final String ERROR_CODE_PREFIX = "error.code.prefix";
    private static final String TABLE_PREFIX = "table.prefix";

    private static Properties properties;

    private static boolean redisSupport;

    static {
        redisSupport = existsPropertiesFile("redis.properties");
        try {
            properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(SAF_PROPERTIES_FILE_NAME));
            checkAndSetProperties(properties);
        } catch (FileNotFoundException e) {
            throw JafI18NException.of(ErrorCode.CONFIG_MISSING, SAF_PROPERTIES_FILE_NAME);
        } catch (IOException e) {
            throw JafI18NException.of(ErrorCode.CONFIG_LOADING_FAIL, SAF_PROPERTIES_FILE_NAME);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String[] getI18NBasenames() {
        if (null != properties && properties.containsKey("i18n.basenames")) {
            return properties.getProperty("i18n.basenames").split(",");
        }
        return new String[]{};
    }

    public static String getProjectName() {
        return properties.getProperty(PROJECT_NAME);
    }

    public static String getErrorCodePrefix() {
        if (null == properties) {
            return "JAF";
        }
        return properties.getProperty(ERROR_CODE_PREFIX);
    }

    public static String getTablePrefix() {
        if (properties.containsKey(TABLE_PREFIX)) {
            return properties.getProperty(TABLE_PREFIX);
        }
        return "t";
    }

    public static boolean isRedisSupport() {
        return redisSupport;
    }

    private static void checkAndSetProperties(Properties properties) {
        if (!properties.containsKey(PROJECT_NAME)) {
            throw JafI18NException.of(ErrorCode.CONFIG_MISSING_ITEM, SAF_PROPERTIES_FILE_NAME, PROJECT_NAME);
        }
        if (!properties.containsKey(ERROR_CODE_PREFIX)) {
            throw JafI18NException.of(ErrorCode.CONFIG_MISSING_ITEM, SAF_PROPERTIES_FILE_NAME, ERROR_CODE_PREFIX);
        }
    }

    private static boolean existsPropertiesFile(String propertiesFile) {
        return null != JafContext.class.getClassLoader().getResource(propertiesFile);
    }
}
