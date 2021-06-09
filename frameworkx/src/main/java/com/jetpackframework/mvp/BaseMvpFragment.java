package com.jetpackframework.mvp;

import android.os.Bundle;

import com.jetpackframework.base.BaseFragment;
import com.jetpackframework.ioc.IViewBind;


public abstract class BaseMvpFragment<V extends IViewBind,P extends BasePresenter> extends BaseFragment<V> implements IBaseView{
    protected P mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        if (mPresenter != null){
            mPresenter.onMvpAttachView(this);
            getLifecycle().addObserver(mPresenter);
        }
    }

    protected abstract P getPresenter();
}
