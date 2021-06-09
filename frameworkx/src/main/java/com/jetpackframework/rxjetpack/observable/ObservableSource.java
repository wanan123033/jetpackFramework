package com.jetpackframework.rxjetpack.observable;

import com.jetpackframework.rxjetpack.Observer;

public interface ObservableSource<T> {
    void subscribeActual(Observer<? super T> observer);
}
