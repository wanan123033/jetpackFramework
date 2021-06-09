package com.frameworkx.fileexpoter;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fileexpoter.layout.activity_file_export;
import com.gwm.annotation.layout.IOCWork;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.gwm.annotation.router.Module;
import com.gwm.annotation.router.RouterField;
import com.jetpackframework.arouter.Router;
import com.jetpackframework.base.BaseTitleActivity;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.common.contract.RouterContract;
@Module(RouterContract.APP_MAIN_FILEAPP)
@IOCWork("fileexpoter")
@ARouter(RouterContract.FILEEXPORTACTIVITY)
@Layout(R2.layout.activity_file_export)
public class FileExportActivity extends BaseTitleActivity<activity_file_export> {
    @RouterField(value = "path",fieldClass = String.class)
    String path;

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("资源管理");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(path)){
            Router.getInstance(this).from("path","/").to(RouterContract.ROUTER_FILEEXPORTFRAGMENT).routerFragment(getSupportFragmentManager(),R.id.frame_content);
        }else {
            Router.getInstance(this).from("path",path).to(RouterContract.ROUTER_FILEEXPORTFRAGMENT).routerFragment(getSupportFragmentManager(),R.id.frame_content);
        }
    }
}
