package com.jetpackframework.mvvm;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jetpackframework.base.BaseActivity;
import com.jetpackframework.ioc.IViewBind;


/**
 *    1.MVVM设计模式
 *
 * @param <M>
 * @param <VM>
 * @param <V>
 */
public abstract class BaseMvvmActivity<M,VM extends ViewModel<M>,V extends IViewBind> extends BaseActivity<V> implements Observer<M>,IViewModelProvider {
    protected VM viewModel;
    private ViewModelProvider viewModelProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(getViewModelClass());
        getLifecycle().addObserver(viewModel);
    }

    public ViewModelProvider getViewModelProvider(){
        return viewModelProvider;
    }

    protected abstract Class<VM> getViewModelClass();

    @Override
    public void onChanged(M o) {

    }
}
