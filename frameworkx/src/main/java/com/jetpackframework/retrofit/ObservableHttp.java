package com.jetpackframework.retrofit;

import androidx.annotation.NonNull;

import com.jetpackframework.http.HttpObserver;
import com.jetpackframework.http.HttpTask;
import com.jetpackframework.http.HttpUtil;
import com.jetpackframework.rxjetpack.Dispose;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.Observer;
import com.jetpackframework.rxjetpack.RxJetpack;

import java.util.Objects;

import okhttp3.WebSocketListener;

public class ObservableHttp<T> extends Observable<T> {
    private final HttpTask<T> item;

    public static <T> Observable<T> http(HttpTask<T> item) {
        Objects.requireNonNull(item);
        return RxJetpack.onAssembly(new ObservableHttp<>(item));
    }
    public ObservableHttp(HttpTask<T> item) {
        this.item = item;
    }
    public void httpObserver(HttpObserver<T> httpObserver){
        item.observer = httpObserver;
    }
    public void webSocketListener(WebSocketListener webSocketListener){
        item.webSocketListener = webSocketListener;
    }
    @Override
    public void subscribeActual(@NonNull Observer<? super T> observer) {
//        Log.e("TAG","ObservableHttp subscribeActual");
        FirstObservable firstObservable = new FirstObservable(observer,item);
        firstObservable.onSubscribe(null);
        firstObservable.run();
    }

    private static class FirstObservable<T> implements Observer<HttpTask<T>>{

        private HttpTask<T> item;
        private Observer<? super T> observer;

        public FirstObservable(Observer<? super T> observer, HttpTask<T> item) {
            this.observer = observer;
            this.item = item;
        }

        @Override
        public void onSubscribe(Dispose dispose) {
//            Log.e("TAG","FirstObservable onSubscribe " +Thread.currentThread().getName());
            observer.onSubscribe(dispose);
        }

        @Override
        public void onNext(@NonNull HttpTask<T> t) {
//            Log.e("TAG","FirstObservable onNext " +Thread.currentThread().getName());
            HttpUtil.getInstance().sendHttp(t);
            observer.onNext(null);
        }

        @Override
        public void onError(@NonNull Throwable e) {
//            Log.e("TAG","FirstObservable onError " +Thread.currentThread().getName());
            observer.onError(e);
        }

        @Override
        public void onComplete() {
//            Log.e("TAG","FirstObservable onComplete " +Thread.currentThread().getName());
            observer.onComplete();
        }

        public void run() {
            try {
                onNext(item);
                onComplete();
            }catch (Exception e){
                onError(e);
            }
        }
    }
}
