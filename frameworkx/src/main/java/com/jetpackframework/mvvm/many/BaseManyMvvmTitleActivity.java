package com.jetpackframework.mvvm.many;

import com.jetpackframework.ioc.IViewBind;
import com.jetpackframework.mvvm.BaseMvvmTitleActivity;

import java.lang.reflect.Method;

public abstract class BaseManyMvvmTitleActivity<VM extends BaseManyViewModel,V extends IViewBind> extends BaseMvvmTitleActivity<ManyBean,VM,V> {
    @Override
    public void onChanged(ManyBean o) {
        Method method = null;
        try {
            if (o.params != null){
                method = getClass().getMethod(o.name,o.params.getClass());
                method.invoke(this,o.params);
            }else {
                method = getClass().getMethod(o.name, (Class<?>) null);
                method.invoke(this, (Object) null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}