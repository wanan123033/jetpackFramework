package com.jetpackframework;

import com.jetpackframework.applicationdelegate.BaseApplication;

public class Application extends BaseApplication {
    @Override
    protected com.jetpackframework.applicationdelegate.ApplicationDelegate createApplicationDelegate() {
        com.jetpackframework.applicationdelegate.ApplicationDelegate delegate = new MergeApplicationDelegate();
        return delegate;
    }

}