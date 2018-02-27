package cn.jcloud.jaf.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author hasayaki(125473)
 *
 */
public class StringUtil {
	
    public static String getEncoding(String str){
    	
        if (str == null){
        	return null;
        }
        
    	String[] encodeAry = new String[]{"ISO-8859-1", "UTF-8", "GB2312", "GBK"};
    	for (String encode : encodeAry) {
    	    try {
				if (str.equals(new String(str.getBytes(encode), encode))) {
				    return encode;
				}
			} catch (UnsupportedEncodingException e) {
				
			}
    	} 
    	
		return null;
	}
	
	public static String convertTo(String str, String toEncoding){
	    
		if (str == null){
	    	return str;
	    }
	    
		String encode = getEncoding(str);
		if (encode != null && !StringUtils.equalsIgnoreCase(encode, toEncoding)) {
		    try {
				return new String(str.getBytes(encode), toEncoding);
			} catch (UnsupportedEncodingException e) {
				
			}
		}
		
		return str;
	}
}
