package com.jetpackframework.virtual.delegate;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jetpackframework.ContextUtil;
import com.jetpackframework.virtual.VirtualApk;
import com.jetpackframework.virtual.VirtualInstaller;

import java.util.HashMap;
import java.util.Map;

public class RemoteContentProvider extends ContentProvider {
    private static final String PKG_KEY = "pkg";

    private Map<String,ContentProvider> providerMap = new HashMap<>();

    /**
     * 通过宿主中转此Uri
     * @param uri  需要中转的uri
     * @return
     */
    public static Uri getRemoteUri(Uri uri,String packageName){
        String authority = uri.getAuthority();
        String path = uri.getPath();
        return Uri.parse("content://com.virtual.remote/"+authority+path+"?"+PKG_KEY+"="+packageName);
    }

    private Uri getContentUri(Uri uri){
        return Uri.parse("content:/"+uri.getPath());
    }
    private ContentProvider getContentProvider(Uri uri) throws RemoteException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String packageName = uri.getQueryParameter(PKG_KEY);
        Log.e("TAG","packageName="+packageName);
        ContentProvider contentProvider = providerMap.get(packageName + "/" + uri.getPath());
        if (contentProvider != null){
            return contentProvider;
        }
        synchronized (providerMap) {
            VirtualApk apk = VirtualInstaller.getInstance(ContextUtil.get()).getVirtualApk(packageName);
            Log.e("TAG","apk="+apk);
            if (apk != null) {
                ProviderInfo info = apk.getProviderInfo(getContentUri(uri));
                Log.e("TAG","info="+info);
                if (info != null) {
                    ContentProvider provider = (ContentProvider) apk.getClassLoader().loadClass(info.name).newInstance();
                    providerMap.put(packageName + "/" + uri.getPath(), provider);
                    return provider;
                }
            }
        }
        return null;
    }
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        try {
            return getContentProvider(uri).query(getContentUri(uri),projection,selection,selectionArgs,sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        try {
            return getContentProvider(uri).getType(getContentUri(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        try {
            return getContentProvider(uri).insert(getContentUri(uri),values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            return getContentProvider(uri).delete(getContentUri(uri),selection,selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            return getContentProvider(uri).update(getContentUri(uri),values,selection,selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
