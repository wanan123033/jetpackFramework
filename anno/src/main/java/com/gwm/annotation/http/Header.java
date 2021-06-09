package com.gwm.annotation.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *  @Header({"key:value","key:value"})
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Header {
    String[] value();
}
