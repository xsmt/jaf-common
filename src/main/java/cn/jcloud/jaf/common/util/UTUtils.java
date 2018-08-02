package cn.jcloud.jaf.common.util;

import cn.jcloud.gaea.WafException;
import cn.jcloud.jaf.common.base.domain.BaseTenantDomain;
import cn.jcloud.jaf.common.constant.IErrorCode;
import cn.jcloud.jaf.common.handler.TenantHandler;
import cn.jcloud.jaf.common.tenant.domain.Tenant;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * 单元测试工具类
 * Created by Wei Han on 2016/5/16.
 */
public class UTUtils {

    public static final long TENANT_ID = 1L;

    /**
     * 校验异常是否为预期的错误码
     *
     * @param errorCode 预期的错误码
     * @param ex        实际异常
     */
    public static void verifyErrorCode(IErrorCode errorCode, Exception ex) {
        if (ex == null) {
            fail("预期抛出[JafI18NException]，但实际未抛出");
        }
        if (!WafException.class.isAssignableFrom(ex.getClass())) {
            fail("预期抛出[JafI18NException]，但实际为[" + ex.getClass().getCanonicalName() + "]");
        }
        WafException tmp = (WafException) ex;
        assertEquals(errorCode.getCode(), tmp.getError().getCode());
    }

    /**
     * 校验异常是否为预期的错误码
     *
     * @param errorCode 预期的错误码
     * @param ex        实际异常
     */
    public static void verifyErrorCode(String errorCode, Exception ex) {
        if (ex == null) {
            fail("预期抛出[JafI18NException]，但实际未抛出");
        }
        if (!WafException.class.isAssignableFrom(ex.getClass())) {
            fail("预期抛出[JafI18NException]，但实际为[" + ex.getClass().getCanonicalName() + "]");
        }
        WafException tmp = (WafException) ex;
        assertEquals(errorCode, tmp.getError().getCode());
    }

    /**
     * 校验异常是否为预期的错误码
     *
     * @param expect 预期的错误码
     * @param actual 实际异常
     */
    public static void verifyErrorCode(WafException expect, Exception actual) {
        if (actual == null) {
            fail("预期抛出[JafI18NException]，但实际未抛出");
        }
        if (!WafException.class.isAssignableFrom(actual.getClass())) {
            fail("预期抛出[JafI18NException]，但实际为[" + actual.getClass().getCanonicalName() + "]");
        }
        WafException tmp = (WafException) actual;
        assertEquals(expect.getError().getCode(), tmp.getError().getCode());
    }

    /**
     * 校验异常是否为预期的错误信息
     *
     * @param expect 预期的错误信息
     * @param actual 实际异常
     */
    public static void verifyErrorMsg(String expect, Exception actual) {
        if (actual == null) {
            fail("预期抛出[JafI18NException]，但实际未抛出");
        }
        if (!WafException.class.isAssignableFrom(actual.getClass())) {
            fail("预期抛出[JafI18NException]，但实际为[" + actual.getClass().getCanonicalName() + "]");
        }
        WafException tmp = (WafException) actual;
        assertEquals(expect, tmp.getError().getMessage());
    }

    public static void initTenant() {
        initTenant(TENANT_ID);
    }

    public static void initTenant(long tenantId) {
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        tenant.setOrgId(String.valueOf(tenantId));
        TenantHandler.setTenant(tenant);
    }

    public static <T extends BaseTenantDomain<I>, I extends Serializable> T mockTenantDomain(Class<T> clazz, I id) {
        T t = mock(clazz);
        doReturn(id).when(t).getId();
        doReturn(TENANT_ID).when(t).getTenant();
        return t;
    }
}
