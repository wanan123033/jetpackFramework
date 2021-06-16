package com.jetpackframework.rxjetpack;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;

import com.commonsware.cwac.saferoom.SafeHelperFactory;
import com.jetpackframework.SparseArray;
import com.jetpackframework.rxjetpack.observable.ObservableConcat;
import com.jetpackframework.rxjetpack.observable.ObservableCreate;
import com.jetpackframework.rxjetpack.observable.ObservableDoOnNext;
import com.jetpackframework.rxjetpack.observable.ObservableFilter;
import com.jetpackframework.rxjetpack.observable.ObservableFlatMap;
import com.jetpackframework.rxjetpack.observable.ObservableFrom;
import com.jetpackframework.rxjetpack.observable.ObservableFromArray;
import com.jetpackframework.rxjetpack.observable.ObservableJust;
import com.jetpackframework.rxjetpack.observable.ObservableLiveData;
import com.jetpackframework.rxjetpack.observable.ObservableMap;
import com.jetpackframework.rxjetpack.observable.ObservableObserveOn;
import com.jetpackframework.rxjetpack.observable.ObservableSource;
import com.jetpackframework.rxjetpack.observable.ObservableSubScribeOn;
import com.jetpackframework.rxjetpack.observable.ObservableTimer;
import com.jetpackframework.rxjetpack.observer.LambdaObsever;
import com.jetpackframework.rxjetpack.thread.Schedule;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class Observable<T> implements ObservableSource<T> {

    public void subscribe(){
        subscribe(new LambdaObsever<T>(onSubscribe,onNext,onError,onComplete));
    }
    public void subscribe(@NonNull Observer<T> observer){
        Objects.requireNonNull(observer);
        subscribeActual(observer);
    }

    /**
     * subscribe执行线程配置
     * @param schedule
     * @return
     */
    public Observable<T> subscribeOn(Schedule schedule){
        Objects.requireNonNull(schedule);
        return RxJetpack.onAssembly(new ObservableSubScribeOn<T>(this,schedule));
    }

    /**
     * observe执行线程配置
     * @param schedule
     * @return
     */
    public Observable<T> observeOn(Schedule schedule){
        Objects.requireNonNull(schedule);
        return RxJetpack.onAssembly(new ObservableObserveOn<T>(this,schedule));
    }

    /**
     * 延时任务
     * @param delay   延时时长
     * @param unit  延时时长单位
     * @param scheduler  延时任务所在的线程
     * @return
     */
    public Observable<T> timer(long delay, TimeUnit unit,Schedule scheduler){
        Objects.requireNonNull(scheduler);
        Objects.requireNonNull(unit);
        return RxJetpack.onAssembly(new ObservableTimer<T>(delay,unit,scheduler));
    }

    /**
     * from操作符
     * @param iterable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> from(Iterator<T> iterable){
        Objects.requireNonNull(iterable);
        return RxJetpack.onAssembly(new ObservableFrom<T>(iterable));
    }
    public static <T> Observable<T> fromArray(T[] ts){
        Objects.requireNonNull(ts);
        return RxJetpack.onAssembly(new ObservableFromArray<T>(ts));
    }
    public static <T> Observable<T> fromSparseArray(SparseArray<T> sparseArray){
        return from(sparseArray.iterator());
    }
    public static <T> Observable<T> fromIterable(Iterable<T> iterable){
        return from(iterable.iterator());
    }

    /**
     * each操作符
     * @param consumer
     */
    public void each(final Consumer<T> consumer){
        Objects.requireNonNull(consumer);
        subscribe(new LambdaObsever<>(onSubscribe,consumer,onError,onComplete));
    }


    /**
     * create操作符
     * @param subscribe
     * @param <T>
     * @return
     */
    public static <T> Observable<T> create(ObservableOnSubscribe<T> subscribe){
        Objects.requireNonNull(subscribe);
        return RxJetpack.onAssembly(new ObservableCreate<T>(subscribe));
    }

    /**
     * just操作符
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Observable<T> just(T t){
        Objects.requireNonNull(t);
        return RxJetpack.onAssembly(new ObservableJust<T>(t));
    }

    /**
     * doOnNext操作符, 在观察者接收数据之前会调用onNext参数的accept()方法
     * @param onNext
     * @return
     */
    public Observable<T> doOnNext(Consumer<T> onNext){
        Objects.requireNonNull(onNext);
        return RxJetpack.onAssembly(new ObservableDoOnNext<T>(this,onNext));
    }

    /**
     * map操作符
     * @param function
     * @param <R>
     * @return
     */
    public <R> Observable<R> map(Function<T,R> function){
        Objects.requireNonNull(function);
        return RxJetpack.onAssembly(new ObservableMap<>(this,function));
    }

    /**
     * filter 过滤器  过滤出不想要的数据
     * @param predicate
     * @return
     */
    public Observable<T> filter(Predicate<T> predicate){
        Objects.requireNonNull(predicate);
        return RxJetpack.onAssembly(new ObservableFilter<T>(this,predicate));
    }
    public <R> Observable<R> flatMap(Function<T,ObservableSource<R>> function){
        Objects.requireNonNull(function);
        return RxJetpack.onAssembly(new ObservableFlatMap<T,R>(this,function));
    }

    /**
     * 合并多个Observable
     * @param observable
     * @param <T>
     * @return
     */
    public static <T> Observable<T> concat(Observable<T>... observable){
        Objects.requireNonNull(observable);
        return RxJetpack.onAssembly(new ObservableConcat(observable));
    }

    /**
     * liveData 操作符  将一个livedata转换为Observable
     * @param liveData
     * @param <T>
     * @return
     */
    public static <T> Observable<T> liveData(LiveData<T> liveData){
        Objects.requireNonNull(liveData);

        return RxJetpack.onAssembly(new ObservableLiveData<T>(liveData));
    }

    private static final Consumer onSubscribe = new Consumer() {

        @Override
        public void accept(Object o) {
//            Log.e("TAG","Consumer onSubscribe " +Thread.currentThread().getName());
        }
    };
    private static final Consumer onNext = new Consumer() {

        @Override
        public void accept(Object o) {
//            Log.e("TAG","Consumer onNext " +Thread.currentThread().getName());
        }
    };
    private static final Consumer onError = new Consumer() {

        @Override
        public void accept(Object o) {
//            Log.e("TAG","Consumer onError " +Thread.currentThread().getName());
        }
    };
    private static final Consumer onComplete = new Consumer() {

        @Override
        public void accept(Object o) {
//            Log.e("TAG","Consumer onComplete " +Thread.currentThread().getName());
        }
    };
}
