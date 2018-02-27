package cn.jcloud.jaf.common.handler;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hackingwu.
 */
public class LogMDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        StringBuilder requestId = new StringBuilder(32);
        if (StringUtils.isNotBlank(request.getHeader("X-B3-TraceId"))){
        	requestId.append(request.getHeader("X-B3-TraceId"));
        } else {
        	requestId.append(RandomStringUtils.randomAlphanumeric(16))
            	.append('@').append(Thread.currentThread().getId());
        }
        
        try {
            MDC.put("RequestId", requestId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}