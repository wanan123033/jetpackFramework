package com.mindmachine.appmodule.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.appmodule.layout.activity_param_setting;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.arouter.Router;
import com.jetpackframework.base.BaseTitleActivity;
import com.jetpackframework.base.JetpackApplicationDelegate;
import com.jetpackframework.http.HttpUtil;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.appmodule.R;
import com.mindmachine.appmodule.R2;
import com.mindmachine.common.contract.HttpContract;
import com.mindmachine.common.contract.MMKVContract;
import com.mindmachine.common.contract.RouterContract;
import com.mindmachine.common.setting.SystemSetting;
import com.tencent.mmkv.MMKV;

@ARouter(RouterContract.PARAMSETTINGACTIVITY)
@Layout(R2.layout.activity_param_setting)
public class ParamSettingActivity extends BaseTitleActivity<activity_param_setting> {
    private SystemSetting systemSetting;
    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("参数设置");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        systemSetting = mmkv.decodeParcelable(MMKVContract.SYSTEM_SETTING_BEAN, SystemSetting.class);
        if (systemSetting == null){
            systemSetting = new SystemSetting();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mmkv.encode(MMKVContract.SYSTEM_SETTING_BEAN,systemSetting);
        HttpUtil.getInstance().baseUrl(systemSetting.serverIp);
    }
    @OnClick({R2.id.btn_default,R2.id.btn_bind,R2.id.btn_tcp_test,R2.id.btn_monitoring_setting,R2.id.btn_thermometer,R2.id.btn_net_setting,R2.id.txt_advanced})
    public void onClick(View v){
        if (v.getId() == R.id.btn_default){
            mBinding.et_sever_ip.setText(HttpContract.BASE_URL);
            systemSetting.serverIp = HttpContract.BASE_URL;
        }else if (v.getId() == R.id.btn_bind){
            Router.getInstance(this).from("url",15).to(RouterContract.ROUTER_LOGINACTIVITY).router();
        }else if (v.getId() == R.id.btn_tcp_test){

        }else if (v.getId() == R.id.btn_monitoring_setting){

        }else if (v.getId() == R.id.btn_thermometer){

        }else if (v.getId() == R.id.btn_net_setting){

        }else if (v.getId() == R.id.txt_advanced){

        }
    }
}
