package cn.jcloud.jaf.common.im.core;

import cn.jcloud.jaf.common.im.domain.MessageTemplate;
import cn.jcloud.jaf.common.im.domain.MessageTemplateParam;
import cn.jcloud.jaf.common.im.enums.MessageTemplateParamTypeEnum;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
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
public class MessageTemplateXmlParser implements MessageTemplateParser {

    public List<MessageTemplate> parse(InputStream in) {
        List<MessageTemplate> templateList = new ArrayList<>();
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            Element root = doc.getRootElement();

            // 读取菜单项
            List<Element> items = root.elements("type");
            for (Element e : items) {
                templateList.add(parseTemplate(e));
            }
        } catch (DocumentException e) { // 菜单配置文件解析异常不应该出现，一旦出现则必须停止项目启动
            throw new RuntimeException(e);
        }
        return templateList;
    }

    private MessageTemplate parseTemplate(Element templateElement) {
        return MessageTemplate.create()
                .systemCode(templateElement.attributeValue("system"))
                .action(templateElement.attributeValue("action"))
                .title(templateElement.attributeValue("title"))
                .content(templateElement.elementText("content"))
                .authCode(templateElement.attributeValue("auth"))
                .hrefUrl(templateElement.elementText("href"))
                .hrefTitle(templateElement.elementText("href_title"))
                .subscribeAble(Boolean.parseBoolean(StringUtils.defaultString(templateElement.attributeValue("subscribe", "false"))))
                .params(parseParam(templateElement))
                .build();
    }

    private List<MessageTemplateParam> parseParam(Element e) {
        List<Element> paramElements = e.elements("param");
        if (paramElements.size() <= 0) {
            return Collections.EMPTY_LIST;
        }

        List<MessageTemplateParam> params = new ArrayList<>();
        for (Element paramElement : paramElements) {
            MessageTemplateParam templateParam = MessageTemplateParam.create()
                    .paramKey(paramElement.attributeValue("key"))
                    .paramName(paramElement.attributeValue("name"))
                    .paramType(EnumUtils.getEnum(MessageTemplateParamTypeEnum.class, paramElement.attributeValue("type", "STRING")))
                    .isMultiLine(Boolean.parseBoolean(paramElement.attributeValue("", "")))
                    .build();

            params.add(templateParam);
        }

        return params;
    }
}
