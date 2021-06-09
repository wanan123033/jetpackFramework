package com.gwm.annotation.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2017/11/3.
 * 例如: @Query("userId")
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Query {
    String value();
}
