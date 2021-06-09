package com.jetpackframework.rxjetpack.observable;

import androidx.annotation.NonNull;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;
import com.jetpackframework.rxjetpack.thread.Schedule;

public class ObservableSubScribeOn<T> extends Observable<T> {
    private ObservableSource<T> observable;
    private Schedule schedule;

    public ObservableSubScribeOn(ObservableSource<T> observable, Schedule schedule) {
        this.observable = observable;
        this.schedule = schedule;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
//        Log.e("TAG","ObservableSubScribeOn subscribeActual");
        Schedule.Worker worker = schedule.createWorker();
        final SubScribeOnObserver<T> scribeOnObserver = new SubScribeOnObserver<>(observer);

        worker.schedule(new Runnable() {
            @Override
            public void run() {
                observable.subscribeActual(scribeOnObserver);
            }
        });
    }
    private static class SubScribeOnObserver<T> implements Observer<T>,Dispose{

        private Observer<? super T> observer;
        private Dispose dispose;

        public SubScribeOnObserver(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe(final Dispose dispose) {
//            Log.e("TAG","SubScribeOnObserver onSubscribe "+Thread.currentThread().getName());
            this.dispose = dispose;
            observer.onSubscribe(dispose);

        }

        @Override
        public void onNext(@NonNull final T t) {
//            Log.e("TAG","SubScribeOnObserver onNext "+Thread.currentThread().getName());
            if (!isDispose())
                observer.onNext(t);

        }

        @Override
        public void onError(@NonNull final Throwable e) {
//            Log.e("TAG","SubScribeOnObserver onError " +Thread.currentThread().getName());
            observer.onError(e);

        }

        @Override
        public void onComplete() {
//            Log.e("TAG","SubScribeOnObserver onComplete " +Thread.currentThread().getName());
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
