package com.mindmachine.appmodule.activity;

import android.os.Bundle;
import android.util.Log;

import com.appmodule.layout.activity_login;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.gwm.annotation.router.RouterField;
import com.jetpackframework.mvvm.BaseMvvmTitleActivity;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.appmodule.R2;
import com.mindmachine.appmodule.viewmodel.LoginViewModel;
import com.mindmachine.common.contract.RouterContract;

@ARouter(RouterContract.LOGINACTIVITY)
@Layout(R2.layout.activity_login)
public class LoginActivity extends BaseMvvmTitleActivity<Object, LoginViewModel, activity_login> {
    @RouterField(value = "url", fieldClass = int.class)
    int url;

    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("用户登录");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG",url+"");
        viewModel.login("name","123");
    }
}
