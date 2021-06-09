package com.frameworkx.fileexpoter;

import com.gwm.annotation.layout.Layout;
import com.jetpackframework.base.BaseRecyclerAdapter;

import java.util.List;
@Layout(R2.layout.item_file)
public class FileAdapter extends BaseRecyclerAdapter<FileBean> {
    public FileAdapter(List<FileBean> data) {
        super(data);
    }

    @Override
    protected void setData(ViewHolder holder, int position, FileBean item) {
        holder.setText(R.id.tv_name,item.name);
        if (item.isDir){
            holder.setImageResource(R.id.iv_icon,R.drawable.icon_folder);
        }else {
            holder.setImageResource(R.id.iv_icon,R.drawable.icon_doc);
        }
    }
}