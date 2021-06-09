package com.jetpackframework.rxjetpack;

public interface Emitter<T> {
    void onNext(T t);
    void onError(Throwable t);
    void onComplete();
}
