package com.jetpackframework.http;

public interface HttpObserver<T> {

    void onError(Exception e, int id);

    void onNext(T response, int id);
}