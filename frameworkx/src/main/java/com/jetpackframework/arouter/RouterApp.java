package com.jetpackframework.arouter;

import com.gwm.annotation.router.Module;
import com.jetpackframework.applicationdelegate.ApplicationDelegate;

public class RouterApp {

    /**
     * 初始化Router路由
     * @param initialization
     */
    public static void init(RouterInitialization initialization){
        RouterConnection conn = RouterConnection.getInstance();
        conn.setCallBackBack(initialization);
        initialization.onInit(conn.getAction());
    }

    /**
     * 添加全局跳转拦截器
     * @param interceptor
     */
    public static void interceptor(RouterInterceptor interceptor){
        RouterConnection.getInstance().getRouterInterceptors().add(interceptor);
    }
}
