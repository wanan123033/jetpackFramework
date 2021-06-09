package com.jetpackframework.retrofit.converter;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.Header;
import com.gwm.annotation.http.HeaderString;
import com.gwm.annotation.http.JSON;
import com.gwm.annotation.http.Path;
import com.gwm.annotation.http.QueryUrl;
import com.jetpackframework.http.HttpUtil;
import com.jetpackframework.retrofit.Converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class JsonRequestConverter implements Converter<Request> {
    private static Converter converter;
    public static synchronized Converter get(){
        if (converter == null){
            converter = new JsonRequestConverter();
        }
        return converter;
    }
    private JsonRequestConverter(){}
    @Nullable
    @Override
    public Request convert(Method method,Object[] args) {
        Request.Builder builder = new Request.Builder();
        RequestBody body = null;
        Header header = method.getAnnotation(Header.class);
        String url = null;
        HTTP annotation = method.getAnnotation(HTTP.class);
        if(method.isAnnotationPresent(HTTP.class)) {
            url = annotation.url();
        }
        String baseUrl = HttpUtil.baseUrl;
        if (!TextUtils.isEmpty(baseUrl)) {
            if (!url.startsWith("http")) {
                url = baseUrl + url;
            }
        }
        if (header != null){
            String[] strings = header.value();
            for (String string : strings){
                String keyvalue[] = string.split(":");
                builder.addHeader(keyvalue[0],keyvalue[1]);
            }
        }
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {     //i:第几个参数的注解
            if (annotations[i].length > 0) {
                for (int j = 0; j < annotations[i].length; j++) {  //j: 第i个参数上的第j个注解
                    if (annotations[i][j] instanceof JSON){
                        body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (String) args[i]);
                    }else if (annotations[i][j] instanceof QueryUrl){
                        String key = ((QueryUrl)annotations[i][j]).value();
                        Object value = args[i];
                        if (url != null){
                            if (url.contains("?")){
                                url += ("&&" + key + "=" + value);
                            }else {
                                url += ("?" + key + "=" + value);
                            }
                        }
                    }else if (annotations[i][j] instanceof Path){
                        String value = ((Path)annotations[i][j]).value();
                        url = url.replaceAll("\\{"+value+"\\}", args[i]+"");
                    }else if (annotations[i][j] instanceof HeaderString){
                        String key = ((HeaderString)annotations[i][j]).value();
                        String value = (String) args[i];
                        builder.addHeader(key,value);
                    }
                }
            }
        }
        if (annotation.way() == HTTP.WAY.DELETE){
            builder = builder.delete(body);
        }else if (annotation.way() == HTTP.WAY.POST){
            builder = builder.post(body);
        }else if (annotation.way() == HTTP.WAY.PUT){
            builder = builder.put(body);
        }
        return builder.url(url).build();
    }
}
