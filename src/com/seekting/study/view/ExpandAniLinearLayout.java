
package com.seekting.study.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.seekting.study.R;

public class ExpandAniLinearLayout extends LinearLayout implements AnimatorListener,
        AnimatorUpdateListener, OnClickListener {

    LayoutInflater mLayoutInflater;
    private static final float END_FRAME = 100;
    private static final int BACK_TIME = 300;
    private static final int DOWN_TIME = 1500;
    PropertyValuesHolder mTranslate;
    ValueAnimator mAnimator;
    int currentIndex = 0;
    private int mItemHeight;
    private onItemSelectListener onItemSelectListener;
    private int mSelectPosition;
    private int mItemBgRes;

    public ExpandAniLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflater = LayoutInflater.from(context);
        mItemHeight = (int) (context.getResources().getDimension(R.dimen.search_item_height));
        setChildrenDrawingOrderEnabled(true);
        Keyframe maskBegin = Keyframe.ofFloat(0f, 0);
        Keyframe maskEnd = Keyframe.ofFloat(1f, END_FRAME);
        mTranslate = PropertyValuesHolder.ofKeyframe("translate", maskBegin, maskEnd);
        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, mTranslate);
        mAnimator.setInterpolator(new OvershootInterpolator());
        mAnimator.setDuration(DOWN_TIME);
        mAnimator.addUpdateListener(this);
        mAnimator.addListener(this);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mAnimator.isRunning()) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    private void backAnimator(View child) {
        ObjectAnimator result = ObjectAnimator.ofFloat(child, "translationY",
                child.getTranslationY(), -1 * child.getTranslationY(), 0);
        result.setDuration(BACK_TIME);
        result.setInterpolator(new OvershootInterpolator());
        result.start();
    }

    private void backEndAnimator(View child) {
        ObjectAnimator result = ObjectAnimator.ofFloat(child, "translationY",
                child.getTranslationY(), 0);
        result.setDuration(DOWN_TIME / getChildCount());
        result.setInterpolator(new OvershootInterpolator());
        result.start();
    }

    public void addItem(int iconId, int titleId) {
        View child = mLayoutInflater.inflate(R.layout.search_engine_ani_item, null);
        child.setBackgroundResource(mItemBgRes);
        ImageView icon = (ImageView) child.findViewById(R.id.icon);
        icon.setImageResource(iconId);
        int height = (int) getResources().getDimension(R.dimen.search_item_height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height);
        addView(child, params);
        child.setOnClickListener(this);

    }

    public void setSelectPosition(int position) {
        this.mSelectPosition = position;
    }

    private void hideAll() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setVisibility(View.INVISIBLE);
        }
    }

    public void startExpand() {
        hideAll();
        currentIndex = 0;
        mAnimator.start();

    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int result = childCount - i - 1;
        return result;

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

        if (animation == mAnimator) {
            updateSelectBackground();
        }
    }

    private void updateSelectBackground() {

        getChildAt(mSelectPosition).setSelected(true);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public void onAnimationUpdate1(ValueAnimator animation) {

        if (animation == mAnimator) {
            if (currentIndex == getChildCount() - 1) {
                return;
            }
            float y = Float.valueOf(animation.getAnimatedValue().toString());
            // 拿到第几个动画
            int index = (int) (y * getChildCount() / END_FRAME);

            if (index < getChildCount()) {
                float childAnimatorY = (y - index * END_FRAME / getChildCount())
                        / (END_FRAME / getChildCount());
                float currentTranslateY = (childAnimatorY - 1) * mItemHeight;
                if (index > 0) {
                    View pre = getChildAt(index - 1);
                    if (childAnimatorY < 0.5) {
                        float preTranslateY = childAnimatorY * mItemHeight;
                        pre.setTranslationY(preTranslateY);
                    } else {
                        backAnimator(pre);
                    }

                }
                View current = getChildAt(index);
                current.setTranslationY(currentTranslateY);

            } else {
            }
        }
    }

    public void onAnimationUpdate(ValueAnimator animation) {

        if (animation == mAnimator) {
            if (currentIndex == getChildCount() - 1) {
                return;
            }
            float y = Float.valueOf(animation.getAnimatedValue().toString());
            // 拿到第几个动画
            int index = (int) (y * getChildCount() / END_FRAME);

            if (index < getChildCount()) {
                // System.out.println("xxx" + index);
                if (index != currentIndex) {
                    View prechild = getChildAt(currentIndex);
                    backAnimator(prechild);
                    System.out.println("back" + currentIndex);
                    currentIndex = index;
                }
                View child = getChildAt(index);
                child.setVisibility(View.VISIBLE);
                float childAnimatorY = (y - index * END_FRAME / getChildCount())
                        / (END_FRAME / getChildCount());
                float translateY = (childAnimatorY - 1) * mItemHeight;
                // System.out.println("xxx" + translateY + "index" + index);
                if (index == getChildCount() - 1) {
                    child.setTranslationY(-mItemHeight);
                    backEndAnimator(getChildAt(index));
                    System.out.println("start__");
                } else {
                    child.setTranslationY(translateY);
                }

            } else {
                if (index == getChildCount() && currentIndex != index) {
                    backAnimator(getChildAt(3));
                    currentIndex = index;
                }

            }
        }
    }

    @Override
    public void onClick(View v) {

        if (onItemSelectListener != null) {
            int position = indexOfChild(v);
//            for (int i = 0; i < getChildCount(); i++) {
//                View child = getChildAt(i);
//                child.setSelected(i == position);
//            }
            onItemSelectListener.onSelect(position);
        }
    }

    public onItemSelectListener getOnItemSelectListener() {
        return onItemSelectListener;
    }

    public void setOnItemSelectListener(onItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public interface onItemSelectListener {
        public void onSelect(int position);

    }

    public int getItemBgRes() {
        return mItemBgRes;
    }

    public void setItemBgRes(int itemBgRes) {
        this.mItemBgRes = itemBgRes;
    }

}
