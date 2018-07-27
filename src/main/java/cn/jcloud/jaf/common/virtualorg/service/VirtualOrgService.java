package cn.jcloud.jaf.common.virtualorg.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.nd.gaea.WafProperties;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.gaea.rest.security.services.impl.CacheUtil;
import com.nd.gaea.rest.support.SConfKeys;
import com.nd.social.common.virtualorg.domain.VOrgNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class VirtualOrgService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualOrgService.class);

    @Autowired
    private WafSecurityHttpClient httpClient;

    /*
     * 获取虚拟组织节点信息
     */
    public static final String VO_GET_NODE_INFO_API = "virtual_organizations/{vorgId}/organizations/{orgId}";

    private static String VO_NODE_INFO_URL;

    private static LoadingCache<String, VOrgNode> voNodeInfoCache;

    @PostConstruct
    public void postConstruct() {
        String uri = WafProperties.getProperty(SConfKeys.WAF_UC_VORG);
        if (StringUtils.isBlank(uri)) {
            LOGGER.warn("【waf.properties】缺少配置项【{}】，会导致虚拟组织相关功能无法使用，建议进行配置", SConfKeys.WAF_UC_VORG);
            return;
        }
        if (!uri.endsWith("/")) {
            uri = uri + "/";
        }
        VO_NODE_INFO_URL = uri + VO_GET_NODE_INFO_API;
        initVoOrganizationCache();
    }

    /**
     * 获取虚拟组织节点的信息FromCache
     */
    public VOrgNode getVoNodeInfo(String vOrgId, String orgId) {
        Assert.notNull(vOrgId, "vorgId is null");
        Assert.notNull(orgId, "userId is null");

        String key = vOrgId + ',' + orgId;
        return CacheUtil.get(voNodeInfoCache, key);
    }

    /**
     * 虚拟组织获取组织节点信息
     */
    private VOrgNode getVoNodeInfoFromHttp(String vOrgId, String orgId) {
        return httpClient.getForObject(VO_NODE_INFO_URL, VOrgNode.class, vOrgId, orgId);
    }

    /**
     * 初始化 organization cache
     */
    private void initVoOrganizationCache() {
        voNodeInfoCache = CacheBuilder.newBuilder().expireAfterWrite(20, TimeUnit.MINUTES)
                .build(new CacheLoader<String, VOrgNode>() {

                    @Override
                    public VOrgNode load(String key) throws Exception {

                        String[] keys = StringUtils.split(key, ",");
                        return getVoNodeInfoFromHttp(keys[0], keys[1]);
                    }
                });
    }

}
