package com.mindmachine.dataselectmanager.activity;

import com.dataselectmanager.layout.activity_temperature_select;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.common.base.BaseCommonMvvmTitleActivity;
import com.mindmachine.common.contract.RouterContract;
import com.mindmachine.dataselectmanager.R2;
import com.mindmachine.dataselectmanager.viewmodel.TemperatureSelectViewModel;

@Layout(R2.layout.activity_temperature_select)
@ARouter(RouterContract.TEMPERATURESELECTACTIVITY)
public class TemperatureSelectActivity extends BaseCommonMvvmTitleActivity<Object, TemperatureSelectViewModel, activity_temperature_select> {

    @Override
    protected Class<TemperatureSelectViewModel> getViewModelClass() {
        return TemperatureSelectViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return super.setTitleBuilder(titleBarBuilder.setTitle("体温查询"));
    }
}