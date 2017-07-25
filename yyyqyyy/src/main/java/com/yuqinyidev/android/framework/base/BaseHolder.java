package com.yuqinyidev.android.framework.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yuqinyidev.android.framework.utils.ThirdViewUtil;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by RDX64 on 2017/6/28.
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
    protected OnViewClickListener mOnViewClickListener = null;
    protected OnViewLongClickListener mOnViewLongClickListener = null;
    protected final String TAG = this.getClass().getSimpleName();

    public BaseHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        AutoUtils.autoSize(itemView);
        ThirdViewUtil.bindTarget(this, itemView);
    }

    public abstract void setData(T data, int position);

    protected void onRelease() {
    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, this.getPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mOnViewLongClickListener != null) {
            mOnViewLongClickListener.onViewLongClick(view, this.getPosition());
        }
        return true;
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }

    public interface OnViewLongClickListener {
        void onViewLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }

    public void setOnItemLongClickListener(OnViewLongClickListener listener) {
        this.mOnViewLongClickListener = listener;
    }
}
