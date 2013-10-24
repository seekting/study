
package com.seekting.study.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.seekting.study.R;

public class BrightValueSeekView extends LinearLayout implements AnimatorListener,
        OnSeekBarChangeListener {
    public static final String Tag = "BrightValueSeekView";
    public static final int ANIMATOR_TIME = 500;
    public static final int SLOWLY_HIDE_TIME = 4000;
    public static final int SHOW_TIME = 1000;
    private Handler mHandler;
    private ObjectAnimator mShowAnimator, mHideAnimator, mSlowlyHideAnimator;

    public static final int animate_to_show = 0;
    public static final int animate_to_hide = 1;
    public static final int no_animate_to_show = 2;
    public static final int no_animate_to_hide = 3;

    private SeekBar mSeekBar;
    private OnBrightnessAdjustSlidListener mOnBrightnessAdjustSlidListener;

    public static int MIN_BRIGHT = 5;
    public static int DEFAULT_BRIGHT = 60;
    public static int MAX_BRIGHT = 255;
    int currentBrightNess;

    /**
     * 隐藏,正在显示,显示完,正在显示并且在倒计时,正在隐藏
     * 
     * @author 张兴挺
     */
    private static enum Status {
        hided, showing, showed, hodding, hidding, slowlyHidding;
    }

    private Status mStatus;
    boolean mIsSeekBarAction = false;

    public BrightValueSeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void setStatus(Status status) {
        Log.d(Tag, "setStatus:" + status);
        mStatus = status;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setMax(MAX_BRIGHT - MIN_BRIGHT);
    }

    public void setAdjustProgress(int progress) {
        mSeekBar.setProgress(progress - MIN_BRIGHT);
        currentBrightNess = progress;
    }

    private void init() {
        mShowAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        mShowAnimator.setDuration(ANIMATOR_TIME);
        mHideAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        mHideAnimator.setDuration(ANIMATOR_TIME);
        mHideAnimator.addListener(this);

        mSlowlyHideAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        mSlowlyHideAnimator.setDuration(SLOWLY_HIDE_TIME);
        mSlowlyHideAnimator.addListener(this);

        mShowAnimator.addListener(this);
        setStatus(Status.hided);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case animate_to_show:
                        animateToShow();
                        break;
                    case animate_to_hide: {
                        animateToHide();
                        break;
                    }
                    case no_animate_to_show: {
                        setVisibility(View.VISIBLE);
                        resetHodding();
                        break;
                    }
                    case no_animate_to_hide: {
                        setVisibility(View.GONE);
                        break;
                    }

                    default:
                        break;
                }
            }
        };
    }

    public void show(boolean animate) {
        Log.d(Tag, "show() Status=" + mStatus);
        if (mStatus == Status.hidding || mStatus == Status.hided) {
            Message message = mHandler.obtainMessage();
            if (animate) {
                message.what = animate_to_show;
            } else {
                message.what = no_animate_to_show;
            }
            mHandler.sendMessage(message);
        } else if (mStatus == Status.showing) {

        }
        else {
            // hodding,showed都可以重新显示
            resetHodding();
        }
    }

    private void animateToShow() {
        setVisibility(View.VISIBLE);
        mShowAnimator.start();
    }

    public void hideQuickly() {
        if (mStatus == Status.hidding) {
            return;
        }
        mStatus = Status.hidding;
        if (mStatus == Status.showed || mStatus == Status.showing) {
            mShowAnimator.cancel();
        } else if (mStatus == Status.slowlyHidding) {

            mSlowlyHideAnimator.cancel();
        }
        mHandler.removeMessages(animate_to_hide);
        float alpha = getAlpha();
        Log.d(Tag, "hideQuickly()-alpha:" + alpha);
        mHideAnimator = ObjectAnimator.ofFloat(this, "alpha", alpha, 0f);
        mHideAnimator.setDuration(ANIMATOR_TIME);
        mHideAnimator.addListener(this);
        mHideAnimator.start();
    }

    public void hide(boolean animate) {

        Log.d(Tag, "hide()");
        if (mStatus == Status.hodding) {
            mHandler.removeMessages(animate_to_hide);
        } else if (mStatus == Status.showing || mStatus == Status.showed) {
            // 如果显示或显示中
            mHandler.removeMessages(animate_to_hide);
        } else if (mStatus == Status.hidding || mStatus == Status.hided
                || mStatus == Status.slowlyHidding) {
            return;
        }

        Message message = mHandler.obtainMessage();
        if (animate) {
            message.what = animate_to_hide;
        } else {
            message.what = no_animate_to_hide;
        }
        mHandler.sendMessage(message);
    }

    private void animateToHide() {
        Log.d(Tag, "animateToHide");
        setVisibility(View.VISIBLE);
        // mHideAnimator.start();
        mSlowlyHideAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (animation == mShowAnimator) {
            setStatus(Status.showing);
        } else if (animation == mHideAnimator) {
            setStatus(Status.hidding);
        } else if (mSlowlyHideAnimator == animation) {
            setStatus(Status.slowlyHidding);
        }

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (animation == mShowAnimator) {
            if (mStatus == Status.showing) {
                setStatus(Status.showed);
                // 显示完后发延时消息来让它hold一段时间后隐藏
                resetHodding();
            }

        } else if (animation == mHideAnimator) {
            if (mStatus == Status.hidding) {
                setStatus(Status.hided);
                setVisibility(View.GONE);
            }
        } else if (animation == mSlowlyHideAnimator) {
            if (mStatus == Status.slowlyHidding) {
                setStatus(Status.hided);
                setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

        if (mSlowlyHideAnimator == animation) {
            setVisibility(View.VISIBLE);
            setStatus(Status.hodding);
            if (mStatus == Status.hidding) {

            } else {
                //当用户托seekbar的时候还会激发显示
                setAlpha(1);
            }

        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(Tag, "onProgressChanged" + progress);
        if (mOnBrightnessAdjustSlidListener != null) {

            currentBrightNess = MIN_BRIGHT + progress;
            mOnBrightnessAdjustSlidListener.onBrightSlide(currentBrightNess);
        }
      if (mStatus == Status.showed || mStatus == Status.hodding
                || mStatus == Status.slowlyHidding) {
            resetHodding();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(Tag, "onStartTrackingTouch");
        mIsSeekBarAction = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        Log.d(Tag, "onStopTrackingTouch");
        mIsSeekBarAction = false;
        if (mOnBrightnessAdjustSlidListener != null) {
            mOnBrightnessAdjustSlidListener.onAdjustBrihgtComplemete(currentBrightNess);
        }

    }

    public boolean isSeekbarAction() {
        return mIsSeekBarAction;

    }

    private void resetHodding() {
        Log.d(Tag, "resetHodding");
        if (mSlowlyHideAnimator.isRunning()) {
            mSlowlyHideAnimator.cancel();
        }
        mHandler.removeMessages(animate_to_hide);
        Message message = mHandler.obtainMessage();
        message.what = animate_to_hide;
        mHandler.sendMessageDelayed(message, SHOW_TIME);
        setStatus(Status.hodding);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                hideQuickly();
            }
        }
        return super.onTouchEvent(event);
    }

    public OnBrightnessAdjustSlidListener getOnBrightnessAdjustSlidListener() {
        return mOnBrightnessAdjustSlidListener;
    }

    public void setOnBrightnessAdjustSlidListener(
            OnBrightnessAdjustSlidListener mOnBrightnessAdjustSlidListener) {
        this.mOnBrightnessAdjustSlidListener = mOnBrightnessAdjustSlidListener;
    }

    /**
     * @author 张兴挺 当弹出夜间模式的dialog时，对dialog作相应的操作进行响应
     */
    public static interface OnBrightnessAdjustSlidListener {

        /**
         * 当托动seekBar时会响应此方法
         * 
         * @param progress
         */
        public void onBrightSlide(int progress);

        /**
         * 当点确定的时候响应此方法
         * 
         * @param progress
         * @param notPromptAdjustBright
         */
        public void onAdjustBrihgtComplemete(int progress);

    }
}
