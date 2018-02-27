package cn.jcloud.jaf.common.query;

import java.lang.annotation.*;

/**
 * 用于ListParam自定义字段映射
 * 如下会将demoName映射至name字段
 *
 * @FieldsMapping( fields={"demoName"},mappings={"name"})
 * public class Demo{
 * private String name;
 * }
 * fields与mappings一一对应
 * Created by Wei Han on 2016/3/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldsMapping {

    /**
     * 需要进行映射的字段名
     */
    String[] fields();

    /**
     * 映射后的字段名，与fields一一对应
     */
    String[] mappings();

    /**
     * 非必填，指定映射字段的类型（一般不需要使用）,
     * 与fields一一对应，如果不特地指定，可以使用{null,null}代替
     */
    Class[] types() default {};
}
