package com.hgsoft.mylibrary.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.hgsoft.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

public class MyLinearLayout extends LinearLayout {

    private LinearLayout mHeadView; // 头部可伸缩部分
    private ListView mListView; // 内容部分
    private int mHeadViewHeight; // 头部空间初始高度
    private float mDownY = 0; // 首次触摸Y轴坐标
    private float mLastY = 0; // 上一次Y轴坐标
    private float mLastSecondY = 0; // 上上次Y轴坐标
    private boolean mHeadViewHide = false; // 头部控件是否隐藏

    public MyLinearLayout(Context context) {
        super(context);
        initView(context);
    }


    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.my_linear_layout, this, true);
        mHeadView = (LinearLayout) findViewById(R.id.ll_head);
        mListView = (ListView) findViewById(R.id.list_view);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            strings.add("index : " + i);
        }
        mListView.setAdapter(new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, strings));
    }

    public void getHeadViewHeight() {
        if (mHeadView.getHeight() <= 0) {
            throw new IllegalArgumentException("height = 0");
        }
        mHeadViewHeight = mHeadView.getHeight();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        System.out.println("onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mHeadViewHeight == 0) {
            getHeadViewHeight();
        }
        int action = ev.getAction();
        if (mHeadView.getHeight() >= ev.getY()) {
            return super.dispatchTouchEvent(ev);
        }
        if (action == MotionEvent.ACTION_DOWN) {
            mDownY = ev.getY();
            mLastY = mDownY;
            mLastSecondY = mDownY;
        } else if (action == MotionEvent.ACTION_MOVE) {
            int offset = (int) (mDownY - ev.getY());
            if (offset > 0 && offset <= mHeadViewHeight && !mHeadViewHide) {
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeadViewHeight - offset);
                mHeadView.setLayoutParams(params);
            } else if (offset < 0 && -offset <= mHeadViewHeight && mHeadViewHide) {
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -offset);
                mHeadView.setLayoutParams(params);
            }
            mLastSecondY = mLastY;
            mLastY = ev.getY();
        } else if (action == MotionEvent.ACTION_UP) {
            int offset = (int) (mLastSecondY - ev.getY());
            if (offset >= 2) {
                setHeadViewState(mHeadView.getHeight(), 0);
            } else if (offset <= -2) {
                setHeadViewState(mHeadView.getHeight(), mHeadViewHeight);
            } else if (mHeadViewHide) {
                setHeadViewState(mHeadView.getHeight(), 0);
            } else {
                setHeadViewState(mHeadView.getHeight(), mHeadViewHeight);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 更改HeadView 的状态
     *
     * @param from 改变前高度
     * @param to   改变后高度
     */
    private synchronized void setHeadViewState(int from, final int to) {
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.setDuration(240);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
                mHeadView.setLayoutParams(params);
            }
        });
        animator.start();
        if (to == 0) {
            mHeadViewHide = true;
        } else {
            mHeadViewHide = false;
        }
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
    }
}
