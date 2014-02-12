
package com.seekting.study.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.seekting.study.R;

public class ListViewFastScroller extends LinearLayout {

    private View mScrollBar;
    private ListView mListView;
    private TextView mSectionPositionTextView;

    public ListViewFastScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mScrollBar = findViewById(R.id.fast_scroller_bar);
        mSectionPositionTextView = (TextView) findViewById(R.id.section_position);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount, String section) {

        int parentHeight = getMeasuredHeight();
        int barHeight = mScrollBar.getMeasuredHeight();
        float translationY = ((firstVisibleItem * 1f) / (totalItemCount - visibleItemCount))
                * (parentHeight - barHeight);
        mScrollBar.setTranslationY(translationY);
        if (!TextUtils.isEmpty(section)) {
            mSectionPositionTextView.setText(section);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        if (x < mSectionPositionTextView.getRight()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float touchY = event.getY();
                int parentHeight = getMeasuredHeight();
                int barHeight = mScrollBar.getMeasuredHeight();
                int position = (int) ((mListView.getCount() - mListView.getChildCount())
                        * (touchY - barHeight * 0.5) / (parentHeight - barHeight));
                mListView.setSelection(position);
                setSectionPostionVisibility(View.VISIBLE);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setSectionPostionVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        return true;
    }

    private void setSectionPostionVisibility(int visibility) {
        mSectionPositionTextView.setVisibility(visibility);
    }

    public ListView getListView() {
        return mListView;
    }

    public void setListView(ListView listView) {
        this.mListView = listView;
    }

}
