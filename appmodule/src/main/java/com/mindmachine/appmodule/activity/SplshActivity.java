package com.mindmachine.appmodule.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.appmodule.layout.activity_splsh;
import com.gwm.annotation.android.Permission;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.base.BaseSplshActivity;
import com.mindmachine.appmodule.R2;
import com.mindmachine.common.contract.RouterContract;

@Layout(R2.layout.activity_splsh)
@ARouter(RouterContract.SPLSHACTIVITY)
@Permission("android.permission.WRITE_EXTERNAL_STORAGE")
public class SplshActivity extends BaseSplshActivity<activity_splsh> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                arouterActivity(RouterContract.ROUTER_MAINACTIVITY);
            }
        },2000);
    }
}
