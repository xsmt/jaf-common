package cn.jcloud.jaf.common.config;

import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Jaf上下文
 * Created by Wei Han on 2016/4/22.
 */
public class JafContext {

    public static final String JAF_PROPERTIES_FILE_NAME = "jaf.properties";
    public static final String PROJECT_NAME = "project.name";
    public static final String ERROR_CODE_PREFIX = "error.code.prefix";
    private static final String TABLE_PREFIX = "table.prefix";

    public static final String TS_URI = "ts.uri";
    public static final String TS_TICKET_URI = "ts.ticket.uri";
    public static final String TS_TICKET_PWD = "ts.ticket.pwd";

    public static final String BEARER_COMMON_UID = "bearer.common.uid";
    public static final String BEARER_GROUP_UID_FORMAT = "bearer.%s.uid";
    public static final String REDIS_TASK_SCHEDULE_PROPERTIES = "redis.taskschedule.properties";

    public static final String PROFILE_ASPECT_SERVICE_THRESHOLD = "profile.aspect.service.threshold";
    public static final String PROFILE_ASPECT_DAO_THRESHOLD = "profile.aspect.service.threshold";
    public static final String PROFILE_ASPECT_SERVICE_ARG_PRINT = "profile.aspect.service.arg.print";
    public static final String PROFILE_ASPECT_DAO_ARG_PRINT = "profile.aspect.dao_arg.print";

    private static Properties properties;

    private static boolean taskScheduleSupport;
    private static String tsUri;

    private static boolean redisSupport;

    static {
        taskScheduleSupport = existsPropertiesFile("redis.taskschedule.properties");
        redisSupport = existsPropertiesFile("redis.properties");
        try {
            properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(JAF_PROPERTIES_FILE_NAME));
            checkAndSetProperties(properties);
        } catch (FileNotFoundException e) {
            throw JafI18NException.of(ErrorCode.CONFIG_MISSING, JAF_PROPERTIES_FILE_NAME);
        } catch (IOException e) {
            throw JafI18NException.of(ErrorCode.CONFIG_LOADING_FAIL, JAF_PROPERTIES_FILE_NAME);
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

    public static String getBearerCommonUid() {
        return properties.getProperty(BEARER_COMMON_UID);
    }

    public static String getBearerUid(String group) {
        return properties.getProperty(String.format(BEARER_GROUP_UID_FORMAT, group));
    }

    public static String defaultBearerUid(String group) {
        return properties.getProperty(
                String.format(BEARER_GROUP_UID_FORMAT, group),
                getBearerCommonUid());
    }

    /**
     * 是否支持任务队列（任务调度）
     */
    public static boolean isTaskScheduleSupport() {
        return taskScheduleSupport;
    }

    public static String getTsUri() {
        return tsUri;
    }

    public static String getTsTicketUri() {
        return properties.getProperty(TS_TICKET_URI);
    }

    public static String getTsTicketPwd() {
        return properties.getProperty(TS_TICKET_PWD);
    }

    public static void checkTaskScheduleSupport() {
        if (isTaskScheduleSupport()) {
            return;
        }
        throw JafI18NException.of(ErrorCode.CONFIG_MISSING, JafContext.REDIS_TASK_SCHEDULE_PROPERTIES);
    }

    public static boolean isRedisSupport() {
        return redisSupport;
    }

    private static void checkAndSetProperties(Properties properties) {
        if (!properties.containsKey(PROJECT_NAME)) {
            throw JafI18NException.of(ErrorCode.CONFIG_MISSING_ITEM, JAF_PROPERTIES_FILE_NAME, PROJECT_NAME);
        }
        if (!properties.containsKey(ERROR_CODE_PREFIX)) {
            throw JafI18NException.of(ErrorCode.CONFIG_MISSING_ITEM, JAF_PROPERTIES_FILE_NAME, ERROR_CODE_PREFIX);
        }

        if (taskScheduleSupport && properties.containsKey(TS_URI)) {
            tsUri = StringUtils.trimToNull(properties.getProperty(TS_URI));
            if (null != tsUri) {
                tsUri += "/";
            }
        }
    }

    private static boolean existsPropertiesFile(String propertiesFile) {
        return null != JafContext.class.getClassLoader().getResource(propertiesFile);
    }
}
