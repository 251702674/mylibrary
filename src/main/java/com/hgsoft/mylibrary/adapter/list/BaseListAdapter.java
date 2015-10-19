package com.hgsoft.mylibrary.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * ListView 极简适配器
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    private List<T> mData;
    private Context mContext;

    public BaseListAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        }
        onBindView(ViewHolderHelper.getViewHolderHelper(convertView), position);
        return convertView;
    }

    public abstract int getLayoutId();

    public abstract void onBindView(ViewHolderHelper holder, int position);


    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }
}
