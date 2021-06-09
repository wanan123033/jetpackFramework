package com.jetpackframework.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;

import com.gwm.annotation.layout.Layout;
import com.jetpackframework.ContextUtil;
import com.jetpackframework.R;
import com.jetpackframework.ioc.ARouterLayoutUtil;
import com.jetpackframework.ioc.IViewBind;
import com.jetpackframework.view.titlebar.CustomNavigatorBar;
import com.jetpackframework.view.titlebar.TitleBarBuilder;

public abstract class BaseTitleActivity<V extends IViewBind> extends BaseActivity {

    protected V mBinding;
    private FrameLayout frame_content;
    protected CustomNavigatorBar cnb_title;

    @Override
    protected void initEventListener() {
        setContentView(R.layout.activity_base_title);
        Layout layout = getClass().getAnnotation(Layout.class);
        if (layout != null){
            frame_content = findViewById(R.id.frame_content);
            cnb_title = findViewById(R.id.cnb_title);
            cnb_title.setTitleBulder(setTitleBuilder(new TitleBarBuilder()));
            mBinding = getViewBind(layout.value());
            frame_content.addView(inflate(mBinding.getLayoutId(),frame_content));
            mBinding.bindView(getView());
            getEventClass().bindEvent(this,mBinding);
        }

    }

    public abstract TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder);

    private View inflate(@LayoutRes int resId, ViewGroup viewGroup){
        return LayoutInflater.from(ContextUtil.get()).inflate(resId,viewGroup,false);
    }
    protected V getViewBind(@LayoutRes int resId){
        return ARouterLayoutUtil.getInstance().getViewBind(resId);
    }
}
