package com.jetpackframework.retrofit;



import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.JSONRequest;
import com.gwm.annotation.http.PostFormRequest;
import com.gwm.annotation.http.RequestBody;
import com.gwm.annotation.http.WebSocket;
import com.jetpackframework.http.HttpTask;
import com.jetpackframework.retrofit.converter.GetBuilderRequestConverter;
import com.jetpackframework.retrofit.converter.JsonRequestConverter;
import com.jetpackframework.retrofit.converter.PostFromRequestConverter;
import com.jetpackframework.retrofit.converter.RequestBodyRequestConverter;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/11/24.
 */

public class HttpInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HttpTask params = addNetworkParams(method, args);
        Type type = method.getGenericReturnType();// 获取返回值类型
        if (type instanceof ParameterizedType) { // 判断获取的类型是否是参数类型
            Type[] typesto = ((ParameterizedType) type).getActualTypeArguments();// 强制转型为带参数的泛型类型，
            // getActualTypeArguments()方法获取类型中的实际类型，如map<String,Integer>中的
            // String，integer因为可能是多个，所以使用数组
            for (Type type2 : typesto) {
                params.result = Class.forName(type2.toString().substring(6));
            }
        }
        return ObservableHttp.http(params);
    }

    protected HttpTask addNetworkParams(Method method, Object[] args){
        HttpTask params = new HttpTask();
        if(method.isAnnotationPresent(HTTP.class)) {
            HTTP annotation = method.getAnnotation(HTTP.class);
            params.method = annotation.way();
            params.message = annotation.message();
            Converter converter = getConverter(method);
            if (method.isAnnotationPresent(PostFormRequest.class)){
                params.postFormBuild = (PostFormBuilder) converter.convert(method,args);
            }else if (method.isAnnotationPresent(JSONRequest.class) || method.isAnnotationPresent(RequestBody.class)){
                params.request = (Request) converter.convert(method,args);
            }else if (params.method == HTTP.WAY.GET){
                params.getBuilder = (GetBuilder) converter.convert(method,args);
            }
        }else if (method.isAnnotationPresent(WebSocket.class)){
            WebSocket webSocket = method.getAnnotation(WebSocket.class);
            params.url = webSocket.value();
        }else {
            throw new IllegalArgumentException("Unknown method annotation");
        }
        return params;
    }

    private Converter getConverter(Method method) {
        if (method.isAnnotationPresent(PostFormRequest.class)){
            return PostFromRequestConverter.get();
        }else if (method.isAnnotationPresent(JSONRequest.class)){
            return JsonRequestConverter.get();
        }else if (method.isAnnotationPresent(RequestBody.class)){
            return RequestBodyRequestConverter.get();
        }else if (method.isAnnotationPresent(HTTP.class)){
            return GetBuilderRequestConverter.get();
        }
        return null;
    }
}
