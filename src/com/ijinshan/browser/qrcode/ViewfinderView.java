/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ijinshan.browser.qrcode;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.ijinshan.browser.qrcode.camera.CameraManager;
import com.seekting.study.R;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final long ANIMATION_DELAY = 2L;
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;

    private CameraManager cameraManager;
    private final Paint paint;
    private final Paint scanPaint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int resultPointColor;

    private float aroundWidth;
    private float aroundLength;
    private float arroundGap;
    private int scanningLinePosition = 0;
    private float scaningLine;
    private static final int MAX_POSITION = 100;
    private static final int OVER_POSITION = 150;
    private static final int SPEED = 1;
    private int frameTop;
    private Rect absloteFrame;
    private boolean drawPoints = false;
    private LinearGradient linearGradient;
    private static final int SCANNING_LEFT_RIGHT_COLOR = 0x00FA8C13;
    private static final int SCANNING_Middle_COLOR = 0xFFFA8C13;
    private static final int AROUND_COLOR = 0XFFFB8C15;
    private static final int AROUND_WIDTH = 5;
    private static final int AROUND_GAP = 3;
    private static final int AROUND_LENGTH = 25;
    private static final int SCANNING_LINE_HEIGHT = 1;
    private static final int SCANNING_LINE_PADDING = 5;
    private float scanningLineHeight;
    private float scanningLinePadding;
    private String trip;
    private static final int TRIP_POSITION = 100;
    private static final float TRIP_TEXT_SIZE = 15;
    private ValueAnimator mAnimator;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every
        // time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scanPaint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        aroundWidth = displayMetrics.density * AROUND_WIDTH;
        arroundGap = displayMetrics.density * AROUND_GAP;
        aroundLength = displayMetrics.density * AROUND_LENGTH;
        scanningLineHeight = displayMetrics.density * SCANNING_LINE_HEIGHT;
        scanningLinePadding = displayMetrics.density * SCANNING_LINE_PADDING;
        absloteFrame = new Rect();

        trip = getResources().getString(R.string.qr_trip);
        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(6000);
        mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = Float.parseFloat(animation.getAnimatedValue().toString());
                // scanningLinePosition = (int) (f * MAX_POSITION);
                scaningLine = f;

                float y = absloteFrame.top + 2 + scaningLine
                        * (absloteFrame.bottom - absloteFrame.top - 2);
                int y1 = (int) (y - scanningLineHeight - 1);
                int y2 = (int) (y + scanningLineHeight + 1);
                // canvas.drawRect(frame.left + scanningLinePadding, y -
                // scanningLineHeight, frame.right
                // - scanningLinePadding, y + scanningLineHeight, paint);
                // invalidate(absloteFrame.left - POINT_SIZE,
                // y1,
                // absloteFrame.right + POINT_SIZE,
                // y2);
                postInvalidate(absloteFrame.left - POINT_SIZE,
                        absloteFrame.top - POINT_SIZE,
                        absloteFrame.right + POINT_SIZE,
                        absloteFrame.bottom + POINT_SIZE);
            }
        });
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(-1);
        mAnimator.start();
    }

    public void setFrameTop(int top) {
        this.frameTop = top;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    Rect rr;

    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        if (rr == null) {
            Rect frame = cameraManager.getFramingRect();

            Rect previewFrame = cameraManager.getFramingRectInPreview();
            if (frame == null || previewFrame == null) {
                return;
            } else {
                rr = frame;
            }
            System.out.println("onDraw()");
        }
        Rect frame = rr;
        absloteFrame.top = frame.top - frameTop;
        absloteFrame.bottom = frame.bottom - frameTop;
        absloteFrame.left = frame.left;
        absloteFrame.right = frame.right;
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, absloteFrame.top, paint);
        canvas.drawRect(0, absloteFrame.top, absloteFrame.left, absloteFrame.bottom + 1, paint);
        canvas.drawRect(absloteFrame.right + 1, absloteFrame.top, width, absloteFrame.bottom + 1,
                paint);
        canvas.drawRect(0, absloteFrame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, absloteFrame, paint);
        } else {

            // drawLaser(canvas, absloteFrame);
            drawAround(canvas, absloteFrame);
            drawScanningLine(canvas, absloteFrame);
            // if (drawPoints) {
            // drawPoints(canvas, previewFrame);
            // }
            drawTrips(canvas, absloteFrame);
            // Request another update at the animation interval, but only
            // repaint the laser line,
            // not the entire viewfinder mask.
            // postInvalidateDelayed(ANIMATION_DELAY,
            // absloteFrame.left - POINT_SIZE,
            // absloteFrame.top - POINT_SIZE,
            // absloteFrame.right + POINT_SIZE,
            // absloteFrame.bottom + POINT_SIZE);
        }
    }

    private void drawTrips(Canvas canvas, Rect frame) {
        // int x = (frame.left + frame.right) >> 1;
        // paint.setTextSize(TRIP_TEXT_SIZE *
        // getResources().getDisplayMetrics().density);
        // paint.setColor(Color.WHITE);
        // paint.setTextAlign(Align.CENTER);
        // canvas.drawText(trip, x, frame.bottom + TRIP_POSITION, paint);
    }

    /**
     * @param canvas
     * @param frame
     */
    private void drawAround(Canvas canvas, Rect frame) {

        // paint.setColor(AROUND_COLOR);
        // // canvas.drawRect(frame, paint);
        // float beginLeft = frame.left - arroundGap - aroundWidth;
        // float beginTop = frame.top - arroundGap - aroundWidth;
        // float endRight = frame.right + arroundGap + aroundWidth;
        // float endBottom = frame.bottom + arroundGap + aroundWidth;
        // // left,top -
        // canvas.drawRect(beginLeft, beginTop, beginLeft + aroundLength,
        // beginTop
        // + aroundWidth,
        // paint);
        // // left,top |
        // canvas.drawRect(beginLeft, beginTop, beginLeft + aroundWidth,
        // beginTop + aroundLength,
        // paint);
        // // right,top -
        // canvas.drawRect(endRight - aroundLength, beginTop, endRight, beginTop
        // + aroundWidth,
        // paint);
        // // right,top |
        // canvas.drawRect(endRight - aroundWidth, beginTop, endRight, beginTop
        // + aroundLength,
        // paint);
        // // left,bottom -
        // canvas.drawRect(beginLeft, endBottom - aroundWidth, beginLeft +
        // aroundLength, endBottom
        // ,
        // paint);
        // // left,bottom |
        // canvas.drawRect(beginLeft, endBottom - aroundLength, beginLeft +
        // aroundWidth, endBottom,
        // paint);
        // // right,bottom -
        // canvas.drawRect(endRight - aroundLength, endBottom - aroundWidth,
        // endRight, endBottom
        // ,
        // paint);
        // // right,bottom |
        // canvas.drawRect(endRight - aroundWidth, endBottom - aroundLength,
        // endRight, endBottom,
        // paint);
    }

    /**
     * @param canvas
     * @param frame
     */
    private void drawScanningLine(Canvas canvas, Rect frame) {
        scanPaint.setColor(Color.WHITE);
        if (linearGradient == null) {
            linearGradient = new LinearGradient(frame.left, 0, frame.right
                    - (frame.right - frame.left) / 2, 0,
                    new int[] {
                            SCANNING_LEFT_RIGHT_COLOR, SCANNING_Middle_COLOR
                    }, null,
                    Shader.TileMode.MIRROR);
            scanPaint.setShader(linearGradient);

        }
        float y = frame.top + 2 + scaningLine
                * (frame.bottom - frame.top - 2);
        Drawable shapeDrawable = getResources().getDrawable(R.drawable.scann_line);
        shapeDrawable.setBounds(frame.left, 0, frame.right
                , 10);

        canvas.save();
        canvas.translate(0, y);
        // // shapeDrawable.draw(canvas);
        canvas.drawRect(frame.left + scanningLinePadding, -scanningLineHeight, frame.right
                - scanningLinePadding, scanningLineHeight, scanPaint);
        canvas.restore();
        // if (scanningLinePosition > OVER_POSITION) {
        // scanningLinePosition = 0;
        // } else {
        // scanningLinePosition += SPEED;
        // }

    }

    public void drawViewfinder() {

    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     * 
     * @param barcode An image of the decoded barcode.
     */

    public void addPossibleResultPoint(ResultPoint point) {

    }

}
