package cn.jcloud.jaf.common.config;

import com.nd.gaea.rest.config.WafWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Saf Web安全通用类
 * Created by Wei Han on 2016/4/6.
 */
@Configuration
@Order(101)
public class JafWebSecurityConfigurerAdapter extends WafWebSecurityConfigurerAdapter {
}
