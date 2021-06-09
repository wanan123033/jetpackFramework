package com.mindmachine.appmodule.viewmodel;

import com.jetpackframework.mvvm.BaseViewModel;
import com.mindmachine.common.presenter.LoginPresenter;

public class LoginViewModel extends BaseViewModel<Object> {

    public void login(String username,String password){
        LoginPresenter loginPresenter = new LoginPresenter();
        loginPresenter.setViewModel(this);
        loginPresenter.login(username,password);
    }
}
