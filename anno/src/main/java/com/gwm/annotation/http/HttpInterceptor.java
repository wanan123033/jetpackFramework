package com.gwm.annotation.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2019/1/10.
 * Http������ע��
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface HttpInterceptor {
}
