package cn.jcloud.jaf.common.exception;

import cn.jcloud.jaf.common.constant.IErrorCode;
import cn.jcloud.jaf.common.util.I18NUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Objects;

/**
 * Waf国际化异常
 * Create By Wei Han
 */
public class JafI18NException extends JafException {

    private String code;
    private String message;
    private HttpStatus status;
    private String[] args;

    protected JafI18NException(String code, String message, HttpStatus status, Throwable cause, String... args) {
        super(code, message, status, cause);
        this.code = code;
        this.message = message;
        this.status = status;
        this.args = args;
    }

    /**
     * code 为 "WAF/INTERNAL_SERVER_ERROR";
     * HttpStatus 为 HttpStatus.INTERNAL_SERVER_ERROR (500);
     * message 为 message
     *
     * @param message 错误消息描述
     */
    public static JafI18NException of(String message, String... args) {
        return of("WAF/INTERNAL_SERVER_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR, null, args);
    }

    /**
     * code 为 "WAF/INTERNAL_SERVER_ERROR";
     * HttpStatus 为 HttpStatus.INTERNAL_SERVER_ERROR (500);
     * message 为 message
     *
     * @param message 错误消息描述
     */
    public static JafI18NException of(String message, Throwable cause, String... args) {
        return of("WAF/INTERNAL_SERVER_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR, cause, args);
    }

    public static JafI18NException of(String message, IErrorCode errorCode, String... args) {
        return of(errorCode.getCode(), message, errorCode.getHttpStatus(), null, args);
    }

    public static JafI18NException of(IErrorCode errorCode, String... args) {
        return of(errorCode.getCode(), errorCode.getMessage(), errorCode.getHttpStatus(), null, args);
    }

    public static JafI18NException of(String code, String message, HttpStatus status, String... args) {
        return of(code, message, status, null, args);
    }

    public static JafI18NException of(String message, IErrorCode errorCode, Throwable cause, String... args) {
        if (StringUtils.isBlank(message)) {
            message = errorCode.getMessage();
        }
        return of(errorCode.getCode(), message, errorCode.getHttpStatus(), cause, args);
    }

    public static JafI18NException of(IErrorCode errorCode, Throwable cause, String... args) {
        return of(errorCode.getCode(), errorCode.getMessage(), errorCode.getHttpStatus(), cause, args);
    }

    public static JafI18NException of(String code, String message, HttpStatus status, Throwable cause, String... args) {
        String[] i18nArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            i18nArgs[i] = I18NUtil.getDefaultI18NMsg(args[i], args[i]);
        }
        message = I18NUtil.getDefaultI18NMsg(message, message, i18nArgs);
        return new JafI18NException(code, message, status, cause);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JafI18NException that = (JafI18NException) o;

        return Objects.equals(this.code, that.code)
                && Objects.equals(this.message, that.message)
                && Objects.equals(this.status, that.status)
                && Arrays.equals(args, that.args);

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (args != null ? Arrays.hashCode(args) : 0);
        return result;
    }
}
