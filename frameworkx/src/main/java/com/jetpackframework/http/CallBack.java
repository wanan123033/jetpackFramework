package com.jetpackframework.http;


import com.google.gson.Gson;
import com.jetpackframework.retrofit.ResponseBodyConverter;
import com.jetpackframework.retrofit.converter.GsonResponseBodyConverter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class CallBack<T> extends com.zhy.http.okhttp.callback.Callback<T> implements okhttp3.Callback {

    private HttpTask<T> httpParams;

    public CallBack(HttpTask<T> httpParams) {
        this.httpParams = httpParams;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (httpParams.observer != null){
            httpParams.observer.onError(e,0);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        T t = parseNetworkResponse(response,0);
        onResponse(t,0);
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws IOException {
        if (HttpUtil.getInstance().responseConverter.isEmpty()){
            HttpUtil.getInstance().addResponseConverter(new GsonResponseBodyConverter(new Gson()));
        }
        for (ResponseBodyConverter converter : HttpUtil.getInstance().responseConverter){
            converter.setClass(httpParams.result);
            return (T) converter.convert(response);
        }
        return null;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        if (httpParams.observer != null){
            httpParams.observer.onError(e,0);
        }
    }

    @Override
    public void onResponse(T response, int id) {
        httpParams.observer.onNext(response,id);
    }
}
