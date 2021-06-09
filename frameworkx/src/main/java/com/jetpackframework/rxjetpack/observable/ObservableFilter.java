package com.jetpackframework.rxjetpack.observable;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;
import com.jetpackframework.rxjetpack.Predicate;

public class ObservableFilter<T> extends Observable<T> {
    private Predicate<T> predicate;
    private ObservableSource<T> source;

    public ObservableFilter(ObservableSource<T> source, Predicate<T> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        source.subscribeActual(new FilterObserver<T>(observer,predicate));
    }

    private static class FilterObserver<T> implements Observer<T>, Dispose{

        private Observer<? super T> observer;
        private Predicate<T> predicate;
        private Dispose dispose;

        public FilterObserver(Observer<? super T> observer, Predicate<T> predicate) {
            this.observer = observer;
            this.predicate = predicate;
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
            try {
                if (!isDispose() && predicate.test(t)){
                    observer.onNext(t);
                }
            } catch (Throwable throwable) {
                onError(throwable);
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
