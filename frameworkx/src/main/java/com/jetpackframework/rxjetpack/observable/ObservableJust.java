package com.jetpackframework.rxjetpack.observable;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;

public class ObservableJust<T> extends Observable<T> {
    private T t;

    public ObservableJust(T t) {
        this.t = t;
    }

    public T get(){
        return t;
    }
    @Override
    public void subscribeActual(Observer<? super T> observer) {
        ScalarDisposable disposable = new ScalarDisposable<T>(observer,t);
        disposable.onSubscribe();
        disposable.run();
    }

    static final class ScalarDisposable<T> implements Runnable,Dispose{

        private T value;
        private Observer<? super T> observer;
        private Dispose dispose;

        public ScalarDisposable(Observer<? super T> observer, T t) {
            this.observer = observer;
            this.value = t;
            dispose = new DisposeImpl();
        }

        public void onSubscribe() {
            observer.onSubscribe(this.dispose);
        }

        @Override
        public void run() {
            try {
                if (!isDispose()) {
                    observer.onNext(value);
                    observer.onComplete();
                }
            }catch (Throwable e){
                observer.onError(e);
            }

        }

        @Override
        public void dispose() {
            DisposableHelper.dispose(dispose);
        }

        @Override
        public boolean isDispose() {
            return DisposableHelper.isDispose(dispose);
        }
    }
}
