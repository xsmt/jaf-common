package cn.jcloud.jaf.common.cs.core;

import cn.jcloud.jaf.common.cs.domain.EnumEntry;
import cn.jcloud.jaf.common.cs.domain.EnumType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 基于xml文件的菜单解析器
 * 
 * @author Wei Han
 * 
 */
@Primary
@Component
public class EnumXmlParser implements EnumParser {

    public List<EnumType> parse(InputStream in) {
        List<EnumType> enumList = new ArrayList<>();
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            Element root = doc.getRootElement();

            // 读取菜单项
            List<Element> items = root.elements("enumType");
            for (Element e : items) {
                enumList.add(parseEnum(e));
            }
        } catch (DocumentException e) { // 菜单配置文件解析异常不应该出现，一旦出现则必须停止项目启动
            throw new RuntimeException(e);
        }
        return enumList;
    }

    private EnumType parseEnum(Element templateElement) {
        return EnumType.create()
                .code(templateElement.attributeValue("code"))
                .name(templateElement.attributeValue("name"))
                .editable(Boolean.parseBoolean(templateElement.attributeValue("editable", "true")))
                .values(parseParam(templateElement))
                .build();
    }

    private List<EnumEntry> parseParam(Element e) {
        List<Element> valueElements = e.elements("enumEntry");
        if (valueElements.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<EnumEntry> params = new ArrayList<>();
        for (Element valueElement : valueElements) {
            EnumEntry templateParam = EnumEntry.create()
                    .valueCode(valueElement.attributeValue("code"))
                    .valueName(valueElement.attributeValue("name"))
                    .language(valueElement.attributeValue("language", "zh"))
                    .build();

            params.add(templateParam);
        }
        return params;
    }
}
