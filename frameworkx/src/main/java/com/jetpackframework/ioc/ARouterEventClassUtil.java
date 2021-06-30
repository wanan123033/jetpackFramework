package com.jetpackframework.ioc;

import android.app.Application;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 解决EventClassUtil在组件化架构中的合并问题
 */
public class ARouterEventClassUtil implements EventClassUtil{
    private HashMap<String,IEventClass> eventArr;
    private List<Class<?>> com;
    public static ARouterEventClassUtil instance;

    public static ARouterEventClassUtil getInstance() {
        if(instance == null){
            instance = new ARouterEventClassUtil();
        }
        return instance;
    }
    public void init(Application application) {
        if (com == null){
            com = new ArrayList<>();
        }
        com.clear();
        ServiceLoader<EventClassUtil> load = ServiceLoader.load(EventClassUtil.class);
        Iterator<EventClassUtil> iterator = load.iterator();
        while (iterator.hasNext()){
            EventClassUtil util = iterator.next();
            com.add(util.getClass());
        }
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
