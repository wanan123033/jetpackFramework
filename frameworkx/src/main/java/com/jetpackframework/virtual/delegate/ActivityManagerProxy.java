package com.jetpackframework.virtual.delegate;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jetpackframework.virtual.VirtualApk;
import com.jetpackframework.virtual.VirtualInstaller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ActivityManagerProxy implements InvocationHandler {

    private VirtualInstaller virtualInstaller;
    private Object origin;
    private Method method;

    public ActivityManagerProxy(VirtualInstaller virtualInstaller, Object origin) {
        this.virtualInstaller = virtualInstaller;
        this.origin = origin;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("startService") || methodName.equals("stopService") ||
                methodName.equals("bindService") || methodName.equals("unbindService")) {
            this.method = method;
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> returnType = method.getReturnType();
            Log.e("TAG-----","name="+methodName+",returnType="+returnType+",parameterTypes="+ Arrays.toString(parameterTypes));
            parameterTypes[0] = Object.class;
            if (parameterTypes.length == 7)
                parameterTypes[4] = Object.class;
            return getClass().getMethod(methodName,parameterTypes).invoke(this, args);
        }
        return method.invoke(origin,args);
    }

    public ComponentName startService(Object applicationThread, Intent intent,String action,int flags) throws RemoteException, InvocationTargetException, IllegalAccessException {
        ComponentName component = intent.getComponent();
        Log.e("TAG-----",component.getPackageName()+"----"+component.getClassName());
        VirtualApk virtualApk = virtualInstaller.getVirtualApk(component.getPackageName());
        if (virtualApk != null) {
            ServiceInfo info = virtualApk.getServiceInfo(component);
            if (info != null) {
                return startDelegateServiceForTarget(intent, info, null, RemoteService.EXTRA_COMMAND_START_SERVICE);
            }
        }
        return (ComponentName) method.invoke(origin,applicationThread,intent,action,flags);
    }

    public int bindService(Object applicationThread, IBinder iBinder,Intent intent,String action,Object connection,int flags,int i) throws RemoteException {
        ComponentName component = intent.getComponent();
        VirtualApk virtualApk = virtualInstaller.getVirtualApk(component.getPackageName());
        if (virtualApk != null) {
            ServiceInfo info = virtualApk.getServiceInfo(component);
            if (info != null) {
                Bundle bundle = new Bundle();
                bundle.putBinder("sc", (IBinder) connection);
                startDelegateServiceForTarget(intent,info,bundle,RemoteService.EXTRA_COMMAND_BINDSERVICE);
            }
        }
        return 1;
    }
    public int stopService(Object applicationThread,Intent intent,String action,int flags) throws RemoteException, InvocationTargetException, IllegalAccessException {
        ComponentName component = intent.getComponent();
        Log.e("TAG-----",component.getPackageName()+"----"+component.getClassName());
        VirtualApk virtualApk = virtualInstaller.getVirtualApk(component.getPackageName());
        if (virtualApk != null) {
            ServiceInfo info = virtualApk.getServiceInfo(component);
            if (info != null) {
                startDelegateServiceForTarget(intent, info, null, RemoteService.EXTRA_COMMAND_STOP_SERVICE);
                return 1;
            }
        }
        return (int) method.invoke(origin,applicationThread,intent,action,flags);
    }
    public boolean unbindService(Object iBinder) throws RemoteException, InvocationTargetException, IllegalAccessException {
        Intent target = virtualInstaller.forgetServiceConnection((IBinder) iBinder);
        if (target == null) {
            // is host service
            return (boolean) method.invoke(origin, iBinder);
        }
        ResolveInfo resolveInfo = virtualInstaller.getVirtualApk(target.getComponent().getPackageName()).resolveService(target, 0);
        startDelegateServiceForTarget(target, resolveInfo.serviceInfo, null, RemoteService.EXTRA_COMMAND_UNBIND_SERVICE);
        return true;
    }
    private ComponentName startDelegateServiceForTarget(Intent intent, ServiceInfo info, Bundle bundle, int command) {
        if (info != null) {
            intent.setComponent(new ComponentName(info.packageName, info.name));
            if (bundle != null){
                intent.putExtras(bundle);
            }
            Intent target = new Intent();
            target.setClass(virtualInstaller.getHostContext(),RemoteService.class);
            target.putExtra(RemoteService.EXTRA_COMMAND,command);
            target.putExtra(RemoteService.EXTRA_INTENT,intent);

            virtualInstaller.getHostContext().startService(target);
        }
        return null;
    }
}
