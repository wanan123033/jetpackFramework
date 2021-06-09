package com.jetpackframework.retrofit;


import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.LogUtils;
import com.jetpackframework.http.HttpObserver;
import com.jetpackframework.rxjetpack.Observable;
import com.jetpackframework.rxjetpack.thread.Schedules;

import okhttp3.WebSocketListener;

/**
 * Created by Administrator on 2018/1/25.
 *
 * Http 请求基类(动态代理模式)
 */
public abstract class BaseDataPresenter<H,D> extends WebSocketListener implements HttpObserver<D> {
    private IRetrofitUtil<H> retrofitOKHttp;
    private H httpApi;
    private ViewModel viewModel;
    public BaseDataPresenter(){
        retrofitOKHttp = getRetrifit();
        httpApi = retrofitOKHttp.create();
    }

    protected abstract IRetrofitUtil<H> getRetrifit();

    protected void addHttpSubscriber(Observable<D> observable){
        if (observable instanceof ObservableHttp){
            ((ObservableHttp<D>) observable).httpObserver(this);
        }
        toSubscribe(observable);
    }

    protected void addWebSocketSubscriber(Observable<D> observable){
        if (observable instanceof ObservableHttp){
            ((ObservableHttp<D>) observable).webSocketListener(this);
        }
        toSubscribe(observable);
    }

    @Override
    public void onError(Exception e, int id) {
        onErrorResult(e,id);
        LogUtils.e(e);
    }

    @Override
    public void onNext(D response, int id) {
        onNextResult(response,id);
    }
    protected H getHttpPresenter(){
        return httpApi;
    }

    private <D> void toSubscribe(Observable<D> o) {
        o.observeOn(Schedules.io())
                .subscribeOn(Schedules.androidMainThread())
                .subscribe();
    }
    protected abstract  void onNextResult(D response, int id);

    protected abstract void onErrorResult(Exception e, int id);

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
