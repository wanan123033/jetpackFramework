package com.jetpackframework.rxjetpack.observable;

import androidx.annotation.NonNull;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;
import com.jetpackframework.rxjetpack.thread.Schedule;

public class ObservableObserveOn<T> extends Observable<T> {
    private ObservableSource<T> observable;
    private Schedule schedule;

    public ObservableObserveOn(ObservableSource<T> observable, Schedule schedule) {
        this.observable = observable;
        this.schedule = schedule;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
//        Log.e("TAG","ObservableObserveOn subscribeActual");
        final ObserveOnObserver<T> observe = new ObserveOnObserver<>(this, observer);
        Schedule.Worker worker = schedule.createWorker();
        observe.onSubscribe(observe.dispose);
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                observable.subscribeActual(observe);
            }
        });

    }

    private static class ObserveOnObserver<T> implements Observer<T>{

        private Observer<? super T> observer;
        private ObservableObserveOn<T> observableObserveOn;
        private Dispose dispose;

        public ObserveOnObserver(ObservableObserveOn<T> observableObserveOn, Observer<? super T> observer) {
            this.observableObserveOn = observableObserveOn;
            this.observer = observer;
            dispose = new DisposeImpl();
        }

        @Override
        public void onSubscribe(Dispose dispose) {
//            Log.e("TAG","ObserveOnObserver onSubscribe " + Thread.currentThread().getName());
            observer.onSubscribe(dispose);
        }

        @Override
        public void onNext(@NonNull T t) {
//            Log.e("TAG","ObserveOnObserver onNext " +Thread.currentThread().getName());
            if (!DisposableHelper.isDispose(dispose))
                observer.onNext(t);
        }

        @Override
        public void onError(@NonNull Throwable e) {
//            Log.e("TAG","ObserveOnObserver onError "+Thread.currentThread().getName());
            observer.onError(e);
        }

        @Override
        public void onComplete() {
//            Log.e("TAG","ObserveOnObserver onComplete "+Thread.currentThread().getName());
            if (!DisposableHelper.isDispose(dispose))
                observer.onComplete();
        }
    }
}
