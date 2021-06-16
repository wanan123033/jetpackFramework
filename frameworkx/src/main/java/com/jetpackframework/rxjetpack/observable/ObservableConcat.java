package com.jetpackframework.rxjetpack.observable;

import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;

public class ObservableConcat<T> extends Observable<T> {
    private Observable<T>[] observable;

    public ObservableConcat(Observable<T>... observable) {
        this.observable = observable;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        for (Observable observable : this.observable){
            observable.subscribeActual(observer);
        }
    }
}
