package com.jetpackframework.rxjetpack.observable;

import androidx.core.util.Consumer;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;

public class ObservableDoOnNext<T> extends Observable<T> {
    private Consumer<T> onNext;
    private ObservableSource<T> observable;

    public ObservableDoOnNext(ObservableSource<T> observable, Consumer<T> onNext) {
        this.observable = observable;
        this.onNext = onNext;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        OnNextObserver onNextObserver = new OnNextObserver(onNext,observer);
        observable.subscribeActual(onNextObserver);
    }

    private static class OnNextObserver<T> implements Observer<T>, Dispose{

        private Observer<? super T> observer;
        private Consumer<T> onNext;
        private Dispose dispose;

        public OnNextObserver(Consumer<T> onNext, Observer<? super T> observer) {
            this.onNext = onNext;
            this.observer = observer;
            dispose = new DisposeImpl();
        }

        @Override
        public void dispose() {
            DisposableHelper.dispose(dispose);
        }

        @Override
        public boolean isDispose() {
            return DisposableHelper.isDispose(dispose);
        }

        @Override
        public void onSubscribe(Dispose dispose) {
            observer.onSubscribe(dispose);
        }

        @Override
        public void onNext(T t) {
            if (!isDispose()) {
                onNext.accept(t);
                observer.onNext(t);
            }
        }

        @Override
        public void onError(Throwable t) {
            observer.onError(t);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
