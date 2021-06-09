package com.mindmachine.appmodule.activity;

import android.view.View;

import com.appmodule.layout.activity_device_out;
import com.gwm.annotation.layout.Layout;
import com.gwm.annotation.layout.OnClick;
import com.gwm.annotation.router.ARouter;
import com.jetpackframework.base.BaseActivity;
import com.mindmachine.appmodule.R;
import com.mindmachine.appmodule.R2;
import com.mindmachine.common.contract.ItemContract;
import com.mindmachine.common.contract.RouterContract;

@ARouter(RouterContract.DEVICEOUTACTIVITY)
@Layout(R2.layout.activity_device_out)
public class DeviceOutActivity extends BaseActivity<activity_device_out> {
    @OnClick({R2.id.btn_zwtqq,R2.id.btn_ldty,R2.id.btn_hwjs,R2.id.btn_hwsxq,R2.id.btn_jgcj,R2.id.btn_ts,R2.id.btn_ywqz})
    public void onClick(View view){
        if (view.getId() == R.id.btn_zwtqq){
            ItemContract.setCurrentItem(ItemContract.ITEM_ZWTQQ);
        }else if (view.getId() == R.id.btn_ldty){
            ItemContract.setCurrentItem(ItemContract.ITEM_LDTY);
        }else if (view.getId() == R.id.btn_hwjs){
            ItemContract.setCurrentItem(ItemContract.ITEM_HWJS);
        }else if (view.getId() == R.id.btn_hwsxq){
            ItemContract.setCurrentItem(ItemContract.ITEM_HWSXQ);
        }else if (view.getId() == R.id.btn_jgcj){
            ItemContract.setCurrentItem(ItemContract.ITEM_JGCJ);
        }else if (view.getId() == R.id.btn_ts){
            ItemContract.setCurrentItem(ItemContract.ITEM_TS);
        }else if (view.getId() == R.id.btn_ywqz){
            ItemContract.setCurrentItem(ItemContract.ITEM_YWQZ);
        }
    }
}
