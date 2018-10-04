package com.saiyi.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class HorzonScrollLinearLayout extends LinearLayout {
    private int mLastX;
    private int mLastY;
    private int mCurrentX;
    private int mCurrentY;
    private int mTouchLastX;
    private int mTouchLastY;


    public HorzonScrollLinearLayout(Context context) {
        super(context);
    }

    public HorzonScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorzonScrollLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int scrollX = (int) event.getX();
        int scrollY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = mLastX - scrollX;
                scrollBy(deltaX, 0);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mLastX = scrollX;
        mLastY = scrollY;
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        mCurrentX = (int) ev.getX();
        mCurrentY = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //往左滑动为正
                int deltaX = mLastX - mCurrentX;
                int deltaY = mLastY - mCurrentY;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    isIntercept = true;
                } else {
                    isIntercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercept = false;
                break;
        }
        mLastX = mCurrentX;
        mLastY = mCurrentY;
        return isIntercept;
    }
}