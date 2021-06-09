package com.jetpackframework;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.jetpackframework.base.JetpackApplicationDelegate;


public class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private final Application.ActivityLifecycleCallbacks callback;

    public ActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        this.callback = callback;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        JetpackApplicationDelegate.getInstance().getActivitys().add(activity);
        if (callback != null){
            callback.onActivityCreated(activity,savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (callback != null){
            callback.onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (callback != null){
            callback.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (callback != null){
            callback.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (callback != null){
            callback.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (callback != null){
            callback.onActivitySaveInstanceState(activity,outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        JetpackApplicationDelegate.getInstance().getActivitys().remove(activity);
        if (callback != null){
            callback.onActivityDestroyed(activity);
        }
    }
}
