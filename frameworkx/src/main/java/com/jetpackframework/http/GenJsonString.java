package com.jetpackframework.http;



import com.gwm.annotation.http.JSON;
import com.gwm.annotation.http.Param;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class GenJsonString implements InvocationHandler {
    private static GenJsonString genJsonString;
    private GenJsonString(){}
    public static synchronized GenJsonString getInstance(){
        if (genJsonString == null){
            genJsonString = new GenJsonString();
        }
        return genJsonString;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(JSON.class)){
            JSONObject obj = new JSONObject();
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {     //i:第几个参数的注解
                if (annotations[i].length > 0) {
                    for (int j = 0; j < annotations[i].length; j++) {  //j: 第i个参数上的第j个注解
                        if (annotations[i][j] instanceof Param) {
                            Param query = (Param) annotations[i][j];
                            String key = query.value();
                            Object value = args[i];
                            obj.put(key,value);
                        }
                    }
                }
            }
            if (method.getReturnType().getName().equals("JSONObject")){
                return obj;
            }
            return obj.toString();
        }else {
            throw new IllegalArgumentException("This method does not use JSON annotation");
        }
    }
}
