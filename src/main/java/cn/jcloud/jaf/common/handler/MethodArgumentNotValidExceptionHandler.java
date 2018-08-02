package cn.jcloud.jaf.common.handler;

import cn.jcloud.gaea.client.exception.ResponseErrorMessage;
import cn.jcloud.gaea.rest.exceptions.rest.AbstractRestErrorHandler;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.util.ValidatorUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证失败异常处理
 * Created by Wei Han on 2016/2/15.
 *
 * @since 1.0
 */
public class MethodArgumentNotValidExceptionHandler extends AbstractRestErrorHandler {

    @Override
    protected ResponseErrorMessage getBody(Throwable throwable, HttpServletRequest request) {
        ResponseErrorMessage errorMessage = new ResponseErrorMessage(throwable);
        String message = ValidatorUtil.getErrorMessageStr(
                ((MethodArgumentNotValidException) throwable).getBindingResult());
        errorMessage.setMessage(message);
        errorMessage.setDetail(appendStackTrace(null, throwable));
        errorMessage.setCode(getCode(throwable, request));
        errorMessage.setThrowable(JafI18NException.of(ErrorCode.INVALID_ARGUMENT));
        updateRemoteErrorMessage(errorMessage, request);
        return errorMessage;
    }

    @Override
    protected String getCode(Throwable throwable, HttpServletRequest request) {
        return ErrorCode.INVALID_ARGUMENT.getCode();
    }

    @Override
    protected HttpStatus getHttpStatus(Throwable throwable, HttpServletRequest request) {
        return HttpStatus.BAD_REQUEST;
    }
}
