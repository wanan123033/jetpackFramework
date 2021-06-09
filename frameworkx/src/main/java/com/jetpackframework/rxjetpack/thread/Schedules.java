package com.jetpackframework.rxjetpack.thread;

import java.util.concurrent.ExecutorService;

public class Schedules {
    private static Schedule android,executorSchedule,newThread,io;
    static {
        android = AndroidSchedule.getInstance();
        io = IOSchedule.getInstance();
        executorSchedule = ExecutorSchedule.getInstance(null);
        newThread = NewThreadSchedule.getInstance();
    }

    public static Schedule io(){
        return io;
    }

    public static Schedule androidMainThread(){
        return android;
    }
    public static Schedule from(ExecutorService executor){
        executorSchedule = ExecutorSchedule.getInstance(executor);
        return executorSchedule;
    }
    public static Schedule newThread(){
        return newThread;
    }

    public static void shutdown(){
        io().shutdown();
        newThread().shutdown();
        executorSchedule.shutdown();
        android.shutdown();
    }
}
