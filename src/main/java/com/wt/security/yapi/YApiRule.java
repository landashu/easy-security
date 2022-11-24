package com.wt.security.yapi;


import java.lang.annotation.*;


/**
 * @author big uncle
 * @date 2020/7/13 11:32
 **/
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface YApiRule {

    boolean required() default false;

    boolean hide() default false;
}
