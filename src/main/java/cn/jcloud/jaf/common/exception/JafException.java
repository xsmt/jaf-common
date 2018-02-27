package cn.jcloud.jaf.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by han on 2017/7/29.
 */
public class JafException extends RuntimeException {
    ResponseEntity<ErrorMessage> responseEntity;

    public JafException(ResponseEntity<ErrorMessage> responseEntity, Throwable cause) {
        super(((ErrorMessage)responseEntity.getBody()).getMessage(), cause);
        this.responseEntity = responseEntity;
    }

    public JafException(ResponseEntity<ErrorMessage> responseEntity) {
        this((ResponseEntity)responseEntity, (Throwable)null);
    }

    public JafException(ErrorMessage errorMessage, HttpStatus status, Throwable cause) {
        this((ResponseEntity)(new ResponseEntity(errorMessage, status)), (Throwable)cause);
    }

    public JafException(ErrorMessage errorMessage, HttpStatus status) {
        this(new ResponseEntity(errorMessage, status));
    }

    public JafException(String code, String message, String detail, HttpStatus status, Throwable cause) {
        this((ErrorMessage)(new ErrorMessage(code, message, detail)), (HttpStatus)status, (Throwable)cause);
    }

    public JafException(String code, String message, String detail, HttpStatus status) {
        this((ErrorMessage)(new ErrorMessage(code, message, detail)), (HttpStatus)status, (Throwable)null);
    }

    public JafException(String code, String message, HttpStatus status, Throwable cause) {
        this((ErrorMessage)(new ErrorMessage(code, message)), (HttpStatus)status, (Throwable)cause);
    }

    public JafException(String code, String message, HttpStatus status) {
        this(code, message, (HttpStatus)status, (Throwable)null);
    }

    public JafException(String code, String message, Throwable cause) {
        this((ErrorMessage)(new ErrorMessage(code, message)), (HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR, (Throwable)cause);
    }

    public JafException(String code, String message) {
        this((String)code, (String)message, (Throwable)((Throwable)null));
    }

    public ResponseEntity<ErrorMessage> getResponseEntity() {
        return this.responseEntity;
    }

    public ErrorMessage getError() {
        return (ErrorMessage)this.responseEntity.getBody();
    }

    public String getLocalizedMessage() {
        return I18NProvider.getString(this.getMessage());
    }
}
