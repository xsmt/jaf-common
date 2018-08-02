package cn.jcloud.jaf.common.whitelist.web;

import cn.jcloud.jaf.common.handler.TenantHandler;
import cn.jcloud.jaf.common.query.Items;
import cn.jcloud.jaf.common.query.ListParam;
import cn.jcloud.jaf.common.whitelist.domain.GuestWhiteList;
import cn.jcloud.jaf.common.whitelist.service.GuestWhiteListService;
import cn.jcloud.jaf.common.whitelist.vo.BatchAddVo;
import cn.jcloud.jaf.common.whitelist.vo.BatchDeleteVo;
import cn.jcloud.jaf.common.whitelist.vo.GuestMode;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 白名单 Controller
 * 本接口重构至原代码，与微博接口相一致或有少量改动
 * Created by Wei Han on 2016/3/4.
 */
@RestController
@RequestMapping("/${version}/guests")
public class GuestWhiteListController {

    @Autowired
    private GuestWhiteListService guestWhiteListService;

    /**
     * 设置访客模式
     */
    @RequestMapping(value = "/mode", method = RequestMethod.PUT)
    public GuestMode updateGuestMode(
            @Valid @RequestBody GuestMode guestMode) {

        guestWhiteListService.updateGuestMode(TenantHandler.getStrictTenant(), guestMode.getFlag());
        guestMode.setOrgId(TenantHandler.getStrictTenant());
        return guestMode;
    }

    /**
     * 获取当前访客模式
     */
    @RequestMapping(value = "/mode", method = RequestMethod.GET)
    public GuestMode getGuestMode() {

        long orgId = TenantHandler.getStrictTenant();
        boolean guestMode = guestWhiteListService.getGuestMode(orgId);
        return new GuestMode(orgId, guestMode);
    }

    /**
     * 获取访客白名单列表
     */
    @RequestMapping(value = "/whitelist", method = RequestMethod.GET)
    public Items<GuestWhiteList> getGuestWhiteList(ListParam<GuestWhiteList> listParam) {
        return guestWhiteListService.list(listParam);
    }

    /**
     * 获取访客白名单
     */
    @RequestMapping(value = "/whitelist/{id}", method = RequestMethod.GET)
    public GuestWhiteList getGuestWhite(@PathVariable Long id) {

        return guestWhiteListService.findStrictOne(id);
    }

    /**
     * 获取可选访客白名单列表
     */
    @JsonView(GuestWhiteList.Optional.class)
    @RequestMapping(value = "/whitelist/options", method = RequestMethod.GET)
    public Items<GuestWhiteList> getOptionalGuestWhite() {

        return guestWhiteListService.getOptionalGuestWhiteLists();
    }

    /**
     * 批量添加访客白名单 [POST] /guests/whitelist
     */
    @RequestMapping(value = "/whitelist", method = RequestMethod.POST)
    public Items<GuestWhiteList> batchAddGuestWhiteList(@RequestBody BatchAddVo batchAddVo) {

        List<GuestWhiteList> list = guestWhiteListService.batchAdd(batchAddVo.getAccesses());
        return Items.of(list);
    }

    /**
     * 批量删除访客白名单 (原接口定义有返回，不全理，去除掉)
     */
    @RequestMapping(value = "/whitelist", method = RequestMethod.PUT)
    public List<GuestWhiteList> batchDeleteGuestWhiteList(@Valid @RequestBody BatchDeleteVo vo) {
        return guestWhiteListService.batchDelete(vo.getIds());
    }
}
