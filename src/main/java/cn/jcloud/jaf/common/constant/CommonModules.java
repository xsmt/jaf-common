package cn.jcloud.jaf.common.constant;

import cn.jcloud.jaf.common.base.domain.Module;

/**
 * 基础模块常量
 * Created by Wei Han on 2016/2/15.
 */
public interface CommonModules {

    Module TENANT = new Module("SERVICE");

    Module DISTRIBUTION = new Module("DISTRIBUTION");

    Module GUEST_WHITE_LIST = new Module("GUEST_WHITE_LIST");

    Module CACHE = new Module("CACHE");

}
