package com.mindmachine.common.base;

import android.view.View;

import com.jetpackframework.base.BaseTitleActivity;
import com.jetpackframework.ioc.IViewBind;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.common.R;

public class BaseCommonTitleActivity<V extends IViewBind> extends BaseTitleActivity<V> {
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setLeftText("返回")
                .setLeftImageResource(R.mipmap.ic_launcher)
                .setLeftImageOnClickListener(onClickListener)
                .setLeftTextOnClickListener(onClickListener);
    }

}
