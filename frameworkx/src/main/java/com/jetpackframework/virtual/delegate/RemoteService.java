package com.jetpackframework.virtual.delegate;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jetpackframework.Reflector;
import com.jetpackframework.virtual.VirtualApk;
import com.jetpackframework.virtual.VirtualInstaller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class RemoteService extends Service {
    public static final int EXTRA_COMMAND_START_SERVICE = 11;
    public static final int EXTRA_COMMAND_BIND_SERVICE = 22;
    public static final int EXTRA_COMMAND_STOP_SERVICE = 33;
    public static final int EXTRA_COMMAND_UNBIND_SERVICE = 44;

    public static final String EXTRA_COMMAND = "EXTRA_COMMAND";
    public static final String EXTRA_INTENT = "EXTRA_INTENT";
    private VirtualApk apk;
    private DexClassLoader classLoader;
    private VirtualInstaller installer;

    private Map<ComponentName,Service> serviceMap;


    @Override
    public void onCreate() {
        super.onCreate();
        installer = VirtualInstaller.getInstance(getApplication());
        serviceMap = new HashMap<>();
        Log.e("tag","onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int command = intent.getIntExtra(EXTRA_COMMAND, 0);
            try {
                switch (command) {
                    case EXTRA_COMMAND_START_SERVICE:
                        startService((Intent) intent.getParcelableExtra(EXTRA_INTENT), flags, startId);
                        break;
                    case EXTRA_COMMAND_BIND_SERVICE:
                        bindService((Intent) intent.getParcelableExtra(EXTRA_INTENT), flags, startId);
                        break;
                    case EXTRA_COMMAND_UNBIND_SERVICE:
                        unbindService((Intent) intent.getParcelableExtra(EXTRA_INTENT), flags, startId);
                        break;
                    case EXTRA_COMMAND_STOP_SERVICE:
                        stopService((Intent) intent.getParcelableExtra(EXTRA_INTENT), flags, startId);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void stopService(Intent intent, int flags, int startId) {
        ComponentName component = getComponentName(intent);
        Log.e("TAG......",component.toString());
        if (apk != null){
            Service service = serviceMap.get(component);
            if (service != null){
                service.onDestroy();
                serviceMap.remove(component);
            }
        }
    }

    private ComponentName getComponentName(Intent intent) {
        ComponentName component = intent.getComponent();
        try {
            Log.e("TAG----", component.toString());
            apk = installer.getVirtualApk(component.getPackageName());
            if (apk != null)
                classLoader = apk.getClassLoader();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return component;
    }

    private void unbindService(Intent intent, int flags, int startId) {
        ComponentName component = getComponentName(intent);
        Log.e("TAG......",component.toString());
        if (apk != null){
            Service service = serviceMap.get(component);
            if (service != null){
                service.onUnbind(intent);
                service.onDestroy();
                serviceMap.remove(component);
            }
        }
    }

    private void startService(Intent intent, int flags, int startId) throws Exception {
        ComponentName component = getComponentName(intent);

        Log.e("TAG......",component.toString());
        if (apk != null){
            Service service = (Service) classLoader.loadClass(component.getClassName()).newInstance();
            Log.e("TAG-----","service="+service);
            Object mainThread = Reflector.on("android.app.ActivityThread").method("currentActivityThread").call();
            Object appThread = Reflector.with(mainThread).method("getApplicationThread").call();
            IBinder token = Reflector.with(appThread).method("asBinder").call();

            Method attach = service.getClass().getMethod("attach", Context.class, Class.forName("android.app.ActivityThread"), String.class, IBinder.class, Application.class, Object.class);
            attach.invoke(service, getApplicationContext(), mainThread, component.getClassName(), token, getApplication(), installer.getActivityManager());

            service.onCreate();
            service.onStartCommand(intent,flags,startId);
            serviceMap.put(component,service);
        }
    }

    private void bindService(Intent intent, int flags, int startId) throws Exception {
        ComponentName component = getComponentName(intent);
        if (apk != null) {
            Service service = (Service) classLoader.loadClass(component.getClassName()).newInstance();
            Log.e("TAG-----", "service=" + service);
            Object mainThread = Reflector.on("android.app.ActivityThread").method("currentActivityThread").call();
            Object appThread = Reflector.with(mainThread).method("getApplicationThread").call();
            IBinder token = Reflector.with(appThread).method("asBinder").call();

            Method attach = service.getClass().getMethod("attach", Context.class, Class.forName("android.app.ActivityThread"), String.class, IBinder.class, Application.class, Object.class);
            attach.invoke(service, getApplicationContext(), mainThread, component.getClassName(), token, getApplication(), installer.getActivityManager());

            service.onCreate();
            IBinder iBinder = service.onBind(intent);

            IBinder serviceConn = (IBinder) intent.getExtras().getBinder("sc");
            installer.putServiceConnection(serviceConn,intent);
            Object iServiceConn = Reflector.on("android.app.IServiceConnection$Stub").method("asInterface", IBinder.class).call(serviceConn);
            if (Build.VERSION.SDK_INT >= 26){
                Reflector.QuietReflector.with(iServiceConn).method("connected", ComponentName.class, IBinder.class,boolean.class).call(component, iBinder,false);
            }else {
                Reflector.QuietReflector.with(iServiceConn).method("connected", ComponentName.class, IBinder.class).call(component, iBinder);
            }
            serviceMap.put(component,service);
        }
    }
}
