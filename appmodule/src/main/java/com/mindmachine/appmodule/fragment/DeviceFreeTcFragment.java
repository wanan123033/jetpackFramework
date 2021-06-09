package com.mindmachine.appmodule.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.appmodule.layout.fragment_device_free_tc;
import com.gwm.annotation.layout.Layout;
import com.jetpackframework.base.BaseFragment;
import com.mindmachine.appmodule.R;
import com.mindmachine.appmodule.R2;
import com.mindmachine.appmodule.adapter.DeviceOutAdapter;
import com.mindmachine.appmodule.domain.FreeItem;
import com.mindmachine.common.contract.ItemContract;

import java.util.ArrayList;
import java.util.List;

@Layout(R2.layout.fragment_device_free_tc)
public class DeviceFreeTcFragment extends BaseFragment<fragment_device_free_tc> {
    private List<FreeItem> freeItems;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        freeItems = new ArrayList<>();
        freeItems.add(new FreeItem(R.mipmap.ic_launcher,"跳绳", ItemContract.ITEM_TS));
        mBinding.rv_list.setLayoutManager(new StaggeredGridLayoutManager(4, RecyclerView.VERTICAL));
        mBinding.rv_list.setAdapter(new DeviceOutAdapter(freeItems));
    }
}
