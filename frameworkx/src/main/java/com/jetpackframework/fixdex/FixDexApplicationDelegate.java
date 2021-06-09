package com.jetpackframework.fixdex;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import com.jetpackframework.applicationdelegate.ApplicationDelegate;
import com.jetpackframework.applicationdelegate.ApplicationHandler;

import java.lang.ref.SoftReference;

/**
 * 热修复的Application代理
 */
public class FixDexApplicationDelegate implements ApplicationDelegate {
    private ApplicationHandler<FixDexApplicationDelegate> handler;
    private static FixDexApplicationDelegate delegate;
    private static FixConfig fixConfig;
    private Application application;

    public static void setFixConfig(FixConfig config){
        fixConfig = config;
    }
    private FixDexApplicationDelegate(){
    }

    public static synchronized FixDexApplicationDelegate getInstance() {
        if (delegate == null){
            delegate = new FixDexApplicationDelegate();
        }
        return delegate;
    }

    @Override
    public void onCreate() {
        FixDexUtils.getInstance().loadDex(fixConfig.getDexDir());
        FixResourceUtils.getInstance().loadResource(fixConfig.getResourceDir());
        FixSoUtils.installSoABI(fixConfig.getSoDir());
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
    public ApplicationHandler getHandler() {
        return handler;
    }

    @Override
    public void init(Application application) {
        this.application = application;
        SoftReference<FixDexApplicationDelegate> delegateSoftReference = new SoftReference<>(this);
        handler = new ApplicationHandler<>(delegateSoftReference);
    }

    @Override
    public String getPackageName() {
        return application.getPackageName();
    }
}
