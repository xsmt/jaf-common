package cn.jcloud.jaf.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Service层配置基础类
 * Created by Wei Han on 2016/4/13.
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
@EnableScheduling
@ComponentScan(basePackages = "com.jcloud",
        excludeFilters = @ComponentScan.Filter({Controller.class, Configuration.class,
                Repository.class, ControllerAdvice.class}))
public class ServiceConfigurerAdapter {

}
