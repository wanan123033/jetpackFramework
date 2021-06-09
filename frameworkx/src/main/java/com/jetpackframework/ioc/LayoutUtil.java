package com.jetpackframework.ioc;

import androidx.annotation.LayoutRes;

public interface LayoutUtil {
    void clear();
    <T extends IViewBind> T getViewBind(@LayoutRes int layoutId);
}
