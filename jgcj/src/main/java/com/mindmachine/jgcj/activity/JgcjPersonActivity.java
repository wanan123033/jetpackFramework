package com.mindmachine.jgcj.activity;

import com.gwm.annotation.layout.IOCWork;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.router.ARouter;
import com.gwm.annotation.router.Module;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.jgcj.layout.activity_jgcj_person;
import com.mindmachine.common.base.BasePersonActivity;
import com.mindmachine.common.contract.RouterContract;
import com.mindmachine.jgcj.R2;
import com.mindmachine.jgcj.viewmodel.JgcjViewModel;

@Module("jgcj")
@IOCWork("jgcj")
@ARouter(RouterContract.JGCJPERSONACTIVITY)
@Layout(R2.layout.activity_jgcj_person)
public class JgcjPersonActivity extends BasePersonActivity<activity_jgcj_person, JgcjViewModel> {
    @Override
    protected Class<JgcjViewModel> getViewModelClass() {
        return JgcjViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("激光测距-个人模式");
    }
}
