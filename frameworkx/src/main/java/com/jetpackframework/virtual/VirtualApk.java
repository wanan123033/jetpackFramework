package com.jetpackframework.virtual;

import android.app.Application;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.jetpackframework.ContextUtil;
import com.jetpackframework.Reflector;
import com.jetpackframework.virtual.delegate.PackageParser;

import java.io.File;
import java.util.List;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class VirtualApk implements Parcelable {
    private VirtualManager virtualManager;
    private Context context;
    private File file;
    private File mNativeLibDir;
    private PackageInfo packageInfo;
    private String dexOutputPath;
    private Resources resources;
    private Instrumentation instrumentation;
    private DexClassLoader classLoader;
    private Application app;



    public VirtualApk(VirtualManager virtualManager, Context context, File file) throws Exception {
        this.virtualManager = virtualManager;
        this.context = context;
        this.file = file;
        instrumentation = getInstrumentation();
        packageInfo = context.getPackageManager().getPackageArchiveInfo(file.getAbsolutePath(),
                        PackageManager.GET_ACTIVITIES|PackageManager.GET_PROVIDERS|PackageManager.GET_SERVICES|PackageManager.GET_RECEIVERS
                        |PackageManager.GET_PERMISSIONS|PackageManager.GET_META_DATA|PackageManager.GET_INSTRUMENTATION|PackageManager.GET_SIGNATURES|PackageManager.GET_INTENT_FILTERS);
        this.mNativeLibDir = getDir(context, "valibs");
        File dexOutputDir = getDir(context,"dex");
        dexOutputPath = dexOutputDir.getAbsolutePath();
        classLoader = new DexClassLoader(file.getAbsolutePath(), dexOutputPath, mNativeLibDir.getAbsolutePath(), context.getClassLoader());
        SoInstaller.nativeLib(file,context,packageInfo,mNativeLibDir);
        Resources hostResources = context.getResources();
        AssetManager assetManager = createAssetManager(context, file);
        resources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
        Map<String, List<IntentFilter>> map = PackageParser.parserApk(file.getAbsolutePath());
        try {
            makeApplication(packageInfo.applicationInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (ActivityInfo info : packageInfo.receivers){
            BroadcastReceiver receiver = null;
            try {
                receiver = BroadcastReceiver.class.cast(classLoader.loadClass(info.name).newInstance());
                List<IntentFilter> intentFilters = map.get(info.packageName+"/"+info.name);
                for (IntentFilter intentFilter : intentFilters){
                    context.registerReceiver(receiver,intentFilter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void makeApplication(ApplicationInfo applicationInfo) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        String appClass = applicationInfo.className;
        if (appClass == null){
            appClass = "android.app.Application";
        }

        app = instrumentation.newApplication(getClassLoader(), appClass, context);
        instrumentation.callApplicationOnCreate(app);
    }

    protected AssetManager createAssetManager(Context context, File apk) {
        AssetManager am = context.getAssets();
        try {
            Reflector.with(am).method("addAssetPath", String.class).call(apk.getAbsolutePath());
        } catch (Reflector.ReflectedException e) {
            e.printStackTrace();
        }
        return am;
    }

    protected VirtualApk(Parcel in) {
        packageInfo = in.readParcelable(PackageInfo.class.getClassLoader());
        file = new File(in.readString());
        context = ContextUtil.get();
        virtualManager = VirtualManager.getInstance(ContextUtil.get());
        mNativeLibDir = new File(in.readString());
        dexOutputPath = in.readString();
        classLoader = new DexClassLoader(file.getAbsolutePath(), dexOutputPath, mNativeLibDir.getAbsolutePath(), context.getClassLoader());
        try {
            SoInstaller.nativeLib(file,context,packageInfo,mNativeLibDir);
            instrumentation = getInstrumentation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources hostResources = context.getResources();
        AssetManager assetManager = createAssetManager(context, file);
        resources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());
    }

    private Instrumentation getInstrumentation() throws Reflector.ReflectedException {
        Object activityThread = Reflector.on("android.app.ActivityThread").method("currentActivityThread").call();
        Instrumentation instrumentation = Reflector.with(activityThread).method("getInstrumentation").call();
        return instrumentation;
    }

    public static final Creator<VirtualApk> CREATOR = new Creator<VirtualApk>() {
        @Override
        public VirtualApk createFromParcel(Parcel in) {
            return new VirtualApk(in);
        }

        @Override
        public VirtualApk[] newArray(int size) {
            return new VirtualApk[size];
        }
    };

    public String getPackageName() {
        return packageInfo.packageName;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public ActivityInfo getActivityInfo(ComponentName component) {
        for (ActivityInfo info : packageInfo.activities) {
            Log.e("TAG----","info.packageName="+info.packageName+",info.name="+info.name);

            if (info.packageName.equals(component.getPackageName()) && info.name.equals(component.getClassName())){
                return info;
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(packageInfo, flags);
        dest.writeString(file.getAbsolutePath());
        dest.writeString(mNativeLibDir.getAbsolutePath());
        dest.writeString(dexOutputPath);
    }

    public DexClassLoader getClassLoader() {
        return (DexClassLoader) classLoader;
    }
    protected File getDir(Context context, String name) {
        return context.getDir(name, Context.MODE_PRIVATE);
    }

    public Resources getResources() {
        return resources;
    }

    public ServiceInfo getServiceInfo(ComponentName component) {
        for (ServiceInfo serviceInfo : packageInfo.services){
            if (serviceInfo.packageName.equals(component.getPackageName()) && serviceInfo.name.equals(component.getClassName())){
                return serviceInfo;
            }
        }
        return null;
    }

    public ResolveInfo resolveService(Intent target, int flags) {
        Log.e("TAG----",target.getComponent().toString());
        ResolveInfo info = new ResolveInfo();
        ComponentName componentName = target.getComponent();
        for (ServiceInfo serviceInfo : packageInfo.services){
            if (serviceInfo.packageName.equals(componentName.getPackageName()) && serviceInfo.name.equals(componentName.getClassName())){
                info.serviceInfo = serviceInfo;
            }
        }
        return info;
    }

    public ProviderInfo getProviderInfo(Uri contentUri) {
        Log.e("TAG----",contentUri.toString());
        String authority = contentUri.getAuthority();
        for (ProviderInfo info : packageInfo.providers){
            Log.e("TAG----",contentUri.getAuthority()+"----"+info.authority);
            if (info.authority.equals(authority)){
                return info;
            }
        }
        return null;
    }
}
