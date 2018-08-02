package cn.jcloud.jaf.common.config;

import cn.jcloud.gaea.util.WafJsonMapper;
import cn.jcloud.jaf.common.taskschedule.core.TaskScheduleSupportCondition;
import cn.jcloud.jaf.common.taskschedule.domain.MsgInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Wei Han on 2016/6/15.
 */
@Configuration
@Conditional(TaskScheduleSupportCondition.class)
@PropertySource(value = "classpath:redis.taskschedule.properties")
public class TaskScheduleConfigurerAdapter {

    private final ObjectMapper objectMapper;

    // host原使用@Value("${ts.redis.host:'localhost'}")赋值
    // 在waf 0.15.3的tomcat环境下该配置文件的String类型无法使用，暂使用该表达式规避
    @Value("${ts.redis.host}")
    private String host;

    // 由于上述原因，这里暂时无法使用密码配置
    @Value("${ts.redis.password:}")
    private String password;

    @Value("${ts.redis.port}")
    private int port;

    @Value("${ts.redis.db}")
    private int db;

    @Value("${ts.redis.pool.maxIdle}")
    private int maxIdle;

    @Value("${ts.redis.pool.minIdle}")
    private int minIdle;

    @Value("${ts.redis.pool.maxWait}")
    private long maxWait;

    @Value("${ts.redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    public TaskScheduleConfigurerAdapter() {
        objectMapper = objectMapper();
    }

    protected ObjectMapper objectMapper() {
        return WafJsonMapper.getMapper();
    }

    private RedisConnectionFactory connectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPassword(StringUtils.trimToNull(password));
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setDatabase(db);
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.afterPropertiesSet();

        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, MsgInfo> taskScheduleRedisTemplate() {
        Jackson2JsonRedisSerializer<MsgInfo> serializer = new Jackson2JsonRedisSerializer<>(MsgInfo.class);
        serializer.setObjectMapper(objectMapper);

        RedisTemplate<String, MsgInfo> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
