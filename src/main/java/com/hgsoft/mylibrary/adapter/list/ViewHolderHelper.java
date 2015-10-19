package com.hgsoft.mylibrary.adapter.list;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ListView 极简适配辅助类
 */
public class ViewHolderHelper {

    private SparseArray<View> mViewSparseArray;
    private View mView;

    private ViewHolderHelper(View view) {
        this.mView = view;
        mViewSparseArray = new SparseArray<>();
    }

    public static ViewHolderHelper getViewHolderHelper(View view) {
        ViewHolderHelper viewHolderHelper = (ViewHolderHelper) view.getTag();
        if (viewHolderHelper == null) {
            System.out.println("new ViewHolderHelper()");
            viewHolderHelper = new ViewHolderHelper(view);
            view.setTag(viewHolderHelper);
        }
        return viewHolderHelper;
    }

    public <T extends View> T get(int id) {
        View childView = mViewSparseArray.get(id);
        if (childView == null) {
            childView = mView.findViewById(id);
            mViewSparseArray.put(id, childView);
        }
        return (T) childView;
    }

    public TextView getTextView(int id) {
        return get(id);
    }

    public Button getButton(int id) {
        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int  id,CharSequence charSequence){
        getTextView(id).setText(charSequence);
    }

    public View getView(int id) {
        return get(id);
    }

}
