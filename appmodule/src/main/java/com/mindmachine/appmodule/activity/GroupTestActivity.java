package com.mindmachine.appmodule.activity;

import android.os.Bundle;
import android.view.View;

import com.appmodule.layout.activity_group_test;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.mvvm.BaseMvvmTitleActivity;
import com.jetpackframework.view.titlebar.TitleBarBuilder;
import com.mindmachine.appmodule.R;
import com.mindmachine.appmodule.R2;
import com.mindmachine.appmodule.viewmodel.GroupViewModel;
import com.mindmachine.common.contract.ItemContract;
import com.mindmachine.common.contract.RouterContract;

@ARouter(RouterContract.GROUPTESTACTIVITY)
@Layout(R2.layout.activity_group_test)
public class GroupTestActivity extends BaseMvvmTitleActivity<Object, GroupViewModel, activity_group_test> {
    @Override
    protected Class<GroupViewModel> getViewModelClass() {
        return GroupViewModel.class;
    }

    @Override
    public TitleBarBuilder setTitleBuilder(TitleBarBuilder titleBarBuilder) {
        return titleBarBuilder.setTitle("请选择分组");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R2.id.img_last,R2.id.img_next,R2.id.txt_start_test,R2.id.txt_print})
    public void onclick(View v){
        if (v.getId() == R.id.img_last){
            viewModel.lastGroup();
        }else if (v.getId() == R.id.img_next){
            viewModel.nextGroup();
        }else if (v.getId() == R.id.txt_start_test){
            arouterActivity(ItemContract.getGroupRouterUrl(ItemContract.getCurrentItemCode()));
        }else if (v.getId() == R.id.txt_print){
            viewModel.print();
        }
    }
}
