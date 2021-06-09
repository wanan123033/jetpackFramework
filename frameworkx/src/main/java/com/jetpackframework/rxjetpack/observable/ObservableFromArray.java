package com.jetpackframework.rxjetpack.observable;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;

public class ObservableFromArray<T> extends Observable<T> {
    private T[] ts;

    public ObservableFromArray(T[] ts) {
        this.ts = ts;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        FromObserver<T> fromObserver = new FromObserver<>(observer,ts);
        observer.onSubscribe(fromObserver);
        fromObserver.run();
    }
    private static class FromObserver<T> implements Runnable,Dispose{
        private Dispose dispose;
        private Observer<? super T> observer;
        private T[] ts;

        public FromObserver(Observer<? super T> observer, T[] ts) {
            this.observer = observer;
            this.ts = ts;
            dispose = new DisposeImpl();
        }

        @Override
        public void run() {
            try {
                for (int i = 0 ; i < ts.length && !isDispose() ; i++){
                    observer.onNext(ts[i]);
                }
            }catch (Throwable e){
                observer.onError(e);
            }
            if (!isDispose())
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
