package com.jetpackframework.mvvm;

import androidx.lifecycle.LifecycleOwner;

import java.util.List;

/**
 *  多个ViewModel之间的switch选择
 * @param <T>
 */
public abstract class BaseSwitchLiveDataViewModel<T> extends BaseMediatorLiveDataViewModel<T>{

    @Override
    protected void onCreate(LifecycleOwner owner) {
        super.onCreate(owner);
    }

    @Override
    protected T getData(Object o) {
        return null;
    }

    @Override
    protected abstract List<Class<? extends BaseViewModel>> getViewModels();
}
