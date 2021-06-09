package com.jetpackframework.rxjetpack.observable;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;
import com.jetpackframework.rxjetpack.thread.Schedule;

import java.util.concurrent.TimeUnit;

public class ObservableTimer<T> extends Observable<T> {
    private TimeUnit unit;
    private Schedule scheduler;
    private long delay;

    public ObservableTimer(long delay, TimeUnit unit, Schedule scheduler) {
        this.delay = delay;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(final Observer<? super T> observer) {
        Schedule.Worker worker = scheduler.createWorker();
        worker.scheduleTimer(new TimerRunable<T>(observer),delay,unit);
    }
    private static class TimerRunable<T> implements Runnable{
        private Observer<? super T> observer;
        private Dispose dispose;

        public TimerRunable(Observer<? super T> observer){
            this.observer = observer;
            dispose = new DisposeImpl();
        }

        @Override
        public void run() {
            try {
                observer.onSubscribe(dispose);
                if (!DisposableHelper.isDispose(dispose)) {
                    observer.onNext(null);
                }
            }catch (Throwable e){
                observer.onError(e);
            }
            if (!DisposableHelper.isDispose(dispose)){
                observer.onComplete();
            }
        }
    }
}
