package com.mindmachine.appmodule;

import android.content.Context;

import com.gwm.annotation.router.Merge;
import com.jetpackframework.Application;
import com.jetpackframework.ApplicationDelegate;
import com.jetpackframework.fixdex.FixConfig;
import com.jetpackframework.fixdex.FixUtil;
import com.jetpackframework.virtual.Virtual;
import com.jetpackframework.virtual.VirtualConfig;

@Merge({"appmodule","dataselectmanager","fileapp"})
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        ApplicationDelegate.getInstance().setProjectName("appmodule");
        super.attachBaseContext(base);

        FixConfig config = new FixConfig.Builder().builder();
        FixUtil.install(config);
        VirtualConfig config1 = new VirtualConfig.Builder()
                .setContext(this)
                .builder();
        Virtual.install(config1);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
