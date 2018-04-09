package com.wang.project.annations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为类的每个属性值注解名字，没有注解的将不会写入excel
 * @author wyw
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldToExcel {
    /**
     * 标注列名
     * 
     * @return
     * @since 1.0
     */
    public String name() default "";

    /**
     * 标注列所在位置
     * 
     * @return
     * @since 1.0
     */
    public int column() default 0;

    /**
     * 标注偏移量
     * 
     * 
     * @return
     * @since 2.0
     */
    public int offset() default 0;

    /**
     * 指定值
     * 
     * @return
     * @since 2.1
     */
    public String[] value() default {};

    /**
     * 映射值
     * 
     * @return
     * @since 2.1
     */
    public String[] mapping() default {};

    /**
     * 替换值
     * 
     * @return
     * @since 2.1
     */
    public String[] replace() default {};

    /**
     * 指定要替换第几次出现的指定值default 1
     * 
     * @return
     * @since 2.1
     */
    public int[] index() default { 1 };

    /**
     * 是否全部替换指定值
     * 
     * @return
     * @since 2.2
     */
    public boolean replaceAll() default false;

    /**
     * 前缀
     * 
     * @return
     * @since 2.1
     */
    public String prefix() default "";

    /**
     * 后缀
     * 
     * @return
     * @since 2.1
     */
    public String suffix() default "";
}
