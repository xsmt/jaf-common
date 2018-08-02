package cn.jcloud.jaf.common.handler;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Wei Han.
 */
public class LogMDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        StringBuilder requestId = new StringBuilder(20);
        requestId.append(RandomStringUtils.randomAlphanumeric(16))
                .append('@')
                .append(Thread.currentThread().getId());
        try {
            MDC.put("RequestId", requestId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}