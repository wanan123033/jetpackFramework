package com.jetpackframework.mvp;

import android.os.Bundle;

import com.jetpackframework.base.BaseTitleActivity;
import com.jetpackframework.ioc.IViewBind;

public abstract class BaseMvpTitleActivity<V extends IViewBind,P extends BasePresenter> extends BaseTitleActivity<V> implements IBaseView {
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
