package com.jetpackframework;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;


import com.jetpackframework.applicationdelegate.ApplicationCall;
import com.jetpackframework.applicationdelegate.ApplicationDelegate;
import com.jetpackframework.applicationdelegate.ApplicationHandler;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
public class MergeApplicationDelegate implements com.jetpackframework.applicationdelegate.ApplicationDelegate{
    private ApplicationHandler handler;
    private Application application;

    List<ApplicationDelegate> delegates;

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void init(Application application) {
        this.application = application;
        handler = new ApplicationHandler(new SoftReference<>(this));
        delegates = new ArrayList<>();
        ServiceLoader<com.jetpackframework.applicationdelegate.ApplicationDelegate> loader = ServiceLoader.load(com.jetpackframework.applicationdelegate.ApplicationDelegate.class);
        Iterator<com.jetpackframework.applicationdelegate.ApplicationDelegate> iterator = loader.iterator();
        for (;iterator.hasNext();){
            delegates.add(iterator.next());
        }
        for (ApplicationDelegate delegate : delegates){
            delegate.init(application);
        }
    }

    @Override
    public String getPackageName() {
        return application.getPackageName();
    }

    @Override
    public void onCreate() {
        for (ApplicationDelegate delegate : delegates){
            ApplicationCall.callOnCreate(delegate.getHandler());
        }
    }

    @Override
    public void onLowMemory() {
        for (ApplicationDelegate delegate : delegates){
            ApplicationCall.callOnLowMemory(delegate.getHandler());
        }
    }

    @Override
    public void onTrimMemory(int level) {
        for (ApplicationDelegate delegate : delegates){
            ApplicationCall.callOnTrimMemory(delegate.getHandler(),level);
        }
    }

    @Override
    public void onTerminate() {
        for (ApplicationDelegate delegate : delegates){
            ApplicationCall.callOnTerminate(delegate.getHandler());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        for (ApplicationDelegate delegate : delegates){
            ApplicationCall.callOnConfigurationChanged(delegate.getHandler(),newConfig);
        }
    }

    @Override
    public void onBaseContextAttached(Context base) {
        for (ApplicationDelegate delegate : delegates){
            ApplicationCall.callOnBaseContextAttached(delegate.getHandler(),base);
        }
    }

    @Override
    public void exit() {
        for (ApplicationDelegate delegate : delegates){
            ApplicationCall.callOnExit(delegate.getHandler());
        }
    }
}
