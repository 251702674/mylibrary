package com.hgsoft.mylibrary.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 高度自适应的ViewPager--高度由内容的最大值决定
 */
public class WrapContentHeightViewPager extends ViewPager {

    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int maxHeight = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
//            child.measure(widthMeasureSpec,
//                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            child.measure(0, 0);
            int h = child.getMeasuredHeight();
            int w = child.getMeasuredWidth();
            int width = child.getWidth();
            int height = width * h / w;
            if (height > maxHeight) {//采用最大的view的高度。
                maxHeight = height;
            }

        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

