package com.jetpackframework.ioc;

import android.app.Application;
import android.util.Log;

import com.jetpackframework.Reflector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
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

    }
    public static ARouterLayoutUtil getInstance() {
        if(instance == null){
            instance = new ARouterLayoutUtil();
        }
        return instance;
    }

    public void init(Application application) {
        layouts.clear();
        ServiceLoader<LayoutUtil> load = ServiceLoader.load(LayoutUtil.class);
        Iterator<LayoutUtil> iterator = load.iterator();
        for (;iterator.hasNext();){
            LayoutUtil next = iterator.next();
            try {
                Map<Integer,String> layouts = Reflector.with(next).field("layouts").get();
                Set<Integer> integers = layouts.keySet();
                for (Integer integer : integers){
                    this.layouts.put(integer,layouts.get(integer));
                }
            } catch (Reflector.ReflectedException e) {
                e.printStackTrace();
            }
        }
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
