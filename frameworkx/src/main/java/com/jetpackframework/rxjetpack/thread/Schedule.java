package com.jetpackframework.rxjetpack.thread;

import java.util.concurrent.TimeUnit;

public interface Schedule {

    Worker createWorker();
    void shutdown();

    interface Worker{
        void schedule(Runnable runnable);
        void scheduleTimer(Runnable runnable, long delay, TimeUnit unit);
    }
}
