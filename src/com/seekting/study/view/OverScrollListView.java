
package com.seekting.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class OverScrollListView extends ListView {

    private OnOverScrollListener mOnOverScrollListener;

    public OverScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        if (mOnOverScrollListener != null) {
            mOnOverScrollListener.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        }
    }

//    @Override
//    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
//            int scrollY, int scrollRangeX, int scrollRangeY,
//            int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
//        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
//                scrollRangeX, scrollRangeY, maxOverScrollX,
//                200, isTouchEvent);
//    }

    public static interface OnOverScrollListener {
        public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY);
    }

    public OnOverScrollListener getOnOverScrollListener() {
        return mOnOverScrollListener;
    }

    public void setOnOverScrollListener(OnOverScrollListener onOverScrollListener) {
        this.mOnOverScrollListener = onOverScrollListener;
    }
}
