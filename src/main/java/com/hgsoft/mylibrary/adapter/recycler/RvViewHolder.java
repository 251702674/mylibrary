package com.hgsoft.mylibrary.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hgsoft.mylibrary.adapter.AdapterItem;

/**
 * Created by Administrator on 2015/10/29.
 */
public class RvViewHolder<T> extends RecyclerView.ViewHolder {


    private AdapterItem<T> mAdapterItem;

    public RvViewHolder(ViewGroup parent, AdapterItem<T> adapterItem) {
        super(LayoutInflater.from(parent.getContext()).inflate(adapterItem.getLayoutResId(), parent, false));
        mAdapterItem = adapterItem;
        mAdapterItem.onBindViews(itemView);
        mAdapterItem.onSetViews();
    }

    public AdapterItem<T> getAdapterItem() {
        return mAdapterItem;
    }

}
