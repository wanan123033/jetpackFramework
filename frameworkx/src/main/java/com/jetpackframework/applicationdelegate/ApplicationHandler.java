package com.jetpackframework.applicationdelegate;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.SoftReference;

public class ApplicationHandler<A extends ApplicationLifeCycle> extends Handler {

    private final SoftReference<A> appDelegate;

    public ApplicationHandler(SoftReference<A> appDelegate) {
        this.appDelegate = appDelegate;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        handleMessageImpl(msg);
    }

    private void handleMessageImpl(Message msg) {
        ApplicationLifeCycle lifeCycle = appDelegate.get();
        switch (msg.what){
            case ApplicationCall.ACTION_ON_CREATE:
                lifeCycle.onCreate();
                break;
            case ApplicationCall.ACTION_ON_BASECONTEXTATTACHED:
                lifeCycle.onBaseContextAttached((Context) msg.obj);
                break;
            case ApplicationCall.ACTION_ON_LOWMEMORY:
                lifeCycle.onLowMemory();
                break;
            case ApplicationCall.ACTION_ON_TRIMMEMORY:
                lifeCycle.onTrimMemory((Integer) msg.obj);
                break;
            case ApplicationCall.ACTION_ON_CONFIGURATION_CHANGED:
                lifeCycle.onConfigurationChanged((Configuration) msg.obj);
                break;
            case ApplicationCall.ACTION_ON_TERMINATE:
                lifeCycle.onTerminate();
                break;
            case ApplicationCall.ACTION_ON_EXIT:
                lifeCycle.exit();
                break;

        }
    }
}
