package com.jetpackframework.applicationdelegate;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.boost_multidex.BoostMultiDexApplication;
import com.jetpackframework.ActivityLifecycleCallbacks;
import com.jetpackframework.ioc.ARouterEventClassUtil;
import com.jetpackframework.ioc.ARouterLayoutUtil;


public abstract class BaseApplication extends Application {
    ApplicationDelegate delegate;
    private Handler handler;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        delegate = createApplicationDelegate();
        delegate.init(this);
        handler = delegate.getHandler();
        onBaseContextAttached(base);
        Log.e("TAG----",delegate.toString());
    }
    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationCall.callOnCreate(handler);
    }


    public void onBaseContextAttached(Context base) {
        ApplicationCall.callOnBaseContextAttached(handler,base);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ApplicationCall.callOnLowMemory(handler);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ApplicationCall.callOnTrimMemory(handler,level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ApplicationCall.callOnTerminate(handler);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ApplicationCall.callOnConfigurationChanged(handler,newConfig);

    }
    public void exit(){
        ApplicationCall.callOnExit(handler);
    }


//    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
//        Log.e("TAG----",this+"  registerActivityLifecycleCallbacks");
//        super.registerActivityLifecycleCallbacks(new com.jetpackframework.ActivityLifecycleCallbacks(callback));
//    }

    protected abstract ApplicationDelegate createApplicationDelegate();
}
