package cn.jcloud.jaf.common.config;

import cn.jcloud.gaea.rest.AbstractWafWebApplicationInitializer;
import cn.jcloud.jaf.common.handler.LogMDCFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * web项目的启动配置
 * Created by Wei Han on 2016/4/20.
 */
public abstract class AbstractJafWebApplicationInitializer extends AbstractWafWebApplicationInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{JafWebSecurityConfigurerAdapter.class};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.initLogMDCFilter(servletContext);
        super.onStartup(servletContext);

    }

    protected void initLogMDCFilter(ServletContext servletContext) {
        LogMDCFilter logMDCFilter = new LogMDCFilter();
        javax.servlet.FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("logMDCFilter", logMDCFilter);
        filterRegistration.setAsyncSupported(this.isAsyncSupported());
        filterRegistration.addMappingForUrlPatterns(this.getDispatcherTypes(), false, "/*");
    }
}
