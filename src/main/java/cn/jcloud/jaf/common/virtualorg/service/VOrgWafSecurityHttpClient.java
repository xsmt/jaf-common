package cn.jcloud.jaf.common.virtualorg.service;

import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.social.common.constant.Constants;
import com.nd.social.common.handler.VOrgHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;

/**
 * 支持虚拟组织的WafSecurityHttpClient
 * 当当前请求为虚拟组织时，使用该类进行的http请求会自动在头部加上vorg
 * 为了区别于Spring Bean中的WafSecurityHttpClient，该类不要注册成Bean
 * Created by Wei Han on 2016-11-30.
 */
public class VOrgWafSecurityHttpClient extends WafSecurityHttpClient {

    private static VOrgWafSecurityHttpClient instance;

    public static VOrgWafSecurityHttpClient getInstance() {
        if (null == instance) {
            synchronized (VOrgWafSecurityHttpClient.class) {
                if (null == instance) {
                    instance = new VOrgWafSecurityHttpClient();
                }
            }
        }
        return instance;
    }

    @Override
    protected HttpHeaders mergerHeaders(URI uri, HttpMethod method, HttpHeaders headers) {
        headers = super.mergerHeaders(uri, method, headers);
        if (StringUtils.isBlank(VOrgHandler.getVOrgName())) {
            return headers;
        }
        HttpHeaders tempHeaders = new HttpHeaders();
        if (headers != null) {
            tempHeaders.putAll(headers);
        }
        tempHeaders.add(Constants.VORG_HEADER, VOrgHandler.getVOrgName());
        return tempHeaders;
    }
}