package com.jetpackframework.retrofit;

import com.jetpackframework.http.HttpUtil;

import java.lang.reflect.Proxy;

import okhttp3.OkHttpClient;

public class Retrofit {
    public <T> T create(Class<T> clazz){
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{clazz},new HttpInvocationHandler());
    }
    public static class Builder{
        private String baseUrl;

        public Builder client(OkHttpClient client) {
            HttpUtil.getInstance().init(client);
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            HttpUtil.getInstance().baseUrl(baseUrl);
            return this;
        }
        public Builder addResponseConverter(ResponseBodyConverter<String,?> converter){
            HttpUtil.getInstance().addResponseConverter(converter);
            return this;
        }
        public Retrofit builder() {
            return new Retrofit();
        }
    }

}
