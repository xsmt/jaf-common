package cn.jcloud.jaf.common.util;

import com.nd.social.common.handler.BizTypeHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author hasayaki(125473)
 *
 */
public class RequestUtil {
	
	public static void dealBizType(HttpServletRequest request){
		
		if (StringUtils.isNotBlank(BizTypeHandler.get())) {
			return;
		}
		
		String bizType = request.getParameter("biz_type");
		
		if (StringUtils.isBlank(bizType)) {
			Map<?, ?> pathVariables = (Map<?, ?>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			if (pathVariables.get("bizType") != null) {
				bizType = pathVariables.get("bizType").toString();
			}
		}
		
		BizTypeHandler.set(bizType);
	}
}
