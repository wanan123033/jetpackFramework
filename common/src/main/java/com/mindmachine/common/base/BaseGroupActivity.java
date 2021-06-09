package com.mindmachine.common.base;

import com.jetpackframework.ioc.IViewBind;
import com.jetpackframework.mvvm.BaseViewModel;
import com.jetpackframework.view.titlebar.TitleBarBuilder;

public abstract class BaseGroupActivity<V extends IViewBind,VM extends BaseViewModel<Object>> extends BaseCheckActivity<Object,VM,V> {

}
