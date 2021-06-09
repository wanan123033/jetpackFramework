package com.jetpackframework.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gwm.annotation.layout.Layout;
import com.jetpackframework.SparseArray;
import com.jetpackframework.ioc.ARouterLayoutUtil;

import java.util.List;

public abstract class BaseRecyclerAdapter<D> extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder> {
    private List<D> data;
    private OnItemClickListener mOnItemClickListener;

    public BaseRecyclerAdapter(List<D> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Layout layout = getClass().getAnnotation(Layout.class);
        int layoutId = ARouterLayoutUtil.getInstance().getViewBind(layout.value()).getLayoutId();
        ViewHolder viewHolder = ViewHolder.get(parent.getContext(), parent, layoutId);
        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        setData(holder,position,data.get(position));
        //绑定事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(null,holder.itemView, pos,0);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    return mOnItemClickListener.onItemLongClick(null,holder.itemView, pos,0);
                }
            });
        }
    }

    protected abstract void setData(ViewHolder holder, int position, D item);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void addAll(List<D> datas) {
        data.addAll(datas);
        notifyDataSetChanged();
    }

    public D getItem(int position) {
        return data.get(position);
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder{

        private SparseArray<View> mViews;
        private View mConvertView;

        public ViewHolder(View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<View>();
        }

        public static ViewHolder get(Context context, ViewGroup parent, int layoutid) {
            View itemView = LayoutInflater.from(context).inflate(layoutid, parent, false);
            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        public <T extends View> T getView(int viewid) {
            View view = mViews.get(viewid);
            if (view == null) {
                view = mConvertView.findViewById(viewid);
                mViews.put(viewid, view);
            }
            return (T) view;
        }

        /**
         * 设置TextView文本
         */
        public ViewHolder setText(int viewId, CharSequence text) {
            View view = getView(viewId);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        /**
         * 设置View的Visibility
         */
        public ViewHolder setViewVisibility(int viewId, int visibility) {
            getView(viewId).setVisibility(visibility);
            return this;
        }

        /**
         * 设置ImageView的资源
         */
        public ViewHolder setImageResource(int viewId, int drawableRes) {
            View view = getView(viewId);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(drawableRes);
            } else {
                view.setBackgroundResource(drawableRes);
            }
            return this;
        }

        public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener onLongClickListener){
            View view = getView(viewId);
            view.setOnLongClickListener(onLongClickListener);
            return this;
        }
        public ViewHolder setOnClickListener(int viewId, View.OnClickListener onClickListener){
            View view = getView(viewId);
            view.setOnClickListener(onClickListener);
            return this;
        }
    }
    public interface OnItemClickListener extends AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {}
}
