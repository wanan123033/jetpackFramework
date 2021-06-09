package com.jetpackframework.fixdex;

import com.jetpackframework.applicationdelegate.ApplicationDelegate;
import com.jetpackframework.applicationdelegate.ApplicationLifeCycle;
import com.jetpackframework.applicationdelegate.BaseApplication;

/**
 * 催生Application的代理,Application的执行由代理完成热修复的加载
 */
public class FixDexApplication extends BaseApplication implements ApplicationLifeCycle {

    public ApplicationDelegate createApplicationDelegate() {
        return FixDexApplicationDelegate.getInstance();
    }
}
