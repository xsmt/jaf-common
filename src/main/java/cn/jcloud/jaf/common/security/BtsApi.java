package cn.jcloud.jaf.common.security;

import java.lang.annotation.*;

/**
 * <p>Title: Module Information         </p>
 * <p>Description: Function Description </p>
 * <p>Copyright: Copyright (c) 2017     </p>
 * <p>Company: ND Co., Ltd.       </p>
 * <p>Create Time: 2017年1月26日           </p>
 * @author Linhua(831008)
 * <p>Update Time:                      </p>
 * <p>Updater:                          </p>
 * <p>Update Comments:由于BTS请求没有用户的概念，也无法从WAF中获得用户id，所以BTS请求强制要求suid参数</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface BtsApi {

}
