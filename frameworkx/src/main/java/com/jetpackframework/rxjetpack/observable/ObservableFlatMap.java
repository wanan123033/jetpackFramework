package com.jetpackframework.rxjetpack.observable;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Function;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;

public class ObservableFlatMap<T,R> extends Observable<R> {
    private final ObservableSource<T> observable;
    private final Function<T, ObservableSource<R>> function;

    public ObservableFlatMap(ObservableSource<T> observable, Function<T, ObservableSource<R>> function) {
        this.observable = observable;
        this.function = function;
    }

    @Override
    public void subscribeActual(Observer<? super R> observer) {
        observable.subscribeActual(new MapObserver<>(function,observer));
    }
    private static class MapObserver<T,R> implements Observer<T>, Dispose {

        private final Function<T, ObservableSource<R>> function;
        private final Observer<? super R> observer;
        private Dispose dispose;

        public MapObserver(Function<T, ObservableSource<R>> function, Observer<? super R> observer) {
            this.function = function;
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
            try {
                if (!isDispose())
                    function.apply(t).subscribeActual(observer);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
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
