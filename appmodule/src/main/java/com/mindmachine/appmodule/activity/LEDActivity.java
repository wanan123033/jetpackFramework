package com.mindmachine.appmodule.activity;

import com.appmodule.layout.activity_led;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.base.BaseTitleActivity;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.appmodule.R2;
import com.mindmachine.common.contract.RouterContract;

@ARouter(RouterContract.LEDACTIVITY)
@Layout(R2.layout.activity_led)
public class LEDActivity extends BaseTitleActivity<activity_led> {
    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("显示屏设置");
    }
}
