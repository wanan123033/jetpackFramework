package com.jetpackframework.base;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.gwm.annotation.android.Permission;
import com.jetpackframework.ioc.IViewBind;

/**
 * 欢迎页面的基类
 *      1.权限处理集成 @Permission
 * @param <V>
 */
public class BaseSplshActivity<V extends IViewBind> extends BaseActivity<V> {
    private static final int PERMISSION_REQ_CODE = 666;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
    }

    private void initPermission() {
        Permission permission = getClass().getAnnotation(Permission.class);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N && permission != null){
            ActivityCompat.requestPermissions(this,permission.value(),PERMISSION_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 授予权限
                //处理授权之后逻辑
                onRequestPermissionsGranted();
            } else {
                // Permission Denied 权限被拒绝
                onRequestPermissionsDenied();
            }
        }
    }

    public void onRequestPermissionsDenied() {

    }

    public void onRequestPermissionsGranted() {

    }
}
