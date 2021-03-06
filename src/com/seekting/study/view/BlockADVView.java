
package com.seekting.study.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seekting.study.R;

/**
 * 因为要有秒表效果,如果考虑用layout布局会出现不停重复layout反而会影响应用的性能,这边考虑用绘制的方式来做 广告拦截
 * 
 * @author 张兴挺
 */
public class BlockADVView extends LinearLayout implements OnClickListener, AnimatorListener {

    // Queue<SuperActivityToast> mQueue;

    public static final int ANIMATOR_TIME = 500;
    public static final int SHOW_TIME = 3000;
    /**
     * 被拦截的广告数
     */
    private int mBlockADVs;

    private Handler mHandler;

    /**
     * 拦截广告数左边的文字
     */
    private String mLeftText;
    /**
     * 拦截广告右边的文字
     */
    private String mRightText;

    /**
     * 拦截广告左边文字和右边文字的颜色
     */
    int mleftRightColor = Color.WHITE;
    /**
     * 拦截广告个数文字的颜色
     */
    int mCountColor = Color.YELLOW;

    /**
     * 拦截广告个数离左右两边文字的距离
     */
    int mCountPadding;

    /**
     * 画笔
     */
    Paint mPaint;

    ObjectAnimator mShowAnimator, mHideAnimator;

    public static final int HIDE_MESSSAGE = 0;
    public static final int INVALIDATE_MESSSAGE = 1;
    public static final int RESET_MESSSAGE = 2;
    public static final int FINISH_MESSAGE = 3;
    public static final int ISFINISH = 1;

    public static final String TAG = "BlockADVView";

    Status mStatus;

    TextView mText;
    View closeBtn;

    public static enum Status {

        hided, showing, hiding, showed, finishing;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
        Log.d(TAG, "setStatus " + status);
    }

    public BlockADVView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStatus(Status.hided);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HIDE_MESSSAGE:
                        Log.d(TAG, "处理隐藏消息");
                        hide(true, msg.arg1 == ISFINISH);

                        break;
                    case INVALIDATE_MESSSAGE:
                        refresh();
                        break;
                    case RESET_MESSSAGE: {
                        doReSet();
                        break;
                    }
                    case FINISH_MESSAGE: {

                        break;
                    }
                    default:
                        break;
                }
            }
        };

        mPaint = new Paint();
        float textSize = 30;
        mPaint.setTextSize(textSize);
        mLeftText = "已拦截";
        mRightText = "条广告";
        mCountPadding = 5;
        mPaint.setAntiAlias(true);
        mShowAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        mShowAnimator.setDuration(ANIMATOR_TIME);
        mHideAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        mHideAnimator.setDuration(ANIMATOR_TIME);
        mHideAnimator.addListener(this);
        mShowAnimator.addListener(this);
        setVisibility(View.GONE);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mText = (TextView) findViewById(R.id.text);
        closeBtn = findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(this);
        setOnClickListener(this);

    }

    //
    // @Override
    // protected void onDraw(Canvas canvas) {
    // super.onDraw(canvas);
    //
    // mBackgroundDrawable.draw(canvas);
    // mADLogo.draw(canvas);
    //
    // // Log.d(TAG, "onDraw" + mBlockADVs);
    // int width = getMeasuredWidth();
    // int height = getMeasuredHeight();
    // FontMetrics fontMetrics = mPaint.getFontMetrics();
    //
    // int baseLine = (int) ((height - (fontMetrics.ascent +
    // fontMetrics.descent)) / 2);
    // float leftTextWidth = mPaint.measureText(mLeftText);
    // float advCountWidth = mPaint.measureText(String.valueOf(mBlockADVs));
    // float rightTextWidth = mPaint.measureText(mRightText);
    // float allTextWidth = (leftTextWidth + advCountWidth +
    // rightTextWidth);
    //
    // mPaint.setColor(mleftRightColor);
    // float x = (width - allTextWidth) / 2;
    // canvas.drawText(mLeftText, x, baseLine, mPaint);
    // mPaint.setColor(mCountColor);
    // x = x + leftTextWidth + mCountPadding;
    // canvas.drawText(String.valueOf(mBlockADVs), x, baseLine,
    // mPaint);
    // mPaint.setColor(mleftRightColor);
    // x = x + advCountWidth + mCountPadding;
    // canvas.drawText(mRightText, x, baseLine,
    // mPaint);
    //
    // }

    @Override
    public void onClick(View v) {

        hide(true, true);
    }

    public void hide(final boolean animator, final boolean isFinish) {

        Log.d(TAG, "hide()方法" + "isFinish" + isFinish);

        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (animator) {
                    setVisibility(View.VISIBLE);
                    setAlpha(0);
                    mHideAnimator.start();
                    if (isFinish) {
                        // 如果是结束就再显示一段时间重置
                        mHandler.sendEmptyMessageDelayed(RESET_MESSSAGE, ANIMATOR_TIME);
                    }
                } else {
                    if (isFinish) {
                        reSet();
                    }
                    setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 重置
     */
    public void reSet() {
        mHandler.sendEmptyMessage(RESET_MESSSAGE);
    }

    private void doReSet() {
        Log.d(TAG, "重置");
        setVisibility(View.GONE);
        mHandler.removeMessages(HIDE_MESSSAGE);
        mHandler.removeMessages(INVALIDATE_MESSSAGE);
        mBlockADVs = 0;
        setStatus(Status.hided);
    }

    public void setAdvCount(int count, boolean isFinish) {

        // 如果正在隐藏或正在结束,就不刷新数字
        if (mStatus == Status.finishing || mStatus == Status.hiding) {
            mHandler.removeMessages(INVALIDATE_MESSSAGE);
            Log.d(TAG, "收到setAdvCount()因为界面正在隐藏,所以不做任何响应");
            return;
        } else if (mStatus == Status.hided) {
            show(true, isFinish);
            mBlockADVs = count;
            mHandler.sendEmptyMessage(INVALIDATE_MESSSAGE);
            return;
        }
        mBlockADVs = count;
        mHandler.sendEmptyMessage(INVALIDATE_MESSSAGE);
        if (isFinish) {
            finish();
        }
    }

    private void refresh() {
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(mLeftText);
        // "<font color=#E61A6B>红色代码</font>
        sbBuffer.append("<font color=#ffff66>");
        sbBuffer.append(mBlockADVs);
        sbBuffer.append("</font>");
        sbBuffer.append(mRightText);
        Spanned sp = Html.fromHtml(sbBuffer.toString());
        mText.setText(sp);
        Log.d(TAG, "是否显示" + getMeasuredHeight());
    }

    public void finish() {
        Log.d(TAG, "finish()");
        mHandler.removeMessages(FINISH_MESSAGE);
        mHandler.removeMessages(HIDE_MESSSAGE);

        setStatus(Status.finishing);
        Message message = mHandler.obtainMessage();
        message.arg1 = ISFINISH;
        message.what = HIDE_MESSSAGE;
        mHandler.sendMessageDelayed(message, SHOW_TIME);
        Log.d(TAG, "发送隐藏");

    }

    boolean needFinish;

    private void show(final boolean animator, final boolean isFinish) {
        Log.d(TAG, "show()");

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (getVisibility() == View.VISIBLE) {
                    setStatus(Status.showed);
                    return;
                }
                setVisibility(View.VISIBLE);
                if (animator) {
                    mShowAnimator.start();
                    needFinish = isFinish;
                } else {
                    if (isFinish) {
                        finish();
                    }
                }
            }
        });

    }

    @Override
    public void onAnimationStart(Animator animation) {

        if (animation == mHideAnimator) {
            setStatus(Status.hiding);
        } else if (animation == mShowAnimator) {
            setStatus(Status.showing);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {

        if (animation == mShowAnimator) {

            setStatus(Status.showed);
            if (needFinish) {
                finish();
                needFinish = false;
            }
        }
        if (animation == mHideAnimator) {
            setVisibility(View.GONE);
            setStatus(Status.hided);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetatch");
        mHandler.removeCallbacksAndMessages(null);
    }
}
