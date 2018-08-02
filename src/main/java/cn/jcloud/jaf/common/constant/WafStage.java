package cn.jcloud.jaf.common.constant;

import cn.jcloud.gaea.client.support.WafContext;

/**
 * waf环境类型
 * Created by Wei Han on 2016/4/14.
 */
public enum WafStage {

    DEVELOPMENT,
    TEST,
    PRESSURE,
    PREPRODUCTION,
    PRODUCT,
    AWS;

    public boolean is() {
        return name().equalsIgnoreCase(WafContext.getEnvironment());
    }
}
