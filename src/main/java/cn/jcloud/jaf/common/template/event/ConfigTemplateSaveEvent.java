package cn.jcloud.jaf.common.template.event;

import com.nd.social.common.tenant.domain.Tenant;

/**
 * 保存配置模板事件
 * 
 * @author hasayaki(125473)
 *
 */
public class ConfigTemplateSaveEvent {

    private Tenant tenant;

    public ConfigTemplateSaveEvent(Tenant tenant) {
        this.tenant = tenant;
    }

    public Tenant getTenant() {
        return this.tenant;
    }
}
