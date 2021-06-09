package com.jetpackframework.applicationdelegate;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;

public class ApplicationCall {
    public static final int ACTION_ON_CREATE = 1;
    public static final int ACTION_ON_BASECONTEXTATTACHED = 2;
    public static final int ACTION_ON_LOWMEMORY = 3;
    public static final int ACTION_ON_TRIMMEMORY = 4;
    public static final int ACTION_ON_TERMINATE = 5;
    public static final int ACTION_ON_CONFIGURATION_CHANGED = 6;
    public static final int ACTION_ON_EXIT = 7;

    public static void callOnCreate(Handler dexHandler) {
        Message msg = Message.obtain(dexHandler,ACTION_ON_CREATE);
        dexHandler.handleMessage(msg);
    }

    public static void callOnBaseContextAttached(Handler dexHandler, Context context) {
        Message msg = Message.obtain(dexHandler,ACTION_ON_BASECONTEXTATTACHED,context);
        dexHandler.handleMessage(msg);
    }

    public static void callOnLowMemory(Handler handler) {
        Message msg = Message.obtain(handler,ACTION_ON_LOWMEMORY);
        handler.handleMessage(msg);
    }

    public static void callOnTrimMemory(Handler handler, int level) {
        Message msg = Message.obtain(handler,ACTION_ON_TRIMMEMORY,level);
        handler.handleMessage(msg);
    }

    public static void callOnTerminate(Handler handler) {
        Message msg = Message.obtain(handler,ACTION_ON_TERMINATE);
        handler.handleMessage(msg);
    }

    public static void callOnConfigurationChanged(Handler handler, Configuration newConfig) {
        Message msg = Message.obtain(handler,ACTION_ON_CONFIGURATION_CHANGED,newConfig);
        handler.handleMessage(msg);
    }
    public static void callOnExit(Handler handler){
        Message msg = Message.obtain(handler,ACTION_ON_EXIT);
        handler.handleMessage(msg);
    }
}
