package cn.jcloud.jaf.common.util;

import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by paul-nd on 2016/11/14.
 */
public class DecimalUtil {
    // 小数精度转换
    private static final DecimalFormat DF0 = new DecimalFormat("###");
    private static final DecimalFormat DF2 = new DecimalFormat("###.00");

    /**
     * double 转String, 最大精度为2
     *
     * @param d
     * @return
     */
    public static String smartString(Double d) {
        if (null == d) return null;
        BigDecimal b = new BigDecimal(d);
        Double value = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (value - value.longValue() == 0) {
            return DF0.format(d);
        }

        return DF2.format(d);
    }

    public static double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            throw JafI18NException.of(ErrorCode.REQUIRE_ARGUMENT);
        }
    }

    /**下划线转驼峰*/
    public static String lineToHump(String str){
        str = str.toLowerCase();
        Matcher matcher = Pattern.compile("_(\\w)").matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
