package com.jetpackframework.applicationdelegate;

import android.content.Context;
import android.content.res.Configuration;

public interface ApplicationLifeCycle {


    void onCreate();


    void onLowMemory();


    void onTrimMemory(int level);


    void onTerminate();


    void onConfigurationChanged(Configuration newConfig);


    void onBaseContextAttached(Context base);

    void exit();
}
