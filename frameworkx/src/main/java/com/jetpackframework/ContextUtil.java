package com.jetpackframework;

import android.app.Application;
import android.content.Context;

/**
 * 为应用的所有模块提供Context的支持,不用担心内存泄漏问题,要引用Context统一从这里获取
 */
public class ContextUtil {
    private static Context mAppContext;

    public static Context get() {
        if (mAppContext == null) {
            mAppContext = getAppContext();
        }
        return mAppContext;
    }

    /**
     * 设置全局级别的Context，在Application初始化时调用
     * @param context Application的Context
     */
    public static void setGlobalContext(Context context) {
        mAppContext = context;
    }

    /**
     * 通过反射获取AppContext，避免直接使用static context
     */
    public static Context getAppContext() {
        Application application = null;
        try {
            application = (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return application;
    }
}
