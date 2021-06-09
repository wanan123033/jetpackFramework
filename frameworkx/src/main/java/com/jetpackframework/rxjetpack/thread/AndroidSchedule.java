package com.jetpackframework.rxjetpack.thread;


import com.jetpackframework.AppHandler;

import java.util.concurrent.TimeUnit;

public class AndroidSchedule implements Schedule{
    private static AndroidSchedule schedule;

    private final Worker handlerWorker = new Worker() {
        @Override
        public void schedule(Runnable runnable) {
            schedule.appHandler.post(runnable);
        }

        @Override
        public void scheduleTimer(Runnable runnable, long delay, TimeUnit unit) {
            switch (unit){
                case HOURS:
                    delay = delay*60;
                case MINUTES:
                    delay = delay*60;
                case SECONDS:
                    delay = delay*1000;
                    break;
            }
            schedule.appHandler.postDelayed(runnable,delay);
        }
    };
    private AppHandler appHandler;
    private AndroidSchedule(){
        appHandler = AppHandler.getAppHandler();
    }
    public static synchronized Schedule getInstance() {
        if (schedule == null){
            schedule = new AndroidSchedule();
        }
        return schedule;
    }

    @Override
    public Worker createWorker() {
        return handlerWorker;
    }

    @Override
    public void shutdown() {

    }
}
