package com.jetpackframework.mvvm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.jetpackframework.ContextUtil;


/**
 * 监听网络变化
 */
public class NetworkLiveData extends LiveData<NetworkInfo> {

    private final Context mContext;
    static NetworkLiveData mNetworkLiveData;
    private NetworkReceiver mNetworkReceiver;
    private final IntentFilter mIntentFilter;

    private static final String TAG = "NetworkLiveData";

    private NetworkLiveData(Context context) {
        mContext = context.getApplicationContext();
        mNetworkReceiver = new NetworkReceiver();
        mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    private static synchronized NetworkLiveData getInstance(Context context) {
        if (mNetworkLiveData == null) {
            mNetworkLiveData = new NetworkLiveData(context);
        }
        return mNetworkLiveData;
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive:");
        mContext.registerReceiver(mNetworkReceiver, mIntentFilter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "onInactive: ");
        mContext.unregisterReceiver(mNetworkReceiver);
    }

    private static class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            getInstance(context).setValue(activeNetwork);

        }
    }

    public static void observer(LifecycleOwner owner,Observer<NetworkInfo> observer){
        NetworkLiveData.getInstance(ContextUtil.get()).observe(owner,observer);
    }
}