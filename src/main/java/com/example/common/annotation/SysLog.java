package com.example.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @Author: cxx
 * @Date: 2019/1/1 17:28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";
}
