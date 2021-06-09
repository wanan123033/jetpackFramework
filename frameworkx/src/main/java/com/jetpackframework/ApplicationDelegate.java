package com.jetpackframework;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;

import com.jetpackframework.applicationdelegate.ApplicationCall;
import com.jetpackframework.applicationdelegate.ApplicationHandler;
import com.jetpackframework.arouter.ARouterApplicationDelegate;
import com.jetpackframework.base.JetpackApplicationDelegate;
import com.jetpackframework.fixdex.FixDexApplicationDelegate;
import com.jetpackframework.retrofit.IRetrofitUtil;

import java.lang.ref.SoftReference;

/**
 * 通过催生代理的方式集成该框架
 *      集成步骤
 *          1.在attachBaseContext()方法中创建该类的对象,并初始化
 *          2.与Application的生命周期进行绑定
 *     代码参见
            @see com.jetpackframework.Application
 */
public class ApplicationDelegate implements com.jetpackframework.applicationdelegate.ApplicationDelegate {
    private ApplicationHandler<ApplicationDelegate> handler;
    private static ApplicationDelegate delegate;

    private com.jetpackframework.applicationdelegate.ApplicationDelegate jetPack,fixDex;
    private ARouterApplicationDelegate arouter;
    public static ApplicationDelegate getInstance() {
        if (delegate == null){
            delegate = new ApplicationDelegate();
        }
        return delegate;
    }

    private ApplicationDelegate(){
        jetPack = JetpackApplicationDelegate.getInstance();
//        fixDex = FixDexApplicationDelegate.getInstance();
        arouter = ARouterApplicationDelegate.getInstance();
    }
    public void setProjectName(String projectName){
        arouter.setProjectName(projectName);
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void init(Application application) {

        SoftReference<ApplicationDelegate> delegateSoftReference = new SoftReference<>(this);
        handler = new ApplicationHandler<>(delegateSoftReference);
        jetPack.init(application);
//        fixDex.init(application);
        arouter.init(application);
    }

    @Override
    public String getPackageName() {
        return jetPack.getPackageName();
    }

    @Override
    public void onCreate() {
        ApplicationCall.callOnCreate(jetPack.getHandler());
//        ApplicationCall.callOnCreate(fixDex.getHandler());
        ApplicationCall.callOnCreate(arouter.getHandler());
    }

    @Override
    public void onLowMemory() {
        ApplicationCall.callOnLowMemory(jetPack.getHandler());
//        ApplicationCall.callOnLowMemory(fixDex.getHandler());
        ApplicationCall.callOnLowMemory(arouter.getHandler());
    }

    @Override
    public void onTrimMemory(int level) {
        ApplicationCall.callOnTrimMemory(jetPack.getHandler(),level);
//        ApplicationCall.callOnTrimMemory(fixDex.getHandler(),level);
        ApplicationCall.callOnTrimMemory(arouter.getHandler(),level);
    }

    @Override
    public void onTerminate() {
        ApplicationCall.callOnTerminate(jetPack.getHandler());
//        ApplicationCall.callOnTerminate(fixDex.getHandler());
        ApplicationCall.callOnTerminate(arouter.getHandler());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        ApplicationCall.callOnConfigurationChanged(jetPack.getHandler(),newConfig);
//        ApplicationCall.callOnConfigurationChanged(fixDex.getHandler(),newConfig);
        ApplicationCall.callOnConfigurationChanged(arouter.getHandler(),newConfig);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        ApplicationCall.callOnBaseContextAttached(jetPack.getHandler(),base);
//        ApplicationCall.callOnBaseContextAttached(fixDex.getHandler(),base);
        ApplicationCall.callOnBaseContextAttached(arouter.getHandler(),base);
    }

    @Override
    public void exit() {
        ApplicationCall.callOnExit(jetPack.getHandler());
//        ApplicationCall.callOnExit(fixDex.getHandler());
        ApplicationCall.callOnExit(arouter.getHandler());
    }

}