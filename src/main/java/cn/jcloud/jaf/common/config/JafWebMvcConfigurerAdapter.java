package cn.jcloud.jaf.common.config;

import cn.jcloud.jaf.common.util.JafJsonMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import cn.jcloud.jaf.common.query.ListParamHandlerMethodArgumentResolver;
import cn.jcloud.jaf.common.util.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

/**
 * Created by Wei Han on 2016/4/12.
 */
@Configuration
@EnableWebMvc
@EnableAsync(proxyTargetClass = true)
@EnableAspectJAutoProxy
@ComponentScan(value = "cn.jcloud", includeFilters = @ComponentScan.Filter(Controller.class))
public class JafWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    public static final String VERSION = "version";

    @Autowired
    private ConfigurableEnvironment env;

    @PostConstruct
    public void postConstruct() {
        MutablePropertySources propertySources = env.getPropertySources();
        Properties versionProperties = new Properties();
        versionProperties.setProperty(VERSION, JafContext.getProperty(VERSION, "v0.1"));
        propertySources.addLast(new PropertiesPropertySource(VERSION, versionProperties));
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ListParamHandlerMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        // 客户端一再要求整型不可以使用null，所以这样子设计，使用得两边都不影响
        JafJsonMapper.getMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public MessageSource messageSource() {
        return I18NUtil.messageSource();
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
