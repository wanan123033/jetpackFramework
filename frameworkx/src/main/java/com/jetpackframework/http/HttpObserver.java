package com.jetpackframework.http;

import okhttp3.Headers;

public interface HttpObserver<T> {

    void onError(Exception e, int id);

    void onNext(T response, int id);

    void onHeader(Headers headers);
}