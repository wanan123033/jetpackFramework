package com.jetpackframework.virtual.delegate;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.util.Log;

import com.jetpackframework.virtual.VirtualApk;

public class PluginContext extends ContextWrapper {
    private VirtualApk apk;
    private PackageManager manager;

    public PluginContext(VirtualApk apk, Context base) {
        super(base);
        this.apk = apk;
        manager = new PluginPackageManager(apk,base.getPackageManager());
    }

    @Override
    public String getPackageName() {
        return apk.getPackageName();
    }

//    @Override
//    public PackageManager getPackageManager() {
//        return manager;
//    }
}
