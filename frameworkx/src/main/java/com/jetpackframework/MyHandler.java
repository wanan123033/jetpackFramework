package com.jetpackframework;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.SoftReference;

public class MyHandler extends Handler {
    private SoftReference<HandlerListener> softReference;
    public MyHandler(HandlerListener activity){
        super(Looper.getMainLooper());
        softReference = new SoftReference<>(activity);
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        softReference.get().handleMessage(msg);
    }
}
