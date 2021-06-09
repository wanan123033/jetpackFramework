package com.jetpackframework.ioc;

import android.view.View;

public interface IViewBind {
    public void bindView(View view);
    public int getLayoutId();
    public <T extends View> T findViewById(int id);
}
