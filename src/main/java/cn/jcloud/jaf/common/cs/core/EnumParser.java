package cn.jcloud.jaf.common.cs.core;

import cn.jcloud.jaf.common.cs.domain.EnumType;

import java.io.InputStream;
import java.util.List;

/**
 * 枚举解析器
 * 
 * @author Wei Han
 * 
 */
public interface EnumParser {

    /**
     * 从指定文件资源输入流中读取配置内容，解析生成菜单
     * 
     * @param in
     *            菜单配置文件资源输入流
     * @return 菜单
     */
    List<EnumType> parse(InputStream in);

}