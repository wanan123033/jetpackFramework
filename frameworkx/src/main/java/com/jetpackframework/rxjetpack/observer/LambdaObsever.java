package com.jetpackframework.rxjetpack.observer;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.Observer;


public class LambdaObsever<T> implements Observer<T> {
    private Consumer<T> onSubscribe;
    private Consumer<T> onNext;
    private Consumer<? super Throwable> onError;
    private Consumer<T> onComplete;
    public LambdaObsever(Consumer<T> onSubscribe,Consumer<T> onNext,Consumer<? super Throwable> onError,Consumer<T> onComplete){
        this.onSubscribe = onSubscribe;
        this.onNext = onNext;
        this.onComplete = onComplete;
        this.onError = onError;
    }
    @Override
    public void onSubscribe(Dispose dispose) {
        try {
            if (onSubscribe != null)
            onSubscribe.accept(null);
        }catch (Exception e){
            onError(e);
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        try {
            if (onNext != null)
                onNext.accept(t);
        }catch (Exception e){
            onError(e);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (onError != null)
            onError.accept(e);
    }

    @Override
    public void onComplete() {
        try {
            if (onComplete != null)
                onComplete.accept(null);
        }catch (Exception e){
            onError(e);
        }
    }
}
