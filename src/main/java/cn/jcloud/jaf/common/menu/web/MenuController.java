package cn.jcloud.jaf.common.menu.web;

import cn.jcloud.jaf.common.menu.core.MenuSupportCondition;
import cn.jcloud.jaf.common.menu.domain.Menu;
import cn.jcloud.jaf.common.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${version}/menu")
@Conditional(MenuSupportCondition.class)
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "{code}", method = RequestMethod.GET)
    public Menu getMenu(@PathVariable("code") String code, @RequestParam(value = "version", required = false) Long version) {
        return menuService.getMenu(code, version);
    }

    @RequestMapping(value = "{code}/user", method = RequestMethod.GET)
    public Menu getUserMenu(@PathVariable("code") String code, @RequestParam(value = "version", required = false) Long version) {
        return menuService.getUserMenu(code, version);
    }
}
