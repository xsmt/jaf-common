package cn.jcloud.jaf.common.exception;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by han on 2017/7/29.
 */
public class ErrorMessage  implements Serializable {
    private static final long serialVersionUID = -5401402542472113075L;
    private String code;
    private String message;
    private String detail;
    private ResponseErrorMessage cause;

    public ErrorMessage() {
    }

    public ErrorMessage(String code) {
        this(code, (String)null, (String)null);
    }

    public ErrorMessage(String code, String message) {
        this(code, message, (String)null);
    }

    public ErrorMessage(String code, String message, String detail) {
        this(code, message, detail, (ResponseErrorMessage)null);
    }

    public ErrorMessage(String code, String message, String detail, ResponseErrorMessage cause) {
        this.message = StringUtils.isEmpty(message)?"null($WAF)":message;
        this.code = code;
        this.detail = detail;
        this.cause = cause;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLocalizedMessage() {
        return I18NProvider.getString(this.message);
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ResponseErrorMessage getCause() {
        return this.cause;
    }

    public void setCause(ResponseErrorMessage cause) {
        this.cause = cause;
    }

    public ResponseErrorMessage convert(Throwable throwable) {
        ResponseErrorMessage responseErrorMessage = new ResponseErrorMessage(throwable);
        responseErrorMessage.setMessage(this.getLocalizedMessage());
        responseErrorMessage.setCode(this.code);
        responseErrorMessage.setDetail(this.detail);
        responseErrorMessage.setCause(this.cause);
        return responseErrorMessage;
    }
}
