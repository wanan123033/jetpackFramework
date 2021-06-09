package com.jetpackframework.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gwm.annotation.layout.Layout;
import com.jetpackframework.ContextUtil;
import com.jetpackframework.ioc.ARouterEventClassUtil;
import com.jetpackframework.ioc.ARouterLayoutUtil;
import com.jetpackframework.ioc.IEventClass;
import com.jetpackframework.ioc.IViewBind;


public class BaseDialogFragment<V extends IViewBind> extends DialogFragment {

    private V mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Layout layout = getClass().getAnnotation(Layout.class);
        View view = LayoutInflater.from(ContextUtil.get()).inflate(layout.value(), container, false);
        mBinding = getViewBind(layout.value());
        mBinding.bindView(view);
        getEventClass().bindEvent(this,mBinding);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }



    private IEventClass getEventClass() {
        return ARouterEventClassUtil.getInstance().getEventClass(getClass());
    }

    protected V getViewBind(int layoutId){
        return ARouterLayoutUtil.getInstance().getViewBind(layoutId);
    }
}
