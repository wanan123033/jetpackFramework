package com.jetpackframework.virtual;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.RemoteException;
import android.util.Log;


import com.jetpackframework.Reflector;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class VirtualManager {
    private static VirtualManager mManager;
    private Context context;

    private Map<String,VirtualApk> virtualApkMap;
    private VirtualManager(Context context) {
        this.context = context;
        virtualApkMap = new HashMap<>();


    }

    public Context getHostContext(){
        return context;
    }



    public static synchronized VirtualManager getInstance(Context context) {
        if (mManager == null){
            mManager = new VirtualManager(context);
        }
        return mManager;
    }
    public String install(String apkFile, IVirtualInstallListenner listenner) throws Exception {
        if (apkFile == null){
            if (listenner != null)
                listenner.installFail(apkFile,ErrorCode.FILE_ERROR_NULL);
            return null;
        }
        File file = new File(apkFile);
        if (!file.exists() || file.isDirectory()){
            if (listenner != null)
                listenner.installFail(apkFile,ErrorCode.FILE_ERROR_EXIT);
            return null;
        }
        VirtualApk virtualApk = new VirtualApk(this,context,file);


        virtualApkMap.put(virtualApk.getPackageName(),virtualApk);

        if (listenner != null)
            listenner.installSuccess(virtualApk.getPackageName());
        return virtualApk.getPackageName();
    }
    public PackageInfo getPackageInfo(String packageName){
        return virtualApkMap.get(packageName).getPackageInfo();
    }

    public VirtualApk getVirtualApk(String packageName){
        Log.e("TAG----",virtualApkMap.values().toString());
        return virtualApkMap.get(packageName);
    }
}
