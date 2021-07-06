package com.mindmachine.appmodule;

import android.content.Context;

import com.jetpackframework.Application;
import com.jetpackframework.virtual.Virtual;
import com.jetpackframework.virtual.VirtualConfig;

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Virtual.install(new VirtualConfig.Builder().setContext(this).builder());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
