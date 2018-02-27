package cn.jcloud.jaf.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by han on 2017/7/29.
 */
public class JafCorsFilter  extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JafCorsFilter() {
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        this.logger.debug("WAF CorsFilter doFilter start");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE, PATCH");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, Cache-control, Orgname");
        response.addHeader("Access-Control-Max-Age", "1800");
        filterChain.doFilter(request, response);
        this.logger.debug("WAF CorsFilter doFilter end");
    }
}
