package com.jetpackframework.ioc;

import android.util.Log;

import com.jetpackframework.ContextUtil;
import com.jetpackframework.arouter.ARouterUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解决EventClassUtil在组件化架构中的合并问题
 */
public class ARouterEventClassUtil implements EventClassUtil{
    private HashMap<String,IEventClass> eventArr;
    private static final List<Class<?>> com;
    public static ARouterEventClassUtil instance;

    static {
        com = ARouterUtil.foreachClass(ContextUtil.get(),"events");
    }
    public static ARouterEventClassUtil getInstance() {
        if(instance == null){
            instance = new ARouterEventClassUtil();
        }
        return instance;
    }

    @Override
    public IEventClass getEventClass(Class simpleName) {
        if (eventArr == null){
            eventArr = new HashMap<>();
        }
        String key = simpleName.getPackage().getName()+"."+simpleName.getSimpleName();
        Log.e("TAG","key="+key);
        IEventClass eventClass = eventArr.get(key);
        if (eventClass == null){
            for (Class clazz : com){
                try {
                    EventClassUtil util = (EventClassUtil) clazz.getMethod("getInstance").invoke(null);
                    eventClass = util.getEventClass(simpleName);
                    if (eventClass != null){
                        eventArr.put(key,eventClass);
                        break;
                    }
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return eventClass;
    }

    @Override
    public void clear() {
        eventArr.clear();
    }
}
