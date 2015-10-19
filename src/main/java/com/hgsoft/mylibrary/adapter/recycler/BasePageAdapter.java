package com.hgsoft.mylibrary.adapter.recycler;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;


public abstract class BasePageAdapter<T extends View> extends PagerAdapter {

    private List<T> mList = new LinkedList<T>();

    private List<String> titles;

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mList.get(position), 0);
        return mList.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position));
    }

    /**
     * 获取数据集合
     *
     * @return
     */
    public List<T> getList() {
        return mList;
    }

    /**
     * 追加list数据集合 默认像末端追加
     *
     * @param list
     */
    public void appendToList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 追加到头部
     *
     * @param list
     */
    public void appendToTopList(List<T> list) {
        if (list == null) {
            return;
        }
        appendToList(list, 0);
        notifyDataSetChanged();
    }

    /**
     * 追加到制定位置
     *
     * @param list
     * @param postion
     */
    public void appendToList(List<T> list, int postion) {
        if (postion < 0 || postion > list.size()) {
            throw new RuntimeException("illegal postion");
        }
        mList.addAll(postion, list);
    }

    /**
     * 清空数据源
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * @return the mList
     */
    public List<T> getmList() {
        return mList;
    }

    /**
     * @param mList the mList to set
     */
    public void setmList(List<T> mList) {
        this.mList = mList;
    }

    /**
     * @return the titles
     */
    public List<String> getTitles() {
        return titles;
    }

    /**
     * @param titles the titles to set
     */
    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

}