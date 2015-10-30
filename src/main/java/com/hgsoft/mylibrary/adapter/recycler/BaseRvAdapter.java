package com.hgsoft.mylibrary.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hgsoft.mylibrary.adapter.item.AbstractIAdapterItem;
import com.hgsoft.mylibrary.adapter.item.IAdapterItem;
import com.hgsoft.mylibrary.adapter.util.AdapterItemUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/10/29.
 */
public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter<RvViewHolder<T>> {

    private List<T> mData; // 数据集
    private Object mItemType; // Item 类型
    private AdapterItemUtil mItemUtil = new AdapterItemUtil();

    public BaseRvAdapter(List<T> data) {
        mData = data;
    }

    @Override
    public int getItemViewType(int position) {
        mItemType = getItemType(mData.get(position));
        return mItemUtil.getIntType(mItemType);
    }

    @Override
    public RvViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RvViewHolder<>(parent, getAdapterItem(mItemType));
    }

    @Override
    public void onBindViewHolder(RvViewHolder<T> holder, int position) {
        IAdapterItem<T> IAdapterItem = holder.getAdapterItem();
        IAdapterItem.onUpdateViews(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /* 根据对象，判断Item类型 */
    public Object getItemType(T object) {
        return null;
    }

    /* 根据类型返回对应的ItemView */
    public abstract AbstractIAdapterItem<T> getAdapterItem(Object itemType);

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }

}
