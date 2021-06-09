package com.jetpackframework.ioc;

public interface EventClassUtil {
    IEventClass getEventClass(Class clazz);
    void clear();
}
