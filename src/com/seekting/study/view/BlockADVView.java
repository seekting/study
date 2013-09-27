
package com.seekting.study.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.seekting.study.R;

/**
 * ��ΪҪ�����Ч��,���������layout���ֻ���ֲ�ͣ�ظ�layout������Ӱ��Ӧ�õ�����,��߿����û��Ƶķ�ʽ���� �������
 * 
 * @author ����ͦ
 */
public class BlockADVView extends View implements OnClickListener, AnimatorListener {

    // Queue<SuperActivityToast> mQueue;

    public static final int ANIMATOR_TIME = 500;
    public static final int SHOW_TIME = 3000;
    /**
     * �����صĹ����
     */
    private int mBlockADVs;

    private Handler mHandler;

    /**
     * ���ع������ߵ�����
     */
    private String mLeftText;
    /**
     * ���ع���ұߵ�����
     */
    private String mRightText;

    /**
     * ���ع��������ֺ��ұ����ֵ���ɫ
     */
    int mleftRightColor = Color.WHITE;
    /**
     * ���ع��������ֵ���ɫ
     */
    int mCountColor = Color.YELLOW;

    /**
     * ���ع������������������ֵľ���
     */
    int mCountPadding;

    /**
     * ����
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

    enum Status {

        hided, showing, hiding, showed, finishing;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }

    public BlockADVView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mStatus = Status.hided;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HIDE_MESSSAGE:
                        Log.d(TAG, "����������Ϣ");
                        hide(true, msg.arg1 == ISFINISH);

                        break;
                    case INVALIDATE_MESSSAGE:
                        invalidate();
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
        mPaint.setTextSize(30);
        mLeftText = "���ص�";
        mRightText = "�����";
        mCountPadding = 5;
        mPaint.setAntiAlias(true);
        setBackgroundResource(R.drawable.block_adv_toast_bg);
        setOnClickListener(this);
        mShowAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        mShowAnimator.setDuration(ANIMATOR_TIME);
        mHideAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        mHideAnimator.setDuration(ANIMATOR_TIME);
        mHideAnimator.addListener(this);
        mShowAnimator.addListener(this);
        setVisibility(View.GONE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Log.d(TAG, "onDraw" + mBlockADVs);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        FontMetrics fontMetrics = mPaint.getFontMetrics();

        int baseLine = (int) ((height - (fontMetrics.ascent + fontMetrics.descent)) / 2);
        float leftTextWidth = mPaint.measureText(mLeftText);
        float advCountWidth = mPaint.measureText(String.valueOf(mBlockADVs));
        float rightTextWidth = mPaint.measureText(mRightText);
        float allTextWidth = (leftTextWidth + advCountWidth + rightTextWidth);

        mPaint.setColor(mleftRightColor);
        float x = (width - allTextWidth) / 2;
        canvas.drawText(mLeftText, x, baseLine, mPaint);
        mPaint.setColor(mCountColor);
        x = x + leftTextWidth + mCountPadding;
        canvas.drawText(String.valueOf(mBlockADVs), x, baseLine,
                mPaint);
        mPaint.setColor(mleftRightColor);
        x = x + advCountWidth + mCountPadding;
        canvas.drawText(mRightText, x, baseLine,
                mPaint);

    }

    @Override
    public void onClick(View v) {

        hide(true, true);
    }

    public void hide(final boolean animator, final boolean isFinish) {

        Log.d(TAG, "hide()����" + "isFinish" + isFinish);

        post(new Runnable() {

            @Override
            public void run() {
                if (animator) {
                    setVisibility(View.VISIBLE);
                    setAlpha(0);
                    mHideAnimator.start();
                    if (isFinish) {
                        // ����ǽ���������ʾһ��ʱ������
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
     * ����
     */
    public void reSet() {
        mHandler.sendEmptyMessage(RESET_MESSSAGE);
    }

    private void doReSet() {
        Log.d(TAG, "����");
        setVisibility(View.GONE);
        mHandler.removeMessages(HIDE_MESSSAGE);
        mHandler.removeMessages(INVALIDATE_MESSSAGE);
        mBlockADVs = 0;
        mStatus = Status.hided;

    }

    public void setAdvCount(int count, boolean isFinish) {

        // ����������ػ����ڽ���,�Ͳ�ˢ������
        if (mStatus == Status.finishing || mStatus == Status.hiding) {
            mHandler.removeMessages(INVALIDATE_MESSSAGE);
            Log.d(TAG, "�յ�setAdvCount()��Ϊ������������,���Բ����κ���Ӧ");
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

    public void finish() {
        Log.d(TAG, "finish()");
        mHandler.removeMessages(FINISH_MESSAGE);
        mHandler.removeMessages(HIDE_MESSSAGE);
        mStatus = Status.finishing;
        Message message = mHandler.obtainMessage();
        message.arg1 = ISFINISH;
        message.what = HIDE_MESSSAGE;
        mHandler.sendMessageDelayed(message, SHOW_TIME);
        Log.d(TAG, "��������");

    }

    private void show(final boolean animator, boolean isFinish) {
        Log.d(TAG, "show()");

        post(new Runnable() {
            @Override
            public void run() {
                if (getVisibility() == View.VISIBLE) {
                    mStatus = Status.showed;
                    return;
                }
                setVisibility(View.VISIBLE);
                if (animator) {
                    mShowAnimator.start();
                }
            }
        });
        if (isFinish) {
            finish();
            return;
        }

    }

    @Override
    public void onAnimationStart(Animator animation) {

        if (animation == mHideAnimator) {
            mStatus = Status.hiding;
        } else if (animation == mShowAnimator) {
            mStatus = Status.showing;
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {

        if (animation == mShowAnimator) {

            mStatus = Status.showed;
        }
        if (animation == mHideAnimator) {
            setVisibility(View.GONE);
            mStatus = Status.hided;
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
