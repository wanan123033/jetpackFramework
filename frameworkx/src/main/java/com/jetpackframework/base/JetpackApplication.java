package com.jetpackframework.base;

import com.jetpackframework.applicationdelegate.ApplicationDelegate;
import com.jetpackframework.applicationdelegate.BaseApplication;

public class JetpackApplication extends BaseApplication {
    @Override
    protected ApplicationDelegate createApplicationDelegate() {
        return JetpackApplicationDelegate.getInstance();
    }
}
