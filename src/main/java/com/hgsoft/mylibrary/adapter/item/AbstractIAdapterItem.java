package com.hgsoft.mylibrary.adapter.item;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 抽象
 */
public abstract class AbstractIAdapterItem<T> implements IAdapterItem<T> {

    private View mRootView;
    private SparseArray<View> mViews;

    public AbstractIAdapterItem() {
        mViews = new SparseArray<>();
    }

    @Override
    public void onBindViews(View root) {
        mRootView = root;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mRootView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    public TextView getTextView(int id) {
        return findView(id);
    }

    public Button getButton(int id) {
        return findView(id);
    }

    public ImageView getImageView(int id) {
        return findView(id);
    }

    public View getView(int viewId) {
        return findView(viewId);
    }

    public void setText(int viewId, String text) {
        getTextView(viewId).setText(text);
    }

}
