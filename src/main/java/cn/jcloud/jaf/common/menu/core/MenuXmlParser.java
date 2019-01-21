package cn.jcloud.jaf.common.menu.core;

import java.io.InputStream;
import java.util.List;

import cn.jcloud.jaf.common.config.JafContext;
import cn.jcloud.jaf.common.menu.domain.Menu;
import cn.jcloud.jaf.common.menu.domain.MenuItem;
import cn.jcloud.jaf.common.menu.domain.MenuOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


/**
 * 基于xml文件的菜单解析器
 * 
 * @author Wei Han
 * 
 */
@Primary
@Component
public class MenuXmlParser implements MenuParser {

    public Menu parse(InputStream in) {
        Menu menu = new Menu();
        menu.setNeedUpdate(true);
        menu.setVersion(System.currentTimeMillis());

        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            Element root = doc.getRootElement();
            // 读取菜单基本配置
            Element config = root.element("config");
            if (config != null) {
                String version = config.elementTextTrim("version");
                if (version != null) {
                    menu.setVersion(NumberUtils.toLong(version));
                }
                Element actionConfig = config.element("action");
                if (actionConfig != null) {
                    String actionExtension = actionConfig.attributeValue("extension");
                    menu.setActionExtension(actionExtension);
                }
            }
            if (menu.getActionExtension() == null) { // 未显式设置action后缀，则从jaf配置获取
                menu.setActionExtension(JafContext.getMenuExt());
            }
            // 读取菜单项
            List<Element> items = root.elements("item");
            String parentId = "";
            for (Element e : items) {
                MenuItem item = parseItem(parentId, e);
                if (item != null) {
                    menu.addItem(item);
                }
            }
        } catch (DocumentException e) { // 菜单配置文件解析异常不应该出现，一旦出现则必须停止项目启动
            throw new RuntimeException(e);
        }
        return menu;
    }

    private MenuItem parseItem(String parentId, Element e) {
        MenuItem item = new MenuItem();
        item.setId(parentId.concat(e.attributeValue("id")));
        item.setSequence(e.attributeValue("sequence"));
        item.setCode(e.attributeValue("code"));
        item.setName(e.attributeValue("name"));
        item.setTitle(e.attributeValue("title"));
        item.setIcon(e.attributeValue("icon"));
        item.setAuthCode(e.attributeValue("auth"));
        item.setAction(e.attributeValue("action"));

        List<Element> itemElements = e.elements("item");
        if (itemElements.size() > 0) {
            for (Element childItemElement : itemElements) {
                MenuItem childItem = parseItem(item.getId(), childItemElement);
                if (childItem != null) {
                    item.addChild(childItem);
                }
            }
        }
        List<Element> operationElements = e.elements("operation");
        if (operationElements.size() > 0) {
            for (Element operationElement : operationElements) {
                MenuOperation operation = parseOperation(operationElement);
                if (operation != null) {
                    item.addOperation(operation);
                }
            }
        }
        return item;
    }

    private MenuOperation parseOperation(Element e) {
        MenuOperation operation = new MenuOperation();
        operation.setId(e.attributeValue("id"));
        operation.setSequence(e.attributeValue("sequence"));
        operation.setCode(e.attributeValue("code"));
        operation.setName(e.attributeValue("name"));
        operation.setTitle(e.attributeValue("title"));
        operation.setAuthCode(e.attributeValue("auth"));
        operation.setAction(e.attributeValue("action"));
        operation.setParams(e.attributeValue("params"));
        return operation;
    }
}
