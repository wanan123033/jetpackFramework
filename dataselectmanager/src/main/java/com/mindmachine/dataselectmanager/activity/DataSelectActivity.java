package com.mindmachine.dataselectmanager.activity;

import com.dataselectmanager.layout.activity_data_select;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.common.base.BaseCommonMvvmTitleActivity;
import com.mindmachine.common.contract.RouterContract;
import com.mindmachine.dataselectmanager.R2;
import com.mindmachine.dataselectmanager.viewmodel.DataSelectViewModel;

@ARouter(RouterContract.DATASELECTACTIVITY)
@Layout(R2.layout.activity_data_select)
public class DataSelectActivity extends BaseCommonMvvmTitleActivity<Object, DataSelectViewModel, activity_data_select> {
    @Override
    protected Class<DataSelectViewModel> getViewModelClass() {
        return DataSelectViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return super.setTitleBuilder(titleBarBuilder.setTitle("数据查询"));
    }

}
