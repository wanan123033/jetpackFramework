package com.jetpackframework.base;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.gwm.annotation.bus.LiveMessage;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.RouterField;
import com.jetpackframework.AppHandler;
import com.jetpackframework.Application;
import com.jetpackframework.HandlerListener;
import com.jetpackframework.MyHandler;
import com.jetpackframework.arouter.Router;
import com.jetpackframework.databus.DataBusUtils;
import com.jetpackframework.ioc.ARouterEventClassUtil;
import com.jetpackframework.ioc.ARouterLayoutUtil;
import com.jetpackframework.ioc.IEventClass;
import com.jetpackframework.ioc.IViewBind;
import com.jetpackframework.view.CustomDialog;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity通用基类
 *      1.封装Handler通讯机制
 *      2.Layout注解:IOC处理
 *      3.再按一次退出程序功能
 *      4.ARouter 跳转
 *      5.读取ARouter数据
 */
public class BaseActivity<V extends IViewBind> extends AppCompatActivity implements HandlerListener {
    public static final String SHOW_PROGRESS = "6666";
    public static final String DIMMSION_PROGREESS = "5858";
    protected AppHandler appHandler;
    protected MyHandler handler;
    protected V mBinding;
    private long exitTime = 0;
    private CustomDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appHandler = AppHandler.getAppHandler();
        handler = new MyHandler(this);
        readRouterData();
        initEventListener();
        DataBusUtils.getInstance().register(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        DataBusUtils.getInstance().unregister(this);
    }

    protected void initEventListener() {
        Layout layout = getClass().getAnnotation(Layout.class);
        if (layout != null){
            mBinding = getViewBind(layout.value());
            Log.e("TAG",layout.value()+"------"+mBinding);
            View view = LayoutInflater.from(getApplicationContext()).inflate(mBinding.getLayoutId(),null,false);
            setContentView(view);
            mBinding.bindView(view);
            getEventClass().bindEvent(this,mBinding);
        }
    }

    protected View getView() {
        return getWindow().getDecorView();
    }

    protected V getViewBind(@LayoutRes int resId){
        return ARouterLayoutUtil.getInstance().getViewBind(resId);
    }
    protected IEventClass getEventClass(){
        return ARouterEventClassUtil.getInstance().getEventClass(getClass());
    }


    /**
     * 将Activity添加到退出通知
     */
    public void addFirstToast() {
        JetpackApplicationDelegate.getInstance().getFisrstToasts().add(getActivityName());
    }
    /**
     * 获取当前正在运行的Activity
     * @return
     */
    private String getActivityName() {
        return getClass().getSimpleName();
    }

    protected String getApplicationMetaValue(String name) {
        String value = "";
        try {
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        List<String> activitys = JetpackApplicationDelegate.getInstance().getFisrstToasts();
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis() - exitTime) > 2000 && (activitys.indexOf(getActivityName()) != -1)){
                Toast.makeText(getApplicationContext(), "在按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else if ((JetpackApplicationDelegate.getInstance().getFisrstToasts().indexOf(getActivityName()) != -1)) {
                JetpackApplicationDelegate.getInstance().exit();
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && (activitys.indexOf(getActivityName()) != -1)) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return onKeyDownMethod(keyCode, event);
    }

    public boolean onKeyDownMethod(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode,event);
    }


    public void handleMessage(Message message){

    }

    public void arouterActivity(String url) {
        Router.getInstance(this).to(url).router();
    }

    public void arouterActivityForResult(String url,int requestCode) {
        Router.getInstance(this).to(url).routerForResult(requestCode);
    }

    public void arouterFragment(String url, int contentId) {
        Router.getInstance(this).to(url).routerFragment(getSupportFragmentManager(),contentId);
    }
    @LiveMessage(action = SHOW_PROGRESS)
    public void showDialog(String message){
        Log.e("TAG",message);
        if (dialog == null || !dialog.isShowing()){
            dialog = new CustomDialog(this);
        }
        dialog.setContentText(message);
        dialog.show();
    }

    @LiveMessage(action = DIMMSION_PROGREESS)
    public void dismissionDialog(String message){
        dialog.dismiss();
    }
}
