package com.mindmachine.common.contract;

import com.gwm.annotation.http.HTTP;
import com.gwm.annotation.http.HeaderString;
import com.gwm.annotation.http.JSON;
import com.gwm.annotation.http.JSONRequest;
import com.gwm.annotation.http.PostFormRequest;
import com.gwm.annotation.http.Query;
import com.gwm.annotation.retrofit.HttpModel;
import com.gwm.annotation.retrofit.OkHttp;
import com.jetpackframework.rxjetpack.Observable;
import com.mindmachine.common.bean.LoginBean;
import com.mindmachine.common.bean.RosterDownBean;

import okhttp3.OkHttpClient;

@HttpModel(baseUrl = HttpContract.BASE_URL)
public interface HttpContract {
    @OkHttp
    OkHttpClient client = null;
    String BASE_URL = "https://www.baidu.com";

    @PostFormRequest
    @HTTP(url = "/auth/terminal/token")
    Observable<LoginBean> login(@HeaderString("Authorization")String token, @Query("username")String username,@Query("password")String password);

    @JSONRequest
    @HTTP(url = "")
    Observable<RosterDownBean> rosterdown(@HeaderString("Authorization")String token, @JSON String json);



}
