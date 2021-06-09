package com.jetpackframework.virtual;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;

import com.jetpackframework.Reflector;
import com.jetpackframework.virtual.delegate.ActivityManagerProxy;
import com.jetpackframework.virtual.delegate.VirtualInstrumentation;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class VirtualInstaller implements ServiceConnection {

    private static VirtualInstaller installer;
    private Context context;

    private VirtualAidl virtual;

    private Object mActivityManager;
    private ArrayMap<IBinder, Intent> mBoundServices = new ArrayMap<IBinder, Intent>();

    private VirtualInstaller(Context context) {
        this.context = context;
        Intent intent = new Intent(context, VirtualInstallService.class);
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
        try {
            hookInstrumentation();
            hookActivityManager();
        } catch (Reflector.ReflectedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized VirtualInstaller getInstance(Context context) {
        if (installer == null){
            installer = new VirtualInstaller(context);
        }
        return installer;
    }

    private void hookActivityManager() {
        try {
            Object defaultSingleton;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                defaultSingleton = Reflector.on(ActivityManager.class).field("IActivityManagerSingleton").get();
            } else {
                defaultSingleton = Reflector.on("android.app.ActivityManagerNative").field("gDefault").get();
            }
            Object origin = Reflector.with(defaultSingleton).method("get").call();
            Object activityManagerProxy = Proxy.newProxyInstance(context.getClassLoader(), new Class[] { Class.forName("android.app.IActivityManager")}, createActivityManagerProxy(origin));

            // Hook IActivityManager from ActivityManagerNative
            Reflector.with(defaultSingleton).field("mInstance").set(activityManagerProxy);
            Object o = Reflector.with(defaultSingleton).method("get").call();
            if (o == activityManagerProxy) {
                this.mActivityManager = activityManagerProxy;
            }
        } catch (Exception e) {
            Log.w("TAG", e);
        }

    }

    private InvocationHandler createActivityManagerProxy(Object origin) {
        return new ActivityManagerProxy(this,origin);
    }

    private void hookInstrumentation() throws Reflector.ReflectedException {
        Log.e("TAG----","hookInstrumentation");
        Object activityThread = Reflector.on("android.app.ActivityThread").method("currentActivityThread").call();
        Instrumentation instrumentation = Reflector.with(activityThread).method("getInstrumentation").call();
        VirtualInstrumentation virtualInstrumentation = new VirtualInstrumentation(this,instrumentation);
        Reflector.with(activityThread).field("mInstrumentation").set(virtualInstrumentation);
    }
    public void install(File apkFile) throws RemoteException {
        install(apkFile,null);
    }

    public void install(File apkFile, IVirtualInstallListenner listenner) throws RemoteException {
        String packageName = virtual.install(apkFile.getAbsolutePath(),listenner);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        virtual = VirtualAidl.Stub.asInterface(service);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        virtual = null;
    }

    public Context getHostContext() {
        return context;
    }

    public VirtualApk getVirtualApk(String packageName) throws RemoteException {
        return virtual.getVirtualApk(packageName);
    }

    public Object getActivityManager() {
        return mActivityManager;
    }

    public Intent getServiceConnection(IBinder conn){
        return mBoundServices.get(conn);
    }

    public void putServiceConnection(IBinder conn,Intent intent){
        mBoundServices.put(conn,intent);
    }
    public Intent forgetServiceConnection(IBinder conn){
        return mBoundServices.remove(conn);
    }
}
