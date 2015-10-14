package com.hgsoft.mylibrary.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 极简适配器Helper 继承自{@link RecyclerView.ViewHolder}
 * 用于{@link BaseQuickAdapter#onBindView(ViewHolderHelper, int)} 方法中控件绑定
 *
 * @author LinZheng
 */
public class ViewHolderHelper extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public ViewHolderHelper(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public TextView getTextView(int viewId) {
        return findView(viewId);
    }

    public Button getButton(int viewId) {
        return findView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return findView(viewId);
    }

    public View getView(int viewId) {
        return findView(viewId);
    }

    public void setText(int viewId, String text) {
        getTextView(viewId).setText(text);
    }

}
