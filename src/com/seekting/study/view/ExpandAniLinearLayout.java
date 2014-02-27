
package com.seekting.study.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
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
import android.widget.TextView;

import com.seekting.study.R;

public class ExpandAniLinearLayout extends LinearLayout implements AnimatorListener,
        AnimatorUpdateListener, OnClickListener {

    private static final String TAG = "ExpandAniLinearLayout";
    private LayoutInflater mLayoutInflater;
    private static final float END_FRAME = 100;
    private static final int BACK_TIME = 100;
    private static final int SCALE_TIME = 100;
    private static final int DOWN_TIME = 1000;
    private PropertyValuesHolder mTranslate;
    private ValueAnimator mAnimator;
    private int mItemHeight;
    private onItemSelectListener onItemSelectListener;
    private int mSelectPosition;
    private int mItemBgRes;
    private static final float overAni = 0.1f;
    private boolean mCareAni = true;
    private int mCurrentbackAni = -1;

    public ExpandAniLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflater = LayoutInflater.from(context);
        mItemHeight = (int) (context.getResources().getDimension(R.dimen.search_item_height));
        setChildrenDrawingOrderEnabled(true);
        Keyframe maskBegin = Keyframe.ofFloat(0f, 0);
        Keyframe maskEnd = Keyframe.ofFloat(1f, END_FRAME);
        mTranslate = PropertyValuesHolder.ofKeyframe("translate", maskBegin, maskEnd);
        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, mTranslate);
        mAnimator.setDuration(DOWN_TIME);
        mAnimator.addUpdateListener(this);
        mAnimator.addListener(this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAnimator.isRunning()) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    private void backAnimator(View child) {
        ObjectAnimator result = ObjectAnimator.ofFloat(child, "translationY",
                child.getTranslationY(), 0);
        result.setDuration(BACK_TIME);
        result.setInterpolator(new OvershootInterpolator(6));
        result.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator objectAnimator = (ObjectAnimator) animation;
                AnimatorSet animatorSet = new AnimatorSet();
                View child = (View) objectAnimator.getTarget();
                if (indexOfChild(child) == mSelectPosition) {
                    child.setSelected(true);
                    View bg = child.findViewById(R.id.search_item_bg);
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(bg, "alpha",
                            0, 1);
                    animatorSet.setDuration(SCALE_TIME);
                    scaleX.start();
                }
                View title = child.findViewById(R.id.title);
                title.setPivotX(0);
                title.setPivotY(0);
                title.setVisibility(View.VISIBLE);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(title, "scaleX",
                        0, 1);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(title, "scaleY",
                        0, 1);
                animatorSet.play(scaleX);
                animatorSet.play(scaleY);
                animatorSet.setDuration(SCALE_TIME);
                animatorSet.start();
            }

        });
        result.start();
    }

    public void addItem(int iconId, int titleId) {
        View child = mLayoutInflater.inflate(R.layout.search_engine_ani_item, null);
        child.findViewById(R.id.search_item_bg).setBackgroundResource(mItemBgRes);
        TextView title = (TextView) child.findViewById(R.id.title);
        title.setText(titleId);
        title.setVisibility(View.INVISIBLE);
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
            child.findViewById(R.id.title).setVisibility(View.INVISIBLE);
            child.setSelected(false);
        }
    }

    public void startExpand() {
        hideAll();
        mCareAni = true;
        mCurrentbackAni=-1;
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

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public void onAnimationUpdate(ValueAnimator animation) {

        if (mCareAni && animation == mAnimator) {
            int aniCount = getChildCount() + 1;

            float y = Float.valueOf(animation.getAnimatedValue().toString());
            int index = (int) (y * aniCount / END_FRAME);

            if (index < aniCount) {
                float childAnimatorY = (y - index * END_FRAME / aniCount)
                        / (END_FRAME / aniCount);
                float currentTranslateY = (childAnimatorY - 1) * mItemHeight;
                if (index > 0) {
                    int preIndex = index - 1;
                    View pre = getChildAt(preIndex);
                    if (childAnimatorY < overAni) {
                        float preTranslateY = childAnimatorY * mItemHeight;
                        pre.setTranslationY(preTranslateY);
                    } else {
                        if (mCurrentbackAni < preIndex) {
                            mCurrentbackAni = preIndex;
                            backAnimator(pre);
                        }
                    }

                }
                if (index < getChildCount()) {
                    View current = getChildAt(index);
                    current.setVisibility(View.VISIBLE);
                    translateY(current, currentTranslateY);
                }

            }
        }
    }

    private void translateY(View target, float y) {
        if (target != null) {
            target.setTranslationY(y);
        }

    }

    @Override
    public void onClick(View v) {
        if (onItemSelectListener != null) {
            int position = indexOfChild(v);
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
