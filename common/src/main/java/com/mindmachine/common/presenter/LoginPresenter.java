package com.mindmachine.common.presenter;

import com.common.retrofit.RetrofitUtil;
import com.jetpackframework.retrofit.BaseDataPresenter;
import com.jetpackframework.retrofit.IRetrofitUtil;
import com.jetpackframework.rxjetpack.Observable;
import com.mindmachine.common.bean.LoginBean;
import com.mindmachine.common.contract.HttpContract;

public class LoginPresenter extends BaseDataPresenter<HttpContract, LoginBean> {

    /**
     * 登录
     * @param username
     * @param password
     */
    public void login(String username,String password){
        String token = "Basic dGVybWluYWw6dGVybWluYWxfc2VjcmV0";
        Observable<LoginBean> loginhttp = getHttpPresenter().login(token,username,password);
        addHttpSubscriber(loginhttp);
    }

    @Override
    protected IRetrofitUtil<HttpContract> getRetrifit() {
        return RetrofitUtil.getInstance();
    }

    @Override
    protected void onNextResult(LoginBean response, int id) {

    }

    @Override
    protected void onErrorResult(Exception e, int id) {

    }
}
