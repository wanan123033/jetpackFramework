package com.jetpackframework;

import com.jetpackframework.applicationdelegate.BaseApplication;

public class Application extends BaseApplication {
    private MergeApplicationDelegate delegate;
    @Override
    protected com.jetpackframework.applicationdelegate.ApplicationDelegate createApplicationDelegate() {
        delegate = new MergeApplicationDelegate();
        return delegate;
    }

    public MergeApplicationDelegate getDelegate(){
        return delegate;
    }

}