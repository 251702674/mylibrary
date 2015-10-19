package com.hgsoft.mylibrary.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hgsoft.mylibrary.R;
import com.hgsoft.mylibrary.adapter.recycler.BasePageAdapter;
import com.hgsoft.mylibrary.utils.ScreenUtil;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import viewpagerindicator.CirclePageIndicator;

/**
 * 轮播焦点图
 */
public class CarouselView extends LinearLayout {
    public CarouselView(Context context) {
        super(context);
        init(context);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("NewApi")
    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("NewApi")
    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private ViewPager carouselpager;
    private CirclePageIndicator indicator;
    private ScheduledExecutorService scheduledExecutorService;

    private void init(Context context) {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        LayoutInflater.from(context).inflate(R.layout.carouselview_layout, this, true);
        carouselpager = (ViewPager) findViewById(R.id.carouselpager);
        int screenWidth = ScreenUtil.getScreenWidth(context);
        int height = screenWidth * 338 / 1080;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        carouselpager.setLayoutParams(layoutParams);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
    }

    /**
     * 停止切换
     */
    public void stopScroller() {
        if (scheduledExecutorService != null
                && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdownNow();
        }
    }

    /**
     * 开启轮播
     */
    public void startScroller() {
        currentItem = 0;
        if (scheduledExecutorService != null
                && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdownNow();
            scheduledExecutorService = null;
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        if (!scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.scheduleAtFixedRate(
                    new ScrollBannerTask(), 6, 6, TimeUnit.SECONDS);
        }
    }

    /**
     * 当前轮播图的索引
     */
    private int currentItem = 0;

    private IPageOnPageChangeListener listener;
    private List<View> viewLists = null;

    public void setAdapter(BasePageAdapter<View> adapter) {
        carouselpager.setAdapter(adapter);
        this.viewLists = adapter.getList();
        indicator.setViewPager(carouselpager);
        indicator.notifyDataSetChanged();
        indicator.setOnPageChangeListener(new PageChangeListener());
    }

    private class PageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (listener != null) {
                listener.onPageScrollStateChanged(arg0);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (listener != null) {
                listener.onPageScrolled(arg0, arg1, arg2);
            }
        }

        @Override
        public void onPageSelected(int arg0) {
            currentItem = arg0;
            if (listener != null) {
                listener.onPageSelected(arg0);
            }
        }
    }

    public interface IPageOnPageChangeListener {
        void onPageScrollStateChanged(int arg0);

        void onPageScrolled(int arg0, float arg1, int arg2);

        void onPageSelected(int arg0);
    }

    private class ScrollBannerTask implements Runnable {
        public void run() {
            synchronized (carouselpager) {
                currentItem = (currentItem + 1) % viewLists.size();
                currHandler.sendEmptyMessage(25);
            }
        }
    }

    private Handler currHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 25:
                    carouselpager.setCurrentItem(currentItem);
                    break;
                default:
                    break;
            }
        }

        ;
    };

}