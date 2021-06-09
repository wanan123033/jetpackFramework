package com.jetpackframework.retrofit;



import com.jetpackframework.http.GenJsonString;

import java.lang.reflect.Proxy;

public abstract class BaseJsonDataPresenter<J,H,D> extends BaseDataPresenter<H,D> {
    private J jsonCreator;
    public BaseJsonDataPresenter(Class<J> jClass) {
        jsonCreator = (J) Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{jClass},GenJsonString.getInstance());
    }

    protected J getJsonCreator() {
        return jsonCreator;
    }
}
