package com.jetpackframework.rxjetpack;

import com.jetpackframework.rxjetpack.thread.ExecutorSchedule;
import com.jetpackframework.rxjetpack.thread.IOSchedule;
import com.jetpackframework.rxjetpack.thread.NewThreadSchedule;
import com.jetpackframework.rxjetpack.thread.Schedule;

import java.util.concurrent.ThreadFactory;

public class RxJetpack {
    private Function<Observable,Observable> onObservableAssembly;
    private static RxJetpack rxJetpack;
    static {
        rxJetpack = new RxJetpack();
        reset();
    }
    private RxJetpack(){

    }
    public static <T> Observable<T> onAssembly(Observable<T> source) {
        if (rxJetpack.onObservableAssembly != null) {
            try {
                return rxJetpack.onObservableAssembly.apply(source);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return source;
    }
    public static RxJetpack setOnObservableAssembly(Function<Observable,Observable> function){
        rxJetpack.onObservableAssembly = function;
        return rxJetpack;
    }
    public static void reset(){
        setOnObservableAssembly(null);
    }

    public static Schedule createIoSchedule(ThreadFactory factory){
        return new IOSchedule(factory);
    }
    public static Schedule createNewThreadSchedule(ThreadFactory factory){
        return new NewThreadSchedule(factory);
    }
    public static Schedule createExectorSchedule(ThreadFactory factory){
        return new ExecutorSchedule(factory);
    }
}
