package cn.jcloud.jaf.common.cs.service;

import cn.jcloud.gaea.client.http.WafSecurityHttpClient;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.cs.core.CSConfig;
import cn.jcloud.jaf.common.cs.core.CSSupportCondition;
import cn.jcloud.jaf.common.cs.core.EnumParser;
import cn.jcloud.jaf.common.cs.domain.EnumType;
import cn.jcloud.jaf.common.exception.JafI18NException;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Component
@Conditional(CSSupportCondition.class)
public class CSEnumService {

    private static Logger LOGGER = LoggerFactory.getLogger(CSEnumService.class);

    @Autowired
    private WafSecurityHttpClient httpClient;

    @Autowired
    private CSConfig csConfig;

    @Autowired
    private EnumParser enumParser;

    /**
     * 初始化缓存
     */
    private void initEnums() throws IOException {

        Resource enumTypeResource = new ClassPathResource(csConfig.getEnumPath());
        if (!enumTypeResource.exists()) {
            return;
        }

        List<EnumType> enumTypes = enumParser.parse(enumTypeResource.getInputStream());
        for (EnumType enumType : enumTypes) {
            httpClient.postForObject(csConfig.formatEnumCreateUrl(), enumType, Map.class);
        }
    }


    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            initEnums();
            LOGGER.info("enum type create success");
        } catch (IOException e) {
            LOGGER.error("enum type format error:", e);
        }
    }
}
