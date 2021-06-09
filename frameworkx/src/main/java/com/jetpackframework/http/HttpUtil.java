package com.jetpackframework.http;

import android.util.Log;

import com.jetpackframework.retrofit.ResponseBodyConverter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    private static HttpUtil instance;
    private ExecutorService executor;
    private MyRunnable runnable = new MyRunnable(this);
    public static String baseUrl;
    public List<ResponseBodyConverter<String,?>> responseConverter;

    private <T> void execHttp(HttpTask<T> task) {
        if (task.getBuilder != null){
            execute(task,task.getBuilder);
        }else if (task.request != null){
            execute(task.request,task);
        }else if (task.postFormBuild != null){
            execute(task,task.postFormBuild);
        }else {
            executeWebSocket(task);
        }
    }

    private void executeWebSocket(HttpTask task) {
        Request request = getRequest(task);
        OkHttpUtils.getInstance().getOkHttpClient().newWebSocket(request,task.webSocketListener);
    }

    private HttpUtil(){
        executor = Executors.newCachedThreadPool();
        responseConverter = new ArrayList<>();
    }

    public synchronized static HttpUtil getInstance(){
        if (instance == null){
            instance = new HttpUtil();
        }
        return instance;
    }

    public void sendHttp(HttpTask task){
        //加入缓存
        HttpCache.getCache().add(task);
        //执行缓存中的http
        execHttp();

    }

    public void init(OkHttpClient okHttpClient){
        OkHttpUtils.initClient(okHttpClient);
    }

    private void execHttp() {
        if (!runnable.isExit)
            executor.execute(runnable);
    }

    private <T> void execute(final Request request, final HttpTask<T> httpParams) {
        Log.e("TAG",httpParams.toString());
        if (httpParams.netListener != null)
            httpParams.netListener.onBefore(request,0);
        OkHttpUtils.getInstance().getOkHttpClient().newCall(request).enqueue(new CallBack<T>(httpParams));
    }

    private Request getRequest(HttpTask params) {
        Request.Builder builder = new Request.Builder();
        builder.url(params.url);
        return builder.build();
    }
    private <T> void execute(final HttpTask<T> httpParams, OkHttpRequestBuilder build) {
        build.build().execute(new CallBack<T>(httpParams));
    }

    public void baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private static final class MyRunnable implements Runnable{
        private HttpUtil httpUtil;
        public boolean isExit = false;

        public MyRunnable(HttpUtil httpUtil) {
            this.httpUtil = httpUtil;
        }

        @Override
        public void run() {
            while(true){
                if (HttpCache.getCache().hasNext()){
                    isExit = true;
                    HttpTask task = HttpCache.getCache().getTask();
                    Log.e("HTTPTASK",task.toString());
                    httpUtil.execHttp(task);
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    isExit = false;
                    return;
                }
            }
        }
    }
    public void close(){
        executor.shutdown();
        instance = null;
    }
    public void addResponseConverter(ResponseBodyConverter<String,?> converter){
        responseConverter.add(converter);
    }
}
