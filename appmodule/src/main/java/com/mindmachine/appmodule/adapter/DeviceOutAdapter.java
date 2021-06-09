package com.mindmachine.appmodule.adapter;

import com.gwm.annotation.layout.Layout;
import com.jetpackframework.base.BaseRecyclerAdapter;
import com.mindmachine.appmodule.R;
import com.mindmachine.appmodule.R2;
import com.mindmachine.appmodule.domain.FreeItem;

import java.util.List;

@Layout(R2.layout.item_free_out)
public class DeviceOutAdapter extends BaseRecyclerAdapter<FreeItem> {
    public DeviceOutAdapter(List<FreeItem> data) {
        super(data);
    }

    @Override
    protected void setData(ViewHolder holder, int position, FreeItem item) {
        holder.setImageResource(R.id.iv_icon,item.freeIcon);
        holder.setText(R.id.tv_name,item.itemName);
    }
}
