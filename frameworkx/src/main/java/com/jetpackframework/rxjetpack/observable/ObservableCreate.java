package com.jetpackframework.rxjetpack.observable;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.ObservableEmitter;
import com.jetpackframework.rxjetpack.ObservableOnSubscribe;
import com.jetpackframework.rxjetpack.Observer;

public class ObservableCreate<T> extends Observable<T> {
    private ObservableOnSubscribe<T> subscribe;

    public ObservableCreate(ObservableOnSubscribe<T> subscribe) {
        this.subscribe = subscribe;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        CreateEmitter<T> emitter = new CreateEmitter<>(observer);
        Dispose dispose = new DisposeImpl();
        observer.onSubscribe(dispose);
        try {
            if (!DisposableHelper.isDispose(dispose))
                subscribe.subscribe(emitter);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static final class CreateEmitter<T> implements ObservableEmitter<T>{

        private Observer<? super T> observer;

        public CreateEmitter(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T t) {
            observer.onNext(t);
        }

        @Override
        public void onError(Throwable throwable) {
            observer.onError(throwable);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
