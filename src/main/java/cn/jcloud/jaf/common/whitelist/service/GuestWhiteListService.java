package cn.jcloud.jaf.common.whitelist.service;

import cn.jcloud.jaf.common.base.domain.Module;
import cn.jcloud.jaf.common.base.service.BaseTenantService;
import cn.jcloud.jaf.common.constant.CommonModules;
import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.handler.TenantHandler;
import cn.jcloud.jaf.common.query.Items;
import cn.jcloud.jaf.common.security.GuestHandler;
import cn.jcloud.jaf.common.tenant.service.TenantService;
import cn.jcloud.jaf.common.tenant.service.TenantSupport;
import cn.jcloud.jaf.common.util.IdUtils;
import cn.jcloud.jaf.common.util.ValidatorUtil;
import cn.jcloud.jaf.common.whitelist.domain.GuestWhiteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 白名单 Service
 * Created by Wei Han on 2016/3/4.
 */
@Service
public class GuestWhiteListService extends BaseTenantService<GuestWhiteList, Long> implements TenantSupport {

    @Autowired
    private TenantService tenantService;

    @Override
    protected Module module() {
        return CommonModules.GUEST_WHITE_LIST;
    }

    @Override
    public Class[] getEntities() {
        return new Class[]{GuestWhiteList.class};
    }

    public void updateGuestMode(long orgId, boolean guestMode) {
        tenantService.updateGuestMode(orgId, guestMode);
        VisitorService.cleanWhiteListCache(orgId);
    }

    public boolean getGuestMode(long orgId) {
        return tenantService.findStrictOne(orgId).isGuestMode();
    }

    public Items<GuestWhiteList> getOptionalGuestWhiteLists() {
        List<GuestWhiteList> items = new ArrayList<>();

        Map<String, String> optionalWhiteMap = GuestHandler.getOptionalWhiteMap();
        for (Map.Entry<String, String> entry : optionalWhiteMap.entrySet()) {
            items.add(new GuestWhiteList(entry.getKey(), entry.getValue()));
        }

        return Items.of(items);
    }

    public List<GuestWhiteList> batchAdd(List<String> accesses) {
        if (CollectionUtils.isEmpty(accesses)) {
            throw JafI18NException.of("白名单列表为空", ErrorCode.INVALID_ARGUMENT);
        }

        Map<String, String> optionalWhiteMap = GuestHandler.getOptionalWhiteMap();
        List<GuestWhiteList> result = new ArrayList<>(accesses.size());
        for (String access : accesses) {
            if (!optionalWhiteMap.containsKey(access)) {
                throw JafI18NException.of("access参数存在无效的值", ErrorCode.INVALID_ARGUMENT);
            }

            GuestWhiteList guestWhiteList = new GuestWhiteList();
            guestWhiteList.setAccess(access);
            guestWhiteList.setDescription(optionalWhiteMap.get(access));

            ValidatorUtil.validateAndThrow(guestWhiteList);

            add(guestWhiteList);

            result.add(guestWhiteList);
        }
        VisitorService.cleanWhiteListCache(TenantHandler.getStrictTenant());
        return result;
    }


    @Override
    protected void beforeAdd(GuestWhiteList guestWhiteList) {
        guestWhiteList.setId(IdUtils.dummyDistributedLongId());
    }

    public List<GuestWhiteList> batchDelete(List<Long> ids) {
        List<GuestWhiteList> result = new ArrayList<>(ids.size());
        for (Long id : ids) {
            delete(id);
        }
        VisitorService.cleanWhiteListCache(TenantHandler.getStrictTenant());
        return result;
    }

    public List<String> findAllAccess() {
        long orgId = TenantHandler.getStrictTenant();
        if (tenantService.isOpenGuestMode(orgId)) {
            List<GuestWhiteList> guestWhiteLists = findAll();
            List<String> accesses = new ArrayList<>(guestWhiteLists.size());
            for (GuestWhiteList guestWhiteList : guestWhiteLists) {
                accesses.add(guestWhiteList.getAccess());
            }
            return accesses;
        } else {
            return Collections.emptyList();
        }
    }

}
