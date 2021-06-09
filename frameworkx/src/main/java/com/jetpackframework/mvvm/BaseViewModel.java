package com.jetpackframework.mvvm;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * 采用观察者设计模式访问数据 并监听Activity,Fragment等的生命周期
 */
public class BaseViewModel<T> extends ViewModel<T>{
    private MutableLiveData<T> liveData;

    public BaseViewModel(){
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<T> getLiveData() {
        return liveData;
    }

    protected void setValue(T data){
        liveData.setValue(data);
    }

    protected void postValue(T data){
        liveData.postValue(data);
    }

    protected void postValue(MutableLiveData<T> data){
        data.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                liveData.postValue(t);
            }
        });
        data.postValue(data.getValue());
    }
    protected void setValue(MutableLiveData<T> data){
        data.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                liveData.setValue(t);
            }
        });
        data.setValue(data.getValue());
    }


}
