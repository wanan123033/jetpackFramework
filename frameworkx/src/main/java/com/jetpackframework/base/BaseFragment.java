package com.jetpackframework.base;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.RouterField;
import com.jetpackframework.AppHandler;
import com.jetpackframework.ContextUtil;
import com.jetpackframework.HandlerListener;
import com.jetpackframework.MyHandler;
import com.jetpackframework.arouter.Router;
import com.jetpackframework.databus.DataBusUtils;
import com.jetpackframework.ioc.ARouterEventClassUtil;
import com.jetpackframework.ioc.ARouterLayoutUtil;
import com.jetpackframework.ioc.IEventClass;
import com.jetpackframework.ioc.IViewBind;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class BaseFragment<V extends IViewBind> extends Fragment implements HandlerListener {
    protected AppHandler appHandler;
    protected MyHandler handler;
    protected V mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appHandler = AppHandler.getAppHandler();
        handler = new MyHandler(this);
        DataBusUtils.getInstance().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        readRouterData();
        super.setArguments(args);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataBusUtils.getInstance().unregister(this);
        handler.removeCallbacksAndMessages(null);

    }

    private View initView() {
        Layout layout = getClass().getAnnotation(Layout.class);
        View contentView = null;
        if (layout != null){
            mBinding = getViewBind(layout.value());
            contentView = setContentView(mBinding.getLayoutId());
            mBinding.bindView(contentView);
            getEventClass().bindEvent(this,mBinding);
        }
        return contentView;
    }

    private View setContentView(int value) {
        return LayoutInflater.from(ContextUtil.get()).inflate(value,null,false);
    }

    protected V getViewBind(@LayoutRes int resId){
        return ARouterLayoutUtil.getInstance().getViewBind(resId);
    }
    protected IEventClass getEventClass(){
        return ARouterEventClassUtil.getInstance().getEventClass(getClass());
    }
    public void handleMessage(Message msg) {
    }


    public void arouterActivity(String url) {
        Router.getInstance(getActivity()).to(url).router();
    }

    public void arouterActivityForResult(String url,int requestCode) {

        Router.getInstance(getActivity()).to(url).routerForResult(requestCode);
    }

    public void arouterFragment(String url, @IdRes int contentId) {
        Router.getInstance(getActivity()).to(url).routerFragment(getChildFragmentManager(),contentId);
    }

    private void readRouterData() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields){
            if (field.isAnnotationPresent(RouterField.class)){
                field.setAccessible(true);
                RouterField routerField = field.getAnnotation(RouterField.class);
                Class<?> fieldClass = routerField.fieldClass();
                Object o = null;
                if (fieldClass == String.class){
                    o = CacheDiskUtils.getInstance().getString(routerField.value());
                }else if (fieldClass == int.class || fieldClass == byte.class || fieldClass == short.class || fieldClass == long.class
                        || fieldClass == float.class || fieldClass == double.class || fieldClass == boolean.class ||
                        fieldClass == char.class || fieldClass == int[].class || fieldClass == short[].class || fieldClass == long[].class ||
                        fieldClass == boolean[].class || fieldClass == char[].class || fieldClass == String[].class || fieldClass == float[].class ||
                        fieldClass == double[].class || fieldClass == Serializable.class || fieldClass == ArrayList.class){
                    o = CacheDiskUtils.getInstance().getSerializable(routerField.value());
                }else if (fieldClass == byte[].class){
                    o = CacheDiskUtils.getInstance().getBytes(routerField.value());
                }else if (fieldClass == Bitmap.class){
                    o = CacheDiskUtils.getInstance().getBitmap(routerField.value());
                }else if (fieldClass == Parcelable.class){
                    try {
                        Field creator = field.getType().getField("CREATOR");
                        Object creatorObj = creator.get(null);
                        o = CacheDiskUtils.getInstance().getParcelable(routerField.value(), (Parcelable.Creator<Object>) creatorObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                CacheDiskUtils.getInstance().remove(routerField.value());
                try {
                    field.set(this,o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    protected String getApplicationMetaValue(String name) {
        return ((BaseActivity)getActivity()).getApplicationMetaValue(name);
    }
}
