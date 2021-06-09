package com.mindmachine.zwtqq.activity;

import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.common.base.BasePersonActivity;
import com.mindmachine.common.contract.RouterContract;
import com.mindmachine.zwtqq.R2;
import com.mindmachine.zwtqq.viewmodel.ZwtqqPersonViewModel;
import com.zwtqq.layout.activity_zwtqq_person;

@ARouter(RouterContract.ZWTQQPERSONACTIVITY)
@Layout(R2.layout.activity_zwtqq_person)
public class ZwtqqPersonActivity extends BasePersonActivity<activity_zwtqq_person, ZwtqqPersonViewModel> {
    @Override
    protected Class<ZwtqqPersonViewModel> getViewModelClass() {
        return ZwtqqPersonViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("坐位体前屈-个人模式");
    }
}
