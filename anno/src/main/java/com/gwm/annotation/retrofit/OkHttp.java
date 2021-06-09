package com.gwm.annotation.retrofit;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface OkHttp {
    int connectTimeout() default 30;  //连接超时时长
    int readTimeout() default 30;  //读取超时时长
    int writeTimeout() default 30;
    boolean retryOnConnectionFailure() default true;  //错误重连
    int retry() default 3;  //失败重连次数
}
