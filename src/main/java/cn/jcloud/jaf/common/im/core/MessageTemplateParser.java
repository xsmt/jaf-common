package cn.jcloud.jaf.common.im.core;

import cn.jcloud.jaf.common.im.domain.MessageTemplate;
import cn.jcloud.jaf.common.menu.domain.Menu;

import java.io.InputStream;
import java.util.List;

/**
 * 菜单解析器
 * 
 * @author Wei Han
 * 
 */
public interface MessageTemplateParser {

    /**
     * 从指定文件资源输入流中读取配置内容，解析生成菜单
     * 
     * @param in
     *            菜单配置文件资源输入流
     * @return 菜单
     */
    List<MessageTemplate> parse(InputStream in);

}