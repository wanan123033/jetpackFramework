package com.mindmachine.common.base;

import android.view.View;

import com.jetpackframework.ioc.IViewBind;
import com.jetpackframework.mvvm.BaseMvvmTitleActivity;
import com.jetpackframework.mvvm.ViewModel;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.common.R;

public class BaseCommonMvvmTitleActivity<M,VM extends ViewModel<M>,V extends IViewBind> extends BaseMvvmTitleActivity<M,VM,V> {
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    @Override
    protected Class<VM> getViewModelClass() {
        return null;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setLeftText("返回")
                .setLeftImageResource(R.mipmap.ic_launcher)
                .setLeftImageOnClickListener(onClickListener)
                .setLeftTextOnClickListener(onClickListener);
    }
}
