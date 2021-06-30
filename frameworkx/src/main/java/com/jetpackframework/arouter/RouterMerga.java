package com.jetpackframework.arouter;

import android.content.Context;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public class RouterMerga implements RouterInitialization{
    @Override
    public void onInit(Map<String, String> routerMap) {
        ServiceLoader<RouterInitialization> loader = ServiceLoader.load(RouterInitialization.class);
        Iterator<RouterInitialization> iterator = loader.iterator();
        while (iterator.hasNext()){
            iterator.next().onInit(routerMap);
        }
    }

    @Override
    public void notFound(Context context) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
