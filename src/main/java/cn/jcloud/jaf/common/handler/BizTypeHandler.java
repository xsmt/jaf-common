package cn.jcloud.jaf.common.handler;

import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;

/**
 * @author hasayaki(125473)
 *
 */
public class BizTypeHandler {

	private static ThreadLocal<String> bizTypeThreadLocal = new NamedThreadLocal<>("bizType");
	
	public static String getStrict(){
		
		String bizType = bizTypeThreadLocal.get();
		
		if(StringUtils.isBlank(bizType)){
			throw WafI18NException.of(ErrorCode.MISSING_BIZ_TYPE);
		}
		
		return bizType;
	}
	
	public static String get(){
		
		return bizTypeThreadLocal.get();
	}
	
	public static void set(String bizType){
		
		bizTypeThreadLocal.set(bizType);
	}
	
	public static void clear(){
		
		bizTypeThreadLocal.remove();
	}
}
