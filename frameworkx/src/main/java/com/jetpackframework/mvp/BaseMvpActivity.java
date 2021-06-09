package com.jetpackframework.mvp;

import android.os.Bundle;

import com.jetpackframework.base.BaseActivity;
import com.jetpackframework.ioc.IViewBind;


public abstract class BaseMvpActivity<V extends IViewBind,P extends BasePresenter> extends BaseActivity<V> implements IBaseView {
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

    public abstract P getPresenter();
}
