package com.jetpackframework.arouter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ARouterUtil {
    public static List<Class<?>> foreachClass(Context context, String name) {
        List<Class<?>> classes = new ArrayList<>();
        int array = context.getResources().getIdentifier(name, "array", context.getPackageName());
        String[] stringArray = context.getResources().getStringArray(array);
        for (String clazzName : stringArray){
            try {
                classes.add(Class.forName(clazzName));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

}
