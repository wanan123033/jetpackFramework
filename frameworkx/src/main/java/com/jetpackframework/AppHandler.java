package com.jetpackframework;

import android.os.Looper;

/**
 * 全局应用的Handler 一个应用只有一个   只能调用postXXX()方法  sendXXXX()用不了
 */
public class AppHandler extends android.os.Handler {
    private static AppHandler appHandler = new AppHandler();
    private AppHandler(){
        super(Looper.getMainLooper());

    }
    public static synchronized AppHandler getAppHandler(){
        return appHandler;
    }

}
