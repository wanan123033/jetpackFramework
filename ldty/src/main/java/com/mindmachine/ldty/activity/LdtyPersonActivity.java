package com.mindmachine.ldty.activity;

import com.gwm.annotation.layout.IOCWork;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.gwm.annotation.router.Module;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.ldty.layout.activity_ldty_person;
import com.mindmachine.common.base.BasePersonActivity;
import com.mindmachine.common.contract.RouterContract;
import com.mindmachine.ldty.R2;
import com.mindmachine.ldty.viewmodel.LdtyPersonViewModel;
@IOCWork("ldty")
@Module(RouterContract.APP_MAIN_LDTY)
@ARouter(RouterContract.LDTYPERSONACTIVITY)
@Layout(R2.layout.activity_ldty_person)
public class LdtyPersonActivity extends BasePersonActivity<activity_ldty_person, LdtyPersonViewModel>{
    @Override
    protected Class<LdtyPersonViewModel> getViewModelClass() {
        return LdtyPersonViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("立定跳远-个人模式");
    }
}
