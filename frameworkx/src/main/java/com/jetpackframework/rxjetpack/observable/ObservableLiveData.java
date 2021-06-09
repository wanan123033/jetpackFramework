package com.jetpackframework.rxjetpack.observable;

import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.jetpackframework.rxjetpack.DisposableHelper;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.DisposeImpl;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;

public class ObservableLiveData<T> extends Observable<T> {
    private LiveData<T> liveData;

    public ObservableLiveData(LiveData<T> liveData) {
        this.liveData = liveData;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        if (Looper.myLooper() == Looper.getMainLooper())
            liveData.observeForever(new ForeverObserver<T>(observer));
    }
    private static class ForeverObserver<T> implements androidx.lifecycle.Observer<T>, Dispose {

        private Observer<? super T> observer;
        private Dispose dispose;

        public ForeverObserver(Observer<? super T> observer) {
            this.observer = observer;
            dispose = new DisposeImpl();
        }

        @Override
        public void onChanged(T t) {
            try {
                observer.onSubscribe(dispose);
                if (!isDispose()){
                    observer.onNext(t);
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
