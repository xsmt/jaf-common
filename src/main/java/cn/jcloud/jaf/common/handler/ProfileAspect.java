package cn.jcloud.jaf.common.handler;

import cn.jcloud.jaf.common.config.JafContext;
import cn.jcloud.jaf.common.exception.JafI18NException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wei Han
 */
@Aspect
@Component
public class ProfileAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileAspect.class);

    // Service层的日志输出阈值
    private static final int SERVICE_THRESHOLD = Integer.parseInt(JafContext.getProperty(JafContext.PROFILE_ASPECT_SERVICE_THRESHOLD,"50"));
    // Dao层 的日志输出阈值
    private static final int DAO_THRESHOLD = Integer.parseInt(JafContext.getProperty(JafContext.PROFILE_ASPECT_DAO_THRESHOLD,"20"));
    // 是否打印Service层方法参数
    private static final boolean SERVICE_ARG_PRINT = Boolean.parseBoolean(JafContext.getProperty(JafContext.PROFILE_ASPECT_SERVICE_ARG_PRINT,"false"));
    // 是否打印Dao层方法参数
    private static final boolean DAO_ARG_PRINT = Boolean.parseBoolean(JafContext.getProperty(JafContext.PROFILE_ASPECT_DAO_ARG_PRINT,"false"));

    @Pointcut("(@within(org.springframework.web.bind.annotation.RestController)" +
            "||@within(org.springframework.stereotype.Controller))" +
            "&& execution(public * *(..))")
    public void contControllerExec() {
        //Empty
    }

    @Around("contControllerExec()")
    public Object aroundControllerMethod(ProceedingJoinPoint jp) throws Throwable {
        long timeStart = System.currentTimeMillis();
        Object ret;
        try {
            ret = jp.proceed();
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            long cost = end - timeStart;
            StringBuilder logStringBuilder = getControllerLogSB(jp.getSignature().toString().split("\\s|\\(")[1]);
            logStringBuilder.append(" ");
            if (e instanceof JafI18NException) {
                logStringBuilder.append(((JafI18NException) e).getError().getCode());
            } else {
                logStringBuilder.append("INTERNAL_SERVER_ERROR");
            }
            logStringBuilder.append(" \"");
            logStringBuilder.append(e.toString());
            logStringBuilder.append("\" ");
            String strLog = String.format("Controller %04d %s ", cost, logStringBuilder.toString());
            LOGGER.error(strLog);
            throw e;
        }
        long end = System.currentTimeMillis();
        long cost = end - timeStart;

        StringBuilder logStringBuilder = getControllerLogSB(jp.getSignature().toString().split("\\s|\\(")[1]);
        String strLog = String.format("Controller %04d %s ", cost, logStringBuilder.toString());
        LOGGER.info(strLog);

        return ret;
    }

    private StringBuilder getControllerLogSB(String function) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        StringBuilder logStringBuilder = new StringBuilder();

        Long orgId = TenantHandler.getTenant();
        if (null == orgId) {
            logStringBuilder.append("-");
        } else {
            logStringBuilder.append(orgId);
        }
        String vOrgId = VOrgHandler.getVOrgId();
        if (StringUtils.isEmpty(vOrgId)) {
            logStringBuilder.append("#-");
        } else {
            logStringBuilder.append("#").append(vOrgId);
        }
        logStringBuilder.append(" ");
        String uid = UserHandler.getUser();
        if (StringUtils.isEmpty(uid)) {
            logStringBuilder.append("-");
        } else {
            logStringBuilder.append(uid);
        }
        String BearerUid = UserHandler.getBearerUser();
        if (StringUtils.isEmpty(BearerUid)) {
            logStringBuilder.append("#-");
        } else {
            logStringBuilder.append("#").append(BearerUid);
        }
        logStringBuilder.append(" ");
        logStringBuilder.append(request.getMethod());
        logStringBuilder.append(" ");
        logStringBuilder.append(function);
        logStringBuilder.append(" ");
        String strUserAgent = request.getHeader("user-agent");
        if (StringUtils.isEmpty(strUserAgent)) {
            logStringBuilder.append(request.getHeader("-"));
        } else {
            logStringBuilder.append("\"");
            logStringBuilder.append(request.getHeader("user-agent"));
            logStringBuilder.append("\"");
        }
        return logStringBuilder;
    }

    @Pointcut("(@within(org.springframework.stereotype.Service)" +
            "||target(cn.jcloud.jaf.common.base.service.BaseService))" +
            "&& execution(public * *(..))")
    public void contServiceExec() {
        //Empty
    }

    @Around("contServiceExec()")
    public Object aroundServiceMethod(ProceedingJoinPoint jp) throws Throwable {
        long timeStart = System.currentTimeMillis();
        Object ret = jp.proceed();
        long timeEnd = System.currentTimeMillis();
        long cost = timeEnd - timeStart;

        if (UserHandler.getUser() != null && cost >= SERVICE_THRESHOLD) {
            String strFuncSignature = jp.getSignature().toString();
            if (SERVICE_ARG_PRINT) {
                String argString = getFuncArgs(jp);
                strFuncSignature = strFuncSignature.substring(0, strFuncSignature.indexOf("(")) + "(" + argString + ")";
            }

            String strLog = String.format("Service %04d %s", cost, strFuncSignature);
            LOGGER.info(strLog);
        }

        return ret;
    }

    @Pointcut("@within(org.springframework.stereotype.Repository)")
    public void contDaoExec() {
        //Empty
    }

    @Around("contDaoExec()")
    public Object aroundDaoMethod(ProceedingJoinPoint jp) throws Throwable {
        long timeStart = System.currentTimeMillis();
        Object ret = jp.proceed();
        long timeEnd = System.currentTimeMillis();
        long cost = timeEnd - timeStart;

        if (UserHandler.getUser() != null && cost >= DAO_THRESHOLD) {
            String strFuncSignature = jp.getSignature().toString();
            if (DAO_ARG_PRINT) {
                String argString = getFuncArgs(jp);
                strFuncSignature = strFuncSignature.substring(0, strFuncSignature.indexOf("(")) + "(" + argString + ")";
            }

            String strLog = String.format("DB %04d %s", cost, strFuncSignature);
            LOGGER.info(strLog);
        }

        return ret;
    }

    private String getFuncArgs(ProceedingJoinPoint jp) {
        StringBuilder argString = new StringBuilder();
        for (Object x : jp.getArgs()) {
            if (x != null) {
                argString.append(x.toString());
                argString.append('+');
            }
        }
        return argString.toString();
    }

}
