package com.jetpackframework.retrofit.converter;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.gwm.annotation.http.FileUpload;
import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.Header;
import com.gwm.annotation.http.HeaderString;
import com.gwm.annotation.http.Path;
import com.gwm.annotation.http.Query;
import com.gwm.annotation.http.QueryUrl;
import com.gwm.annotation.http.Url;
import com.jetpackframework.http.HttpUtil;
import com.jetpackframework.retrofit.Converter;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class PostFromRequestConverter implements Converter<PostFormBuilder> {
    private static Converter converter;
    public static synchronized Converter get(){
        if (converter == null){
            converter = new PostFromRequestConverter();
        }
        return converter;
    }
    private PostFromRequestConverter(){}
    @Nullable
    @Override
    public PostFormBuilder convert(Method method,Object[] args){
        PostFormBuilder builder = new PostFormBuilder();
        Header header = method.getAnnotation(Header.class);
        String url = null;
        if(method.isAnnotationPresent(HTTP.class)) {
            HTTP annotation = method.getAnnotation(HTTP.class);
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
                    if(annotations[i][j] instanceof Query) {
                        Query query = (Query) annotations[i][j];
                        String key = query.value();
                        Object value = args[i];
                        builder.addParams(key,String.valueOf(value));
                    }else if(annotations[i][j] instanceof FileUpload){
                        FileUpload fileUpload = (FileUpload) annotations[i][j];
                        String key = fileUpload.value();
                        File file = (File) args[i];
                        builder.addFile(key,file.getName(),file);
                    }else if(annotations[i][j] instanceof Url){
                        if(args[i] instanceof String){
                            url = (String) args[i];
                        }
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
        return builder.url(url);
    }
}
