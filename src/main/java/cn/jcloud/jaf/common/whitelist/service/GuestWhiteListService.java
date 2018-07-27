package cn.jcloud.jaf.common.whitelist.service;

import com.nd.social.common.base.domain.Module;
import com.nd.social.common.base.service.BaseTenantService;
import com.nd.social.common.constant.CommonModules;
import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import com.nd.social.common.handler.TenantHandler;
import com.nd.social.common.query.Items;
import com.nd.social.common.security.GuestHandler;
import com.nd.social.common.tenant.service.TenantService;
import com.nd.social.common.tenant.service.TenantSupport;
import com.nd.social.common.util.IdUtils;
import com.nd.social.common.util.ValidatorUtil;
import com.nd.social.common.whitelist.domain.GuestWhiteList;
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
    public Class<?>[] getEntities() {
        return new Class[]{GuestWhiteList.class};
    }

    public void updateGuestMode(long tenantId, boolean guestMode) {
        tenantService.updateGuestMode(tenantId, guestMode);
        VisitorService.cleanCustomizableWhiteApiCache(tenantId);
    }

    public boolean getGuestMode(long tenantId) {
        return tenantService.findStrictOne(tenantId).isGuestMode();
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
            throw WafI18NException.of("白名单列表为空", ErrorCode.INVALID_ARGUMENT);
        }

        List<String> existedAccesses = existedAccesses();
        Map<String, String> optionalWhiteMap = GuestHandler.getOptionalWhiteMap();
        List<GuestWhiteList> result = new ArrayList<>(accesses.size());
        for (String access : accesses) {
            if (!optionalWhiteMap.containsKey(access)) {
                throw WafI18NException.of("accesses参数存在无效的值", ErrorCode.INVALID_ARGUMENT);
            }
            
            if(existedAccesses.contains(access)){
            	continue;
            }

            GuestWhiteList guestWhiteList = new GuestWhiteList();
            guestWhiteList.setAccess(access);
            guestWhiteList.setDescription(optionalWhiteMap.get(access));

            ValidatorUtil.validateAndThrow(guestWhiteList);

            add(guestWhiteList);

            result.add(guestWhiteList);
        }
        VisitorService.cleanCustomizableWhiteApiCache(TenantHandler.getStrictTenant());
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
        VisitorService.cleanCustomizableWhiteApiCache(TenantHandler.getStrictTenant());
        return result;
    }

    public List<String> findAllAccess() {
        long tenantId = TenantHandler.getStrictTenant();
        if (tenantService.isOpenGuestMode(tenantId)) {
            return existedAccesses();
        } else {
            return Collections.emptyList();
        }
    }

    public List<String> existedAccesses(){
    	
    	List<GuestWhiteList> guestWhiteLists = findAll();
        List<String> accesses = new ArrayList<>(guestWhiteLists.size());
        for (GuestWhiteList guestWhiteList : guestWhiteLists) {
            accesses.add(guestWhiteList.getAccess());
        }
        return accesses;
    }
}
