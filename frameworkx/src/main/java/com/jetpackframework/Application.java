package com.jetpackframework;

import com.jetpackframework.applicationdelegate.BaseApplication;

public class Application extends BaseApplication {
    @Override
    protected ApplicationDelegate createApplicationDelegate() {
        ApplicationDelegate delegate = ApplicationDelegate.getInstance();
        return delegate;
    }

}