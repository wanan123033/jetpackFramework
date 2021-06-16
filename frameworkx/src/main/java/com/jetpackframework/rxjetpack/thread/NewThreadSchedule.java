package com.jetpackframework.rxjetpack.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NewThreadSchedule implements Schedule {
    private ThreadFactory factory;
    private NewThreadWorker worker;

    private static Schedule schedule;
    public static synchronized Schedule getInstance() {
        if (schedule == null){
            schedule = new NewThreadSchedule();
        }
        return schedule;
    }
    private NewThreadSchedule(){
        factory = new RxThreadFactory();
    }
    public NewThreadSchedule(ThreadFactory factory){
        this.factory = factory;
    }
    @Override
    public Worker createWorker() {
        if (worker == null){
            worker = new NewThreadWorker(factory);
        }
        return worker;
    }

    @Override
    public void shutdown() {
        if (worker != null)
            worker.scheduledExecutorService.shutdownNow();
    }


    private static class NewThreadWorker implements Worker{
        private final ScheduledExecutorService scheduledExecutorService;

        public NewThreadWorker(ThreadFactory factory) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(factory);
        }

        @Override
        public void schedule(Runnable runnable) {
            scheduledExecutorService.execute(runnable);
        }

        @Override
        public void scheduleTimer(Runnable runnable, long delay, TimeUnit unit) {
            scheduledExecutorService.schedule(runnable,delay,unit);
        }
    }
}
