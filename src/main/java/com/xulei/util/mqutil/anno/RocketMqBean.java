package com.xulei.util.mqutil.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: xulei30
 * @Date: 2021/11/17 20:25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RocketMqBean {
    String groupName() default "";

    String topic() default "";

    String tags() default "";

    String targetAddress()
            default "";
}
