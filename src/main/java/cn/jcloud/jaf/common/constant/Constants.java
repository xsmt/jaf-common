package cn.jcloud.jaf.common.constant;

/**
 * 常量
 * Created by Wei Han on 2016/2/15.
 */
public class Constants {
    /**
     * 通过waf.properties获取realm值，此处为waf.properties中的key
     * 已过时，请使用{@code RealmService.getRealm}替换
     */
    @Deprecated
    public static final String REALM_PROPERTY = "waf.uc.realm";

    public static final String VORG_HEADER = "vorg";

    private Constants() {
    }

}
