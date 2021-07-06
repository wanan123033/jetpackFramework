package com.jetpackframework.http;

import android.text.TextUtils;

import com.gwm.annotation.http.HTTP;
import com.jetpackframework.base.BaseActivity;
import com.jetpackframework.databus.DataBus;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import okhttp3.Request;
import okhttp3.WebSocketListener;

public class HttpTask<T> {
    public String url;
    public HTTP.WAY method;
    public Request request;
    public PostFormBuilder postFormBuild;
    public GetBuilder getBuilder;
    public HttpObserver<T> observer; //控制器，http请求会将服务器返回的数据传递给该接口
    public Class<T> result;
    public HttpNetListener netListener = new HttpNetListener();  //网络请求对话框
    public WebSocketListener webSocketListener;
    public String message;                 //网络消息对话框提示信息
    public class HttpNetListener{

        /**
         * UI Thread
         *response.headers()
         * @param request
         */
        void onBefore(Request request, int id){
            if (!TextUtils.isEmpty(message))
                DataBus.getDefault().with(BaseActivity.SHOW_PROGRESS).postValue(message);
        }

        /**
         * UI Thread
         *
         * @param
         */
        void onAfter(int id){
            if (!TextUtils.isEmpty(message))
                DataBus.getDefault().with(BaseActivity.DIMMSION_PROGREESS).postValue(message);
        }

        /**
         * UI Thread
         *
         * @param progress
         */
        void inProgress(float progress, long total, int id){

        }
    }

    @Override
    public String toString() {
        return "HttpTask{" +
                "url='" + url + '\'' +
                ", method=" + method +
                ", request=" + request +
                ", postFormRequest=" + postFormBuild +
                ", getBuilder=" + getBuilder +
                ", observer=" + observer +
                ", result=" + result +
                ", netListener=" + netListener +
                ", webSocketListener=" + webSocketListener +
                ", message='" + message + '\'' +
                '}';
    }
}
