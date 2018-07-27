package cn.jcloud.jaf.common.config;

import cn.jcloud.jaf.common.handler.LogMDCFilter;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * web项目的启动配置
 * Created by Wei Han on 2016/4/20.
 */
@Order(1)
public abstract class AbstractJafWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.initLogMDCFilter(servletContext);
        super.onStartup(servletContext);
        this.initFilters(servletContext);
    }

    protected EnumSet<DispatcherType> getDispatcherTypes() {
        return this.isAsyncSupported()?EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ASYNC):EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
    }

    private void initFilters(ServletContext servletContext) {
//        this.addFilter(servletContext, "exceptionFilter", new DelegatingFilterProxy("exceptionFilter"));
        this.initCharacterEncodingFilter(servletContext);
        this.addFilter(servletContext, "wafCorsFilter", new JafCorsFilter());
        this.addFilter(servletContext, "wafHttpMethodOverrideFilter", new JafHttpMethodOverrideFilter());
    }

    protected void initCharacterEncodingFilter(ServletContext servletContext) {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        this.addFilter(servletContext, "characterEncodingFilter", characterEncodingFilter);
    }

    protected void addFilter(ServletContext servletContext, String filterName, Filter filter) {
        javax.servlet.FilterRegistration.Dynamic filterRegistration = servletContext.addFilter(filterName, filter);
        filterRegistration.setAsyncSupported(this.isAsyncSupported());
        filterRegistration.addMappingForUrlPatterns(this.getDispatcherTypes(), false, new String[]{"/*"});
    }

    protected void initLogMDCFilter(ServletContext servletContext) {
        LogMDCFilter logMDCFilter = new LogMDCFilter();
        javax.servlet.FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("logMDCFilter", logMDCFilter);
        filterRegistration.setAsyncSupported(this.isAsyncSupported());
        filterRegistration.addMappingForUrlPatterns(this.getDispatcherTypes(), false, "/*");
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{};
    }

}
