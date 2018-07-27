package cn.jcloud.jaf.common.tenant.web;

import com.nd.social.common.query.Items;
import com.nd.social.common.query.ListParam;
import com.nd.social.common.security.BearerApi;
import com.nd.social.common.security.SuidRequired;
import com.nd.social.common.tenant.domain.Tenant;
import com.nd.social.common.tenant.service.TenantService;
import com.nd.social.common.tenant.vo.TenantPauseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 租户(服务)控制器
 * Created by Wei Han on 2016/1/27.
 */
@BearerApi(group = "services", suidRequired = SuidRequired.IGNORE)
@RestController
@RequestMapping({"/${version}/services", "/${version}/{bizType}/services"})
public class TenantController {

    @Autowired
    private TenantService tenantService;

    /**
     * 开通服务
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Tenant add(@RequestBody @Valid Tenant tenant) {
        return tenantService.add(tenant);
    }

    /**
     * 删除服务信息
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tenantService.delete(id);
    }

    /**
     * 修改服务信息
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Tenant update(@PathVariable Long id,
                         @RequestBody Map<String, Object> tenantMap) {
        return tenantService.update(id, tenantMap);
    }

    /**
     * 获取服务详情
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Tenant get(@PathVariable long id) {
        return tenantService.findStrictOne(id);
    }

    /**
     * 获取服务列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public Items<Tenant> list(ListParam<Tenant> listParam) {

        listParam.addDefaultSort(new Sort(Sort.Direction.DESC, "updateTime"));
        return tenantService.list(listParam);
    }

    /**
     * 判断服务是否存在
     */
    @RequestMapping(value = "/exist", method = RequestMethod.GET)
    public Map<String, Boolean> exist(@RequestParam("org_id") Long id) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("is_exist", tenantService.exists(id));
        return map;
    }

    /**
     * 暂停服务/开启服务
     */
    @RequestMapping(value = "/pause/{id}", method = RequestMethod.POST)
    public Tenant pause(@PathVariable long id,
                        @Valid @RequestBody TenantPauseVo vo) {

        return tenantService.pause(id, vo.getPauseFlag(), vo.getMsg());
    }
}
