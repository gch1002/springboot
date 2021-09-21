package com.crud_restful.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  //注解用的位置   用在方法上
@Retention(RetentionPolicy.RUNTIME)   //注解的生命周期   运行期间
public @interface LoggerManage {
    public String logDescription();
}
