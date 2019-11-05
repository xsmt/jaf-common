package cn.jcloud.jaf.common.tenant.service;

import cn.jcloud.gaea.WafProperties;
import cn.jcloud.gaea.client.http.WafSecurityHttpClient;
import cn.jcloud.gaea.client.support.WafClientContextHolder;
import cn.jcloud.gaea.rest.support.WafContext;
import cn.jcloud.gaea.util.UrlUtil;
import cn.jcloud.jaf.common.tenant.domain.Tenant;

/**
 * Created by Wei Han on 2016-09-05.
 */
public class DefaultTenantAutoCreateImpl implements TenantAutoCreatable {

    public static final String WAF_UC_GET_ORG_INFO = "waf.uc.get.org";
    public static final String orgInfoUrl;

    static {
        orgInfoUrl = UrlUtil.combine(WafProperties.getProperty(WafContext.WAF_UC_URI),
                        WafProperties.getProperty(WAF_UC_GET_ORG_INFO, "organizations/{id}?suid={suid}"));
    }
    @Override
    public Tenant getByOrgId(String orgId) {
        WafSecurityHttpClient httpClient = new WafSecurityHttpClient();
        Organization organization = httpClient.getForObject(orgInfoUrl, Organization.class, orgId, WafClientContextHolder.getToken().getUserId());
        Tenant tenant = new Tenant();
        tenant.setOrgId(organization.getOrgId().toString());
        tenant.setName(organization.getOrgCode());
        tenant.setAppName(organization.getOrgName());
        tenant.setRealm(organization.getOrgCode() + ".j-cloud.cn");
        return tenant;
    }

    private static class Organization {
        private Long orgId;
        private String orgCode;
        private String orgName;

        public Long getOrgId() {
            return orgId;
        }

        public void setOrgId(Long orgId) {
            this.orgId = orgId;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }
    }
}
