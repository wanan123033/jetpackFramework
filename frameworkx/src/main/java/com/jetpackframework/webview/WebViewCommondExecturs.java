package com.jetpackframework.webview;

import android.os.RemoteException;

import com.WebViewCommondInterface;

import java.util.HashMap;
import java.util.Map;

public class WebViewCommondExecturs extends WebViewCommondInterface.Stub {
    private static WebViewCommondExecturs wcexec;
    public Map<String,IWebViewCommond> mCommonds;

    private WebViewCommondExecturs(){
        mCommonds = new HashMap<>();
    }
    public static synchronized WebViewCommondExecturs getInstance() {
        if (wcexec == null){
            wcexec = new WebViewCommondExecturs();
        }
        return wcexec;
    }

    @Override
    public void execWebViewCommond(String commond, String params) throws RemoteException {
        if (mCommonds == null || mCommonds.isEmpty()){
        }else {
            mCommonds.get(commond).execCommond(params);
        }
    }
}
