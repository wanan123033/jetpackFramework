package com.jetpackframework.rxjetpack.observable;

import androidx.annotation.NonNull;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;

import java.util.Iterator;

public class ObservableFrom<T> extends Observable<T> {


    private Iterator<T> iterator;

    public ObservableFrom(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public void subscribeActual(@NonNull Observer<? super T> observer) {
        FromObserver<T> fromObserver = new FromObserver<>(observer,iterator);
        fromObserver.run();
    }
    private static class FromObserver<T> implements Runnable{

        private Iterator<T> iterator;
        private Observer<? super T> observer;
        private Dispose dispose;

        public FromObserver(@NonNull Observer<? super T> observer, @NonNull Iterator<T> iterator) {
            this.observer = observer;
            this.iterator = iterator;
            dispose = new DisposeImpl();
        }

        @Override
        public void run() {
            try {
                observer.onSubscribe(dispose);
                while (iterator.hasNext()&& !DisposableHelper.isDispose(dispose)){
                    observer.onNext(iterator.next());
                }
            }catch (Throwable e){
                observer.onError(e);
            }
            if (!DisposableHelper.isDispose(dispose))
                observer.onComplete();
        }
    }
}
