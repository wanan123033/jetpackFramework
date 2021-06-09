package com.jetpackframework.arouter;

import android.graphics.Bitmap;
import android.os.Parcelable;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.gwm.annotation.router.RouterField;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;


public abstract class BaseRouterInterceptor implements RouterInterceptor {
    @Override
    public boolean interceptor(String toUri, String fromUrl) {
        readRouterData();
        return intercept(toUri,fromUrl);
    }

    protected abstract boolean intercept(String toUri, String fromUrl);

    private void readRouterData() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields){
            if (field.isAnnotationPresent(RouterField.class)){
                field.setAccessible(true);
                RouterField routerField = field.getAnnotation(RouterField.class);
                Class<?> fieldClass = routerField.fieldClass();
                Object o = null;
                if (fieldClass == String.class){
                    o = CacheDiskUtils.getInstance().getString(routerField.value());
                }else if (fieldClass == int.class || fieldClass == byte.class || fieldClass == short.class || fieldClass == long.class
                        || fieldClass == float.class || fieldClass == double.class || fieldClass == boolean.class ||
                        fieldClass == char.class || fieldClass == int[].class || fieldClass == short[].class || fieldClass == long[].class ||
                        fieldClass == boolean[].class || fieldClass == char[].class || fieldClass == String[].class || fieldClass == float[].class ||
                        fieldClass == double[].class || fieldClass == Serializable.class || fieldClass == ArrayList.class){
                    o = CacheDiskUtils.getInstance().getSerializable(routerField.value());
                }else if (fieldClass == byte[].class){
                    o = CacheDiskUtils.getInstance().getBytes(routerField.value());
                }else if (fieldClass == Bitmap.class){
                    o = CacheDiskUtils.getInstance().getBitmap(routerField.value());
                }else if (fieldClass == Parcelable.class){
                    try {
                        Field creator = field.getType().getField("CREATOR");
                        Object creatorObj = creator.get(null);
                        o = CacheDiskUtils.getInstance().getParcelable(routerField.value(), (Parcelable.Creator<Object>) creatorObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    field.set(this,o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
