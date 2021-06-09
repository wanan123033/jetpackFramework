package com.jetpackframework.arouter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由管道
 */
public class RouterConnection {
    private Map<String,String> mActions;   //路由池
    private static RouterConnection instance;
    private RouterCallBack callback;
    private List<RouterInterceptor> routerInterceptors;  //Activity拦截器集合
    private String packageName;
    private String moudleName;

    private RouterConnection(){
        mActions = new HashMap<>();
        routerInterceptors = new ArrayList<>();
    }

    public static synchronized RouterConnection getInstance(){
        if (instance == null){
            instance = new RouterConnection();
        }
        return instance;
    }
    public Map<String,String> getAction(){
        return mActions;
    }

    public void clear() {
        mActions.clear();
    }

    public void setCallBackBack(RouterCallBack callBack) {
        this.callback = callBack;
    }

    public RouterCallBack getCallback() {
        return callback;
    }

    public List<RouterInterceptor> getRouterInterceptors() {
        return routerInterceptors;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMoudleName() {
        return moudleName;
    }

    public void setMoudleName(String moudleName) {
        this.moudleName = moudleName;
    }
}
