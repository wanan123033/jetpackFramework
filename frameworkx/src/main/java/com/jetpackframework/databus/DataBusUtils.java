package com.jetpackframework.databus;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.GsonUtils;
import com.gwm.annotation.bus.LiveMessage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DataBusUtils {
    private static DataBusUtils dataBusUtils;
    private DataBus dataBus;
    private List<Object> objects;

    private static AidlServiceConnection connection;
    private DataBusUtils(){
        dataBus = DataBus.getDefault();
        objects = new ArrayList<>();
    }
    public static synchronized DataBusUtils getInstance(){
        if (dataBusUtils == null){
            dataBusUtils = new DataBusUtils();
        }
        return dataBusUtils;
    }

    public void register(LifecycleOwner owner){
        if (objects.contains(owner)){
            return;
        }
        objects.add(owner);
        findLiveDataMethod(owner);
    }
    public void unregister(LifecycleOwner owner){
        objects.remove(owner);
        Method[] methods = owner.getClass().getDeclaredMethods();
        for (final Method method :methods) {
            if (method.isAnnotationPresent(LiveMessage.class)) {
                LiveMessage message = method.getAnnotation(LiveMessage.class);
                dataBus.removeMessage(message.action());
            }
        }
    }


    private void findLiveDataMethod(final LifecycleOwner owner) {
        Method[] methods = owner.getClass().getMethods();
        for (final Method method :methods){
            if (method.isAnnotationPresent(LiveMessage.class)){
                LiveMessage message = method.getAnnotation(LiveMessage.class);
                dataBus.with(message.action()).observe(owner, new Observer<Object>() {
                    @Override
                    public void onChanged(Object o) {
                        try {
                            method.invoke(owner,o);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * 初始化
     * @param isAidl 是否打开aidl服务
     */
    public static void init(Context context,boolean isAidl){
        if (isAidl){
            connection = AidlServiceConnection.getInstance();
            Intent intent = new Intent(context,DataBusService.class);
            context.bindService(intent,connection,Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 发送消息,不跨进程
     * @param obj
     */
    public void post(DataBusEventBean obj){
        dataBus.with(obj.getEvent()).postValue(obj.getData());
    }

    /**
     * 发送消息,可跨进程
     * @param obj
     * @throws RemoteException
     */
    public void postAidl(DataBusEventBean obj) throws RemoteException {
        connection.getAidl().post(obj.getEvent(), GsonUtils.toJson(obj.getData()));
    }
}
