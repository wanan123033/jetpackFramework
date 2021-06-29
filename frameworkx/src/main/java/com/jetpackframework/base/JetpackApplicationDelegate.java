package com.jetpackframework.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Process;
import android.util.Log;

import com.bytedance.boost_multidex.BoostMultiDex;
import com.bytedance.boost_multidex.BoostMultiDexApplication;
import com.jetpackframework.ActivityLifecycleCallbacks;
import com.jetpackframework.AppHandler;
import com.jetpackframework.applicationdelegate.ApplicationDelegate;
import com.jetpackframework.applicationdelegate.ApplicationHandler;
import com.jetpackframework.applicationdelegate.ApplicationLifeCycle;
import com.jetpackframework.http.HttpCache;
import com.jetpackframework.http.HttpUtil;
import com.jetpackframework.ioc.ARouterEventClassUtil;
import com.jetpackframework.ioc.ARouterLayoutUtil;
import com.jetpackframework.ioc.EventClassUtil;
import com.jetpackframework.ioc.LayoutUtil;
import com.jetpackframework.rxjetpack.thread.Schedules;
import com.tencent.mmkv.MMKV;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class JetpackApplicationDelegate implements ApplicationLifeCycle, ApplicationDelegate {
    private Application application;
    private ApplicationHandler handler;
    private LayoutUtil layoutUtil;
    private EventClassUtil eventClassUtil;
    private List<String> firstToasts;
    private MMKV mmkv;
    private List<Activity> activities;

    private static JetpackApplicationDelegate delegate;
    public synchronized static JetpackApplicationDelegate getInstance(){
        if (delegate == null){
            delegate = new JetpackApplicationDelegate();
        }
        return delegate;
    }

    private JetpackApplicationDelegate(){

    }
    public void init(Application application){
        this.application = application;
        SoftReference<JetpackApplicationDelegate> delegateSoftReference = new SoftReference<>(this);
        handler = new ApplicationHandler(delegateSoftReference);
    }

    @Override
    public String getPackageName() {
        return application.getPackageName();
    }


    @Override
    public void onCreate() {
        activities = new ArrayList<>();
        firstToasts = new ArrayList<>();
        layoutUtil = ARouterLayoutUtil.getInstance();
        eventClassUtil = ARouterEventClassUtil.getInstance();
        MMKV.initialize(application);
        mmkv = MMKV.defaultMMKV();
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks(null));
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
        BoostMultiDex.install(base);
    }

    @Override
    public void exit() {
        firstToasts.clear();
        layoutUtil.clear();
        eventClassUtil.clear();
        Schedules.shutdown();
        HttpUtil.getInstance().close();
        MMKV.onExit();
        AppHandler.getAppHandler().removeCallbacksAndMessages(null);
        for (Activity activity : activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
        Process.killProcess(Process.myPid()); //自杀
        System.exit(0);
    }

    public ApplicationHandler getHandler() {
        return handler;
    }
    public List<String> getFisrstToasts(){
        if (firstToasts == null){
            throw new NullPointerException("jetpack no init");
        }
        return firstToasts;
    }

    public MMKV getMmkv() {
        return mmkv;
    }

    public Resources getResources() {
        return application.getResources();
    }

    public List<Activity> getActivitys() {
        return activities;
    }
}
