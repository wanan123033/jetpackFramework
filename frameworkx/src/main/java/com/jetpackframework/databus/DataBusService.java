package com.jetpackframework.databus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.jetpackframework.DataBusAidl;

public class DataBusService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    private static final IBinder iBinder = new DataBusAidl.Stub() {
        @Override
        public void post(String event, String data) throws RemoteException {
            DataBus.getDefault().with(event).postValue(data);
        }
    };
}
