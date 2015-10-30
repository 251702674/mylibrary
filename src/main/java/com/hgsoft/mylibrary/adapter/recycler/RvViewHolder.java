package com.hgsoft.mylibrary.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hgsoft.mylibrary.adapter.item.AbstractIAdapterItem;

/**
 *
 */
public class RvViewHolder<T> extends RecyclerView.ViewHolder {

    private AbstractIAdapterItem<T> mAdapterItem;

    public RvViewHolder(ViewGroup parent, AbstractIAdapterItem<T> adapterItem) {
        super(LayoutInflater.from(parent.getContext()).inflate(adapterItem.getLayoutResId(), parent, false));
        mAdapterItem = adapterItem;
        mAdapterItem.onBindViews(itemView);
        mAdapterItem.onSetViews();
    }

    public AbstractIAdapterItem<T> getAdapterItem() {
        return mAdapterItem;
    }

}
