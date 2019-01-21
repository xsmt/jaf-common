package cn.jcloud.jaf.common.menu.core;

import cn.jcloud.jaf.common.menu.domain.Menu;

import java.io.InputStream;

/**
 * 菜单解析器
 * 
 * @author Wei Han
 * 
 */
public interface MenuParser {

    /**
     * 从指定文件资源输入流中读取配置内容，解析生成菜单
     * 
     * @param in
     *            菜单配置文件资源输入流
     * @return 菜单
     */
    public Menu parse(InputStream in);

}