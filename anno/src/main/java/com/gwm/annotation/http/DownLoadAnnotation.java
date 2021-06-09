package com.gwm.annotation.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2019/1/2.
 * 下载文件时会用到
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface DownLoadAnnotation {
    String targetPath();
    String targetFileName();
}
