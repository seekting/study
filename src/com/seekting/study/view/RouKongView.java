
package com.seekting.study.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.seekting.study.R;

public class RouKongView extends TextView implements OnClickListener {

    private Paint mPaint;

    private NinePatchDrawable mCheckedDrawable;
    private NinePatchDrawable mUnCheckedDrawable;

    boolean isChecked = true;
    private NinePatchDrawable mCurrentDrawable;
    PorterDuffXfermode mPorterDuffXfermode;
    Bitmap bitmap;

    public RouKongView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setOnClickListener(this);

    }

    private void init() {
        mCheckedDrawable = (NinePatchDrawable) getResources().getDrawable(
                R.drawable.home_search_radio_checked);
        mUnCheckedDrawable = (NinePatchDrawable) getResources().getDrawable(
                R.drawable.home_search_radio_unchecked);
        mPorterDuffXfermode = new PorterDuffXfermode(Mode.XOR);
    }

    @Override
    public void draw(Canvas canvas) {
        onDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (isChecked) {
            mPaint = mCheckedDrawable.getPaint();
            mCurrentDrawable = mCheckedDrawable;
        } else {
            mPaint = mUnCheckedDrawable.getPaint();
            mCurrentDrawable = mUnCheckedDrawable;
        }
        mPaint.setXfermode(mPorterDuffXfermode);
        int sc = canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        mCurrentDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mCurrentDrawable.draw(canvas);
        mPaint.setTextSize(getTextSize());
        mPaint.setAntiAlias(true);
        // canvas.drawBitmap(bitmap, 0, 0, mPaint);
        // int bottom = (int) (mPaint.getFontMetrics().descent -
        // mPaint.getFontMetrics().top)+getPaddingTop();
        int bottom = (int) (0 - mPaint.getFontMetrics().top) + getPaddingTop();
        canvas.drawText(getText().toString(), getPaddingLeft(), bottom, mPaint);
        canvas.restoreToCount(sc);

    }

    @Override
    public void onClick(View v) {

        totol();
    }

    private void totol() {
        isChecked = !isChecked;
        invalidate();
    }

    public interface OnCheckedListener {
        public void onCheck(View v, boolean isChecked);
    }

}
