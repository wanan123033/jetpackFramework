package com.jetpackframework.virtual.delegate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.util.Log;

import com.jetpackframework.ContextUtil;
import com.jetpackframework.Reflector;
import com.jetpackframework.arouter.Router;
import com.jetpackframework.virtual.Contracts;
import com.jetpackframework.virtual.VirtualApk;
import com.jetpackframework.virtual.VirtualInstaller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

import dalvik.system.DexClassLoader;

public class VirtualInstrumentation extends Instrumentation {
    private final VirtualInstaller installer;
    private final Instrumentation instrumentation;
    private VirtualApk apk;

    public VirtualInstrumentation(VirtualInstaller installer, Instrumentation instrumentation) {
        this.installer = installer;
        this.instrumentation = instrumentation;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        try {
            Log.e("TAG----","execStartActivity Activity");
            injentIntent(intent);
            if (router(target,intent)) {
                Reflector.with(instrumentation).method("execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class)
                        .call(who, contextThread, token, target, intent, requestCode, options);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean router(Activity target, Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            if (Router.getInstance(target).isRouter(uri.toString())) {
                Router.getInstance(target).to(uri.toString()).router();
                intent.setData(null);
                return false;
            }
        }
        return true;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, String target,
            Intent intent, int requestCode, Bundle options) {
        try {
            Log.e("TAG----","execStartActivity String");
            injentIntent(intent);
            if (router(null, intent)) {
                Reflector.with(instrumentation).method("execStartActivity", Context.class, IBinder.class, IBinder.class, String.class, Intent.class, int.class, Bundle.class)
                        .call(who, contextThread, token, target, intent, requestCode, options);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    } 

    private void injentIntent(Intent intent) throws RemoteException {
        ComponentName component = intent.getComponent();
        Log.e("TAG-----","component="+component);
        if (component == null || component.getPackageName().equals(installer.getHostContext().getPackageName())){
            return;
        }
        String targetClassName = component.getClassName();
        String packageName = component.getPackageName();
        Set<String> categories = intent.getCategories();
        if (categories != null && !categories.isEmpty()){
            intent.putExtra(Contracts.VIR_CATE,new ArrayList<>(categories));
            categories.clear();
        }
        intent.addCategory(Contracts.VIR_CLASSNAME+targetClassName);
        intent.addCategory(Contracts.VIR_PACKAGENAME+packageName);

        ActivityInfo activityInfo = installer.getVirtualApk(packageName).getActivityInfo(component);
        String stubActivity = getStubActivity(activityInfo);
        intent.setClassName(installer.getHostContext(),stubActivity);

    }

    private String getStubActivity(ActivityInfo activityInfo) {
        String string = "com.virtual.%s.%s.v$%d";
        String launchMode = "standard";
        String screenOrientation = "portrait";
        switch (activityInfo.launchMode){
            case ActivityInfo.LAUNCH_SINGLE_TASK:
                launchMode = "singleTask";
                break;
            case ActivityInfo.LAUNCH_SINGLE_TOP:
                launchMode = "singleTop";
                break;
            case ActivityInfo.LAUNCH_MULTIPLE:
                launchMode = "standard";
                break;
            case ActivityInfo.LAUNCH_SINGLE_INSTANCE:
                launchMode = "singleInstance";
                break;
        }
        switch (activityInfo.screenOrientation){
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                screenOrientation = "landscape";
                break;
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                screenOrientation = "portrait";
                break;
        }
        string = String.format(string,launchMode,screenOrientation,activityInfo.launchMode);
        return string;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        try {
            return (Activity) cl.loadClass(className).newInstance();
        }catch (ClassNotFoundException e){
            return newActivity(intent);
        }
    }

    private Activity newActivity(Intent intent) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Set<String> categories = intent.getCategories();
        String className = null;
        String packageName = null;
        for (String category : categories){
            if (category.startsWith(Contracts.VIR_CLASSNAME)){
                className = category.substring(Contracts.VIR_CLASSNAME.length());
            }
            if (category.startsWith(Contracts.VIR_PACKAGENAME)){
                packageName = category.substring(Contracts.VIR_PACKAGENAME.length());
            }
        }
        try {

            apk = installer.getVirtualApk(packageName);
            DexClassLoader classLoader = apk.getClassLoader();
            Activity activity = (Activity) classLoader.loadClass(className).newInstance();
            activity.setIntent(intent);
            Reflector.QuietReflector.with(activity).field("mResources").set(apk.getResources());
            return activity;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        if (apk != null) {
            Reflector.QuietReflector.with(activity).field("mBase").set(new PluginContext(apk, ContextUtil.getAppContext()));
        }
        instrumentation.callActivityOnCreate(activity, icicle);
    }
}
