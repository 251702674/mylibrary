package com.hgsoft.mylibrary.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hgsoft.mylibrary.R;


public class MyLinearLayoutPull extends LinearLayout {

    private LinearLayout mHeadView;
    private View myHeadView;
    private int mHeadViewHeight; // 头部View初始高度
    private float mDownY = 0; // 首次触摸Y轴坐标
    private float mLastY = 0; // 上一次Y轴坐标
    private float mLastSecondY = 0; // 上上次Y轴坐标
    private boolean mHeadViewHide = false; // 头部控件是否隐藏
    private int touchOffset = 0;

    public MyLinearLayoutPull(Context context) {
        super(context);
        initView(context);
    }

    public MyLinearLayoutPull(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        getAttrs(context, attrs);
    }

    public MyLinearLayoutPull(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        getAttrs(context, attrs);
    }

    private void initView(Context context) {
        View rootView = inflate(context, R.layout.my_linear_layout_2, this);
        mHeadView = (LinearLayout) rootView.findViewById(R.id.ll_head);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyLinearLayoutPull);
        int headViewId = ta.getResourceId(R.styleable.MyLinearLayoutPull_head_view, -1);
        ta.recycle();
        if (headViewId != -1) {
            myHeadView = inflate(context, headViewId, mHeadView);
        }
    }

    public View getmHeadView() {
        return myHeadView;
    }

    /* 获取头控件初始高度 */
    private void getHeadViewOriginalHeight() {
        mHeadViewHeight = mHeadView.getHeight();
    }

    public int getTuochbleOffset() {
        return touchOffset;
    }

    public void setTuochbleOffset(int offset) {
        this.touchOffset = offset;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mHeadViewHeight == 0) {
            getHeadViewOriginalHeight();
        }
        int action = ev.getAction();
        int touchLimit = mHeadView.getHeight() + touchOffset;
        if (touchLimit >= ev.getY()) {
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
    }

}
