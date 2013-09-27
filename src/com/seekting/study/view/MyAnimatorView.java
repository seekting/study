
package com.seekting.study.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ViewAnimator;


public class MyAnimatorView extends ViewAnimator implements OnClickListener {
    Button b1, b2;

    public MyAnimatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TranslateAnimation fadeIn = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        fadeIn.setDuration(200);

        TranslateAnimation fadeOut = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        fadeOut.setDuration(200);

        setInAnimation(fadeIn);
        setOutAnimation(fadeOut);
        b1 = new Button(context);
        b1.setText("1");
        addView(b1);
        b2 = new Button(context);
        b2.setText("2");
        addView(b2);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (b1 == v) {
            showNext();
        }
        if (b2 == v) {
            showNext();
        }
    }
}
