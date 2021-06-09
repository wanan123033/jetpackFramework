package com.jetpackframework.rxjetpack;

public interface Function<T,R> {

    R apply(T t) throws Throwable;
}
