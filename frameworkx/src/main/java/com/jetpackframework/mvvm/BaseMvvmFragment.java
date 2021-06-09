package com.jetpackframework.mvvm;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jetpackframework.base.BaseFragment;
import com.jetpackframework.ioc.IViewBind;


public abstract class BaseMvvmFragment<M, VM extends ViewModel<M>,V extends IViewBind> extends BaseFragment<V> implements Observer<M>,IViewModelProvider {
    protected VM viewModel;
    private ViewModelProvider viewModelProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(getViewModelClass());
        getLifecycle().addObserver(viewModel);

    }

    @Override
    public ViewModelProvider getViewModelProvider() {
        return viewModelProvider;
    }

    protected abstract Class<VM> getViewModelClass();

    @Override
    public void onChanged(M m) {

    }
}
