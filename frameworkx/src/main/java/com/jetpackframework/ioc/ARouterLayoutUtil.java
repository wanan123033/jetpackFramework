package com.jetpackframework.ioc;

import android.util.Log;

import com.jetpackframework.ContextUtil;
import com.jetpackframework.arouter.ARouterUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 解决LayoutUtil在组件化架构中的合并问题
 */
public class ARouterLayoutUtil implements LayoutUtil{
    private static final Map<Integer,String> layouts;
    private Map<Integer,IViewBind> layoutArr;

    public static ARouterLayoutUtil instance;
    static {
        layouts = new HashMap<>();
        layouts.clear();
        List<Class<?>> com = ARouterUtil.foreachClass(ContextUtil.get(),"layouts");
        for (Class clazz : com){
            try {
                Field events1 = clazz.getDeclaredField("layouts");
                events1.setAccessible(true);
                Method getInstance = clazz.getMethod("getInstance");
                Object obj = getInstance.invoke(null);
                Map<Integer,String> o = (Map<Integer,String>) events1.get(obj);
                Set<Integer> integers = o.keySet();
                for (Integer integer : integers){
                    layouts.put(integer,o.get(integer));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static ARouterLayoutUtil getInstance() {
        if(instance == null){
            instance = new ARouterLayoutUtil();
        }
        return instance;
    }

    @Override
    public void clear() {
        layoutArr.clear();
        layouts.clear();
    }

    @Override
    public synchronized <V extends IViewBind> V getViewBind(int layoutId) {
        if(layoutArr == null){
            layoutArr = new HashMap<>();
        }
        IViewBind bind = layoutArr.get(layoutId);
        if(bind == null){
            Log.e("TAG","layouts="+layouts);
            try {
                String s = layouts.get(layoutId);
                bind = (IViewBind) Class.forName((String)layouts.get(layoutId)).newInstance();
                Log.e("TAG","layoutId="+layoutId+","+"className="+s+",bind="+bind);
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            layoutArr.put(layoutId,bind);
        }
        return (V) bind;
    }
}
