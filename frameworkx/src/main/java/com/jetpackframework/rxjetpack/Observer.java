package com.jetpackframework.rxjetpack;

public interface Observer<T> extends Emitter<T>{
    void onSubscribe(Dispose dispose);
}
