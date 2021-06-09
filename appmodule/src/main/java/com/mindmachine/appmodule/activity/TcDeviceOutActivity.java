package com.mindmachine.appmodule.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.appmodule.layout.activity_device_out_tc;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.base.BaseTitleActivity;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.appmodule.R2;
import com.mindmachine.appmodule.adapter.DevicePageAdapter;
import com.mindmachine.common.contract.RouterContract;

@ARouter(RouterContract.TCDEVICEOUTACTIVITY)
@Layout(R2.layout.activity_device_out_tc)
public class TcDeviceOutActivity extends BaseTitleActivity<activity_device_out_tc> {
    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("设备切换");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.vp_content.setAdapter(new DevicePageAdapter(getSupportFragmentManager()));
    }
}
