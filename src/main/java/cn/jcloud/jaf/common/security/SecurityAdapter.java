package cn.jcloud.jaf.common.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Wei Han@ND on 2016/5/9.
 */
public interface SecurityAdapter {
    public boolean isPermit(HttpServletRequest request, HttpServletResponse response, Object handler);
}
