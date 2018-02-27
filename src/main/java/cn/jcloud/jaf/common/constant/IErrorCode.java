package cn.jcloud.jaf.common.constant;

import org.springframework.http.HttpStatus;

/**
 * Created by Wei Han on 2016/4/28.
 */
public interface IErrorCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
