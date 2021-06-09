package com.mindmachine.ts.activity;

import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.common.base.BasePersonActivity;
import com.mindmachine.common.contract.RouterContract;
import com.mindmachine.ts.R2;
import com.mindmachine.ts.viewmodel.TsViewModel;
import com.ts.layout.activity_person_ts;

@ARouter(RouterContract.TSPERSONACTIVITY)
@Layout(R2.layout.activity_person_ts)
public class TsPersonActivity extends BasePersonActivity<activity_person_ts, TsViewModel> {
    @Override
    protected Class<TsViewModel> getViewModelClass() {
        return TsViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("跳绳-个人模式");
    }
}
