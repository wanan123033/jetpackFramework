package com.mindmachine.appmodule;

import android.content.Context;

import com.appmodule.arouter.AppmoduleRouter;
import com.gwm.annotation.router.AutoService;
import com.gwm.annotation.router.Merge;
import com.jetpackframework.Application;
import com.jetpackframework.arouter.Router;
import com.jetpackframework.arouter.RouterApp;

@Merge({"appmodule","dataselectmanager","fileexpoter","hwjs","hwsxq","jgcj","ldty","ts","ywqz","zwtqq"})
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        FixConfig config = new FixConfig.Builder().builder();
//        FixUtil.install(config);
//        VirtualConfig config1 = new VirtualConfig.Builder()
//                .setContext(this)
//                .builder();
//        Virtual.install(config1);
        RouterApp.init(new AppmoduleRouter());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
