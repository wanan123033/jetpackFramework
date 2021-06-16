package com.jetpackframework.rxjetpack.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class IOSchedule implements Schedule {
    private static IOSchedule ioSchedule;
    private final IOWorker worker;
    private IOSchedule(){
        worker = new IOWorker();
    }

    public IOSchedule(ThreadFactory threadFactory){
        worker = new IOWorker(threadFactory);
    }

    public static synchronized Schedule getInstance() {
        if (ioSchedule == null){
            ioSchedule = new IOSchedule();
        }
        return ioSchedule;
    }

    @Override
    public Worker createWorker() {
        return worker;
    }

    @Override
    public void shutdown() {
        worker.executor.shutdownNow();
    }

    private static class IOWorker implements Worker{
        private ScheduledExecutorService executor;
        public IOWorker(){
            executor = Executors.newScheduledThreadPool(3);
        }

        public IOWorker(ThreadFactory threadFactory) {
            executor = Executors.newScheduledThreadPool(3,threadFactory);
        }

        @Override
        public void schedule(Runnable runnable) {
            executor.execute(runnable);
        }

        @Override
        public void scheduleTimer(Runnable runnable, long delay, TimeUnit unit) {
            executor.schedule(runnable,delay,unit);
        }
    }
}
