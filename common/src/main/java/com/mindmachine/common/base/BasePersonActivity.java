package com.mindmachine.common.base;

import com.jetpackframework.ioc.IViewBind;
import com.jetpackframework.mvvm.BaseViewModel;

public abstract class BasePersonActivity<V extends IViewBind,VM extends BaseViewModel<Object>> extends BaseCheckActivity<Object,VM,V> {

}
