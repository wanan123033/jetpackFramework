package com.jetpackframework.rxjetpack;

import androidx.annotation.NonNull;

public interface ObservableOnSubscribe<T> {
    void subscribe(@NonNull ObservableEmitter<T> emitter) throws Throwable;
}
