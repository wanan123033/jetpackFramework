package com.jetpackframework.databus;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.jetpackframework.DataBusAidl;

import java.util.ArrayList;
import java.util.List;

public class AidlServiceConnection implements ServiceConnection {

    private DataBusAidl aidl;

    public DataBusAidl getAidl() {
        return aidl;
    }

    private static class Instance{
        private static final AidlServiceConnection instance = new AidlServiceConnection();
    }
    private AidlServiceConnection(){
    }
    public static synchronized AidlServiceConnection getInstance(){
        return Instance.instance;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        aidl = DataBusAidl.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        aidl = null;
    }
}
