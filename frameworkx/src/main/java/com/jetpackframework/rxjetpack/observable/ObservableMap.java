package com.jetpackframework.rxjetpack.observable;

import androidx.annotation.NonNull;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Function;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;

public class ObservableMap<T,R> extends Observable<R> {
    private Function<T, R> function;
    private ObservableSource<T> source;

    public ObservableMap(ObservableSource<T> source, Function<T, R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    public void subscribeActual(Observer<? super R> observer) {
        MapObserver<T, R> mapObserver = new MapObserver<>(observer, function);
        mapObserver.onSubscribe(mapObserver.dispose);
        source.subscribeActual(new MapObserver<T, R>(observer, function));
    }
    public static class MapObserver<T,R> implements Observer<T>,Dispose{

        private Function<T, R> function;
        private Observer<? super R> observer;
        private Dispose dispose;

        public MapObserver(Observer<? super R> observer, Function<T, R> function) {
            this.observer = observer;
            this.function = function;
            this.dispose = new DisposeImpl();
        }

        @Override
        public void onSubscribe(Dispose dispose) {
            observer.onSubscribe(dispose);
        }

        @Override
        public void onNext(@NonNull T t) {
            try {
                R r = function.apply(t);
                if (!isDispose())
                    observer.onNext(r);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                onError(throwable);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
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
