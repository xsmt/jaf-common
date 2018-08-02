package cn.jcloud.jaf.common.constant;

import cn.jcloud.jaf.common.config.JafContext;
import org.springframework.http.HttpStatus;

/**
 * List error messages of server internal exception or error
 *
 * @since 1.0
 */
public enum ErrorCode implements IErrorCode {

    // 租户和用户相关
    OUT_OF_SERVICE(HttpStatus.PAYMENT_REQUIRED, "OUT_OF_SERVICE", "error.code.out.of.service"),
    MISSING_ORG_ID(HttpStatus.BAD_REQUEST, "MISSING_ORG_ID", "error.code.missing.org.id"),
    MISSING_SUID(HttpStatus.BAD_REQUEST, "MISSING_SUID", "error.code.missing.suid"),
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "USER_NOT_EXIST", "error.code.user.not.exist"),
    USER_NOT_LOGIN(HttpStatus.UNAUTHORIZED, "USER_NOT_LOGIN", "error.code.user.not.login"),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "NO_PERMISSION", "error.code.no.permission"),
    WRONG_AUTH_TYPE(HttpStatus.FORBIDDEN, "WRONG_AUTH_TYPE", "error.code.wrong.auth.type"),

    // 请求相关
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT", "error.code.invalid.argument"),
    REQUIRE_ARGUMENT(HttpStatus.BAD_REQUEST, "REQUIRE_ARGUMENT", "error.code.require.argument"),
    INVALID_OPERATOR(HttpStatus.NOT_ACCEPTABLE, "INVALID_OPERATOR", "error.code.invalid.operator"),

    //ListParam相关
    INVALID_QUERY(HttpStatus.BAD_REQUEST, "INVALID_QUERY", "error.code.invalid.query"),
    FIELD_NOT_FOUND(HttpStatus.BAD_REQUEST, "FIELD_NOT_FOUND", "error.code.field.not.found"),

    // 没有数据
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "DATA_NOT_FOUND", "error.code.data.not.found"),

    //配置相关
    CONFIG_LOADING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "CONFIG_LOADING_FAIL", "error.code.config.loading.fail"),
    CONFIG_MISSING(HttpStatus.INTERNAL_SERVER_ERROR, "CONFIG_MISSING", "error.code.config.missing"),
    CONFIG_MISSING_ITEM(HttpStatus.INTERNAL_SERVER_ERROR, "CONFIG_MISSING_ITEM", "error.code.config.missing.item"),

    //程序错误
    FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "FAIL", "error.code.fail");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    public static final String PREFIX = JafContext.getErrorCodePrefix();

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getCode() {
        return PREFIX + this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
