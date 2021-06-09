package com.jetpackframework.rxjetpack.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorSchedule implements Schedule {
    private ExecutorService executor;
    private ExecutorWorker worker;

    private static ExecutorSchedule schedule;
    public synchronized static ExecutorSchedule getInstance(ExecutorService executor){
        if (schedule == null){
            schedule = new ExecutorSchedule(executor);
        }
        schedule.executor = executor;
        return schedule;
    }

    private ExecutorSchedule(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public Worker createWorker() {
        if (worker == null){
            worker = new ExecutorWorker(executor);
        }
        return worker;
    }

    @Override
    public void shutdown() {
        if (worker != null)
            worker.executor.shutdownNow();
    }

    private static class ExecutorWorker implements Worker{

        private ExecutorService executor;

        public ExecutorWorker(ExecutorService executor) {
            this.executor = executor;
        }

        @Override
        public void schedule(Runnable runnable) {
            executor.execute(runnable);
        }

        @Override
        public void scheduleTimer(Runnable runnable, long delay, TimeUnit unit) {
            if (executor instanceof ScheduledExecutorService) {
                ((ScheduledExecutorService) executor).schedule(runnable,delay,unit);
            }else {
                Executors.newSingleThreadScheduledExecutor().schedule(runnable,delay,unit);
            }
        }
    }
}
