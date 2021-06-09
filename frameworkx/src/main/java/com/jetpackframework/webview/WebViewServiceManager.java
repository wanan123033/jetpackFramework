package com.jetpackframework.webview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.WebViewCommondInterface;

public class WebViewServiceManager implements ServiceConnection {
    private static WebViewServiceManager wsm;
    private WebViewCommondInterface webViewCommondInterface;
    public static synchronized WebViewServiceManager getInstance() {
        if (wsm == null){
            wsm = new WebViewServiceManager();
        }
        return wsm;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        webViewCommondInterface = WebViewCommondInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        webViewCommondInterface = null;
    }

    public void handleWebViewCommond(WebViewCommond commond) {
        if (webViewCommondInterface == null){
            Log.e("TAG","WebViewService service not Found!");
        }else {
            try {
                webViewCommondInterface.execWebViewCommond(commond.commond,commond.param.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initConnection(Context context){
        Intent intent = new Intent(context,WebViewService.class);
        context.bindService(intent,WebViewServiceManager.getInstance(),Context.BIND_AUTO_CREATE);
    }

}
