package com.jetpackframework.virtual.delegate;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.jetpackframework.virtual.VirtualApk;

public class PluginContext extends ContextWrapper {
    private VirtualApk apk;

    public PluginContext(VirtualApk apk, Context base) {
        super(base);
        this.apk = apk;
    }

    @Override
    public String getPackageName() {
        Log.e("TAG","-------getPackageName");
        return apk.getPackageName();
    }
}
