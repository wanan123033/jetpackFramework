package com.jetpackframework.arouter;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.LogUtils;
import com.jetpackframework.ContextUtil;
import com.jetpackframework.base.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 路由器
 */
public class Router {
    private Activity baseActivity;
    private Intent intent;
    private Context context;
    private static Router router;
    private List<RouterInterceptor> routerInterceptors;
    private String routerUrl;
    private boolean interceptor = true;
    private String fromUrl;
    private Class toClass;
    private int enterAnim;
    private int exitAnim;

    private Router(){
        intent = new Intent();
        routerInterceptors = new ArrayList<>();
        this.context = ContextUtil.get();
    }

    public static synchronized Router getInstance(Activity activity){
        if (router == null){
            router = new Router();
        }
        router.baseActivity = activity;
        return router;
    }

    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, String value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }

    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, int value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, float value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, byte value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, long value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, short value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, byte[] value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, int[] value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, short[] value){
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, float[] value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, double[] value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, String[] value){
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, boolean value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, boolean[] value){
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, double value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, Serializable value){
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, Bundle value){
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, Parcelable value){
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }
    /**
     * 跳转时所要携带的参数
     * @param key 键
     * @param value 值
     */
    public Router from(String key, ArrayList<? extends Parcelable> value) {
        CacheDiskUtils.getInstance().put(key,value);
        return this;
    }


    /**
     *跳转拦截器
     * @param interceptor
     */
    public Router interceptor(RouterInterceptor interceptor){
        router.routerInterceptors.add(interceptor);
        return this;
    }

    public Router transition(int enterAnim, int exitAnim){
        router.enterAnim = enterAnim;
        router.exitAnim = exitAnim;
        return this;
    }

    /**
     * 跳转去哪里
     */
    public Router to(String uri){
        routerUrl = uri;
        RouterConnection connection = RouterConnection.getInstance();
//        if (!routerUrl.startsWith(connection.getMoudleName()+"://")){  //支持短链接
//            routerUrl = connection.getMoudleName()+"://"+connection.getPackageName()+"/"+routerUrl;
            LogUtils.d("ARouter routerUrl="+routerUrl);
//        }
        fromUrl = connection.getMoudleName()+"://"+connection.getPackageName()+"/"+fromUrl;
        LogUtils.d("ARouter fromUrl="+fromUrl);
        Class aClass = null;
        try {
            aClass = Class.forName(connection.getAction().get(routerUrl));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Set<String> queryParameterNames = UriCompact.getQueryParameterNames(uri);
        for (String key: queryParameterNames){
            Uri uu = Uri.parse(uri);
            from(key,uu.getQueryParameter(key));
        }
        if (aClass == null){
            RouterCallBack callBack = RouterConnection.getInstance().getCallback();
            callBack.notFound(context);
            return this;
        }
        intent.setClass(context,aClass);
        this.toClass = aClass;
        RouterConnection conn = RouterConnection.getInstance();
        List<RouterInterceptor> activityInterceptors = conn.getRouterInterceptors();
        if (activityInterceptors != null && !activityInterceptors.isEmpty() && interceptor){
            interceptor(activityInterceptors);
        }
        return this;
    }

    private void interceptor(List<RouterInterceptor> activityInterceptors) {
        this.routerInterceptors.addAll(activityInterceptors);
    }

    /**
     * 是否需要拦截该Uri
     * @param interceptor  true 拦截   false 不拦截
     */
    public Router interceptor(boolean interceptor){
        this.interceptor = interceptor;
        return this;
    }

    private void clear() {
        router.intent = null;
        router.routerInterceptors.clear();
        router.routerInterceptors = null;
        router.context = null;
        router.baseActivity = null;
        router = null;
    }

    public boolean isRouter(String uri){
        Map<String, String> action = RouterConnection.getInstance().getAction();
        return action.values().contains(uri);
    }

    /**
     * 跳转动作
     */
    public void router(){
        try {
            final boolean[] flag = {false};
            if (this.routerInterceptors != null && !this.routerInterceptors.isEmpty() && interceptor){
                for (RouterInterceptor interceptor : this.routerInterceptors){
                    flag[0] = interceptor.interceptor(routerUrl,fromUrl);
                    LogUtils.d(interceptor+"-----interceptor()-----"+ flag[0]);
                    if (flag[0]){
                        break;
                    }
                }
            }
            if (!flag[0]) {
                baseActivity.startActivity(intent);
                if (enterAnim != 0 && exitAnim != 0){
                    baseActivity.overridePendingTransition(enterAnim,exitAnim);
                }
            }
            clear();
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
            RouterCallBack callBack = RouterConnection.getInstance().getCallback();
            callBack.notFound(context);
        }catch (Exception ex){
            ex.printStackTrace();
            RouterCallBack callBack = RouterConnection.getInstance().getCallback();
            callBack.onError(ex);
        }
    }

    /**
     * 跳转动作
     */
    public void routerForResult(int reqCode){
        try {
            boolean flag = false;
            if (this.routerInterceptors != null && !this.routerInterceptors.isEmpty() && interceptor){
                for (RouterInterceptor interceptor : this.routerInterceptors){
                    flag = interceptor.interceptor(routerUrl,fromUrl);
                    LogUtils.e(interceptor+"-----interceptor()-----"+flag);
                    if (flag){
                        break;
                    }
                }
            }
            if (!flag) {
                baseActivity.startActivityForResult(intent, reqCode);
            }
            clear();
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
            RouterCallBack callBack = RouterConnection.getInstance().getCallback();
            callBack.notFound(context);
        }catch (Exception ex){
            ex.printStackTrace();
            RouterCallBack callBack = RouterConnection.getInstance().getCallback();
            callBack.onError(ex);
        }
    }

    /*************************************************************Fragment****************************************/
    public void routerFragment(FragmentManager fm, int resId){
        BaseFragment fragment = null;
        try {
            fragment = (BaseFragment) toClass.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean flag = false;
        if (this.routerInterceptors != null && !this.routerInterceptors.isEmpty() && interceptor){
            for (RouterInterceptor interceptor : this.routerInterceptors){
                flag = interceptor.interceptor(routerUrl,fromUrl);
                LogUtils.e(interceptor+"-----interceptor()-----"+flag);
                if (flag){
                    break;
                }
            }
        }
        if (!flag) {
            fragment.setArguments(null);
            fm.beginTransaction().replace(resId,fragment).commit();
        }
        clear();
    }
}