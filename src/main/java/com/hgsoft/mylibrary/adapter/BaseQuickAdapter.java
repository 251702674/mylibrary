package com.hgsoft.mylibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * {@link RecyclerView} 极简适配器抽象类
 *
 * @author LinZheng
 */
public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<ViewHolderHelper> {

    /* item点击事件监听器 */
    protected OnItemClickListener mOnItemClickListener;
    /* item长按事件监听器 */
    protected OnItemLongClickListener mOnItemLongClickListener;
    /* 上下文 */
    protected Context mContext;
    /* 数据集合 */
    protected List<T> mData;

    public BaseQuickAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    public List<T> getData() {
        return mData;
    }

    public Context getContext(){
        return mContext;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public ViewHolderHelper onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
        return new ViewHolderHelper(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderHelper holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("onclick");
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    System.out.println("onLongClick");
                    mOnItemLongClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
        onBindView(holder, position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public abstract int getLayoutId();

    public abstract void onBindView(ViewHolderHelper holder, int position);

}
