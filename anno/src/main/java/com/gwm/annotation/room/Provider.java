package com.gwm.annotation.room;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Provider {
    String uri();
    ParamterType[] param() default {};
    String daoClazzName();
    @interface ParamterType{
        String name();
        Class type();
    }
}
