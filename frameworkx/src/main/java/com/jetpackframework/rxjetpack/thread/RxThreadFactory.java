package com.jetpackframework.rxjetpack.thread;

import java.util.concurrent.ThreadFactory;

public class RxThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
