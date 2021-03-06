
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
