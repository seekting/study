
package com.seekting.study.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class ScalingView extends View {

    private Rect absloteFrame;

    private Paint paint = new Paint();
    private LinearGradient linearGradient;
    private static final int SCANNING_LEFT_RIGHT_COLOR = 0x00FA8C13;
    private static final int SCANNING_Middle_COLOR = 0xFFFA8C13;
    private float scaningLine;
    private static final int SCANNING_LINE_HEIGHT = 1;
    private static final int SCANNING_LINE_PADDING = 5;
    private float scanningLineHeight;
    private float scanningLinePadding;
    private ValueAnimator mAnimator;
    private static final int POINT_SIZE = 6;

    public ScalingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        absloteFrame = new Rect();
        absloteFrame.top = 100;
        absloteFrame.bottom = 500;
        absloteFrame.left = 100;
        absloteFrame.right = 500;
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        scanningLineHeight = displayMetrics.density * SCANNING_LINE_HEIGHT;
        scanningLinePadding = displayMetrics.density * SCANNING_LINE_PADDING;
        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(6000);
        mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = Float.parseFloat(animation.getAnimatedValue().toString());
                // scanningLinePosition = (int) (f * MAX_POSITION);
                scaningLine = f;
                // postInvalidate(absloteFrame.left - POINT_SIZE,
                // absloteFrame.top - POINT_SIZE,
                // absloteFrame.right + POINT_SIZE,
                // absloteFrame.bottom + POINT_SIZE);
                invalidate(absloteFrame.left - POINT_SIZE,
                        absloteFrame.top - POINT_SIZE,
                        absloteFrame.right + POINT_SIZE,
                        absloteFrame.bottom + POINT_SIZE);
            }
        });
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(-1);
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        canvas.drawRect(0, 0, width, absloteFrame.top, paint);
        canvas.drawRect(0, absloteFrame.top, absloteFrame.left, absloteFrame.bottom + 1, paint);
        canvas.drawRect(absloteFrame.right + 1, absloteFrame.top, width, absloteFrame.bottom + 1,
                paint);
        canvas.drawRect(0, absloteFrame.bottom + 1, width, height, paint);

        // drawLaser(canvas, absloteFrame);
        drawScanningLine(canvas, absloteFrame);

    }

    private void drawScanningLine(Canvas canvas, Rect frame) {
        if (linearGradient == null) {
            linearGradient = new LinearGradient(frame.left, 0, frame.right
                    - (frame.right - frame.left) / 2, 0,
                    new int[] {
                            SCANNING_LEFT_RIGHT_COLOR, SCANNING_Middle_COLOR
                    }, null,
                    Shader.TileMode.MIRROR);

        }

        paint.setShader(linearGradient);
        float y = frame.top + 2 + scaningLine
                * (frame.bottom - frame.top - 2);
        canvas.drawRect(frame.left + scanningLinePadding, y - scanningLineHeight, frame.right
                - scanningLinePadding, y + scanningLineHeight, paint);
        // if (scanningLinePosition > OVER_POSITION) {
        // scanningLinePosition = 0;
        // } else {
        // scanningLinePosition += SPEED;
        // }
        paint.setShader(null);

    }
}
