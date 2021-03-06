package com.jetpackframework.arouter;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;

import com.jetpackframework.ContextUtil;
import com.jetpackframework.Reflector;
import com.jetpackframework.applicationdelegate.ApplicationDelegate;
import com.jetpackframework.applicationdelegate.ApplicationHandler;
import com.jetpackframework.ioc.ARouterEventClassUtil;
import com.jetpackframework.ioc.ARouterLayoutUtil;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 *  arouter 的Application 代理类
 */
public class ARouterApplicationDelegate implements ApplicationDelegate {
    private Application application;
    private ApplicationHandler handler;

    @Override
    public void onCreate() {
        ContextUtil.setGlobalContext(application);
        RouterApp.init(new RouterMerga());
        ARouterLayoutUtil.getInstance().init(application);
        ARouterEventClassUtil.getInstance().init(application);
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

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void init(Application application) {
        this.application = application;
        SoftReference<ARouterApplicationDelegate> delegateSoftReference = new SoftReference<>(this);
        handler = new ApplicationHandler(delegateSoftReference);
    }

    @Override
    public String getPackageName() {
        return application.getPackageName();
    }
}
