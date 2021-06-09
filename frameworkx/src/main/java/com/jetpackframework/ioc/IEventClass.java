package com.jetpackframework.ioc;

public interface IEventClass<V extends IViewBind> {
    void bindEvent(Object object, V view);
}
