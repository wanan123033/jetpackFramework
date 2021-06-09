package com.jetpackframework.virtual;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.jetpackframework.Reflector;


public class VirtualInstallService extends Service {
    public static final String FILEPATH = "FILEPATH";
    private VirtualManager mManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mManager = VirtualManager.getInstance(getApplication());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return virtualAidl;
    }

    private IBinder virtualAidl = new VirtualAidl.Stub() {
        @Override
        public String install(String apkPath, IVirtualInstallListenner listenner) throws RemoteException {
            try {
                return mManager.install(apkPath,listenner);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public PackageInfo getPackageInfo(String packageName) throws RemoteException {
            return mManager.getPackageInfo(packageName);
        }

        @Override
        public VirtualApk getVirtualApk(String packageName) throws RemoteException {
            return mManager.getVirtualApk(packageName);
        }
    };
}
