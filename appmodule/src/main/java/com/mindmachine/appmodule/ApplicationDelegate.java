package com.mindmachine.appmodule;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;

import com.gwm.annotation.router.AutoService;
import com.jetpackframework.applicationdelegate.ApplicationHandler;

import java.lang.ref.SoftReference;

@AutoService(com.jetpackframework.applicationdelegate.ApplicationDelegate.class)
public class ApplicationDelegate implements com.jetpackframework.applicationdelegate.ApplicationDelegate{
    ApplicationHandler handler;
    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void init(Application application) {
        handler = new ApplicationHandler(new SoftReference(this));
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onBaseContextAttached(Context base) {

    }

    @Override
    public void exit() {

    }
}
