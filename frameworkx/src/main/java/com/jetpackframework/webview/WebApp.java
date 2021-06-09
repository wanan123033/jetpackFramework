package com.jetpackframework.webview;



import com.jetpackframework.ContextUtil;

import java.lang.reflect.Method;

public abstract class WebApp {

    protected <T extends IWebViewCommond> void addCommond(T commond){
        WebViewCommondExecturs.getInstance().mCommonds.put(commond.getName(),commond);
    }
    public static void init(boolean isHook){
        try {
            Class clazz = Class.forName("com.app.webview.WebAppInterface");
            Object newInstance = clazz.newInstance();
            Method method = clazz.getMethod("initWebView");
            method.invoke(newInstance,new Object[]{});
            if (isHook){
                WebViewServiceManager.initConnection(ContextUtil.get());
            }
        }catch (Exception e){

        }
    }
    public static void init(){
        init(false);
    }
    protected abstract void initWebView();

}
