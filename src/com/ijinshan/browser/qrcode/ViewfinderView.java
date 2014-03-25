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

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;
    private static final int AROUND_COLOR = 0XFFFB8C15;
    private static final int AROUND_WIDTH = 5;
    private static final int AROUND_GAP = 3;
    private static final int AROUND_LENGTH = 25;
    private static final int TRIP_POSITION = 100;
    private static final float TRIP_TEXT_SIZE = 15;
    private static final int laserColor = 0xFFFA8C13;
    private static final int LASER_LENGTH = 20;
    private static final int LASER_WIDTH = 1;
    public static final int LASER_ANI_TIME = 1000;
    private static final long OPEN_DELAY_TIME = 500;
    public static final int OPEN_TIME = 500;
    public static final int CLOSE_TIME = 300;
    private static final int LOGO_WIDTH = 100;
    private static final int LOGO_HEIGHT = 100;
    private static final float BEGIN_LASER_ALPHA = 0.3f;
    private static final float END_LASER_ALPHA = 1f;

    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int resultPointColor;
    private List<ResultPoint> possibleResultPoints;
    private List<ResultPoint> lastPossibleResultPoints;
    private CameraManager cameraManager;
    private float aroundWidth;
    private float aroundLength;
    private float arroundGap;
    private int frameTop;
    private Rect absloteFrame;
    private boolean drawPoints = false;
    private String trip;

    private float laserLength;
    private float laserWidth;
    private Path laserPath;
    private ValueAnimator valueAnimator, openAnimator, closeAnimator;
    private float alpha = 0;
    private float shutDownpersent = 1f;
    private RectF topShut;
    private RectF bottomShut;
    private Drawable topLogoDrawable;
    private Drawable bottomLogoDrawable;
    private float logoWidth;
    private float logoHeight;

    private float titleHeight;
    private OnViewFinderListener onViewFinderListener;
    private int action;
    private boolean hasCameraManager;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every
        // time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        possibleResultPoints = new ArrayList<ResultPoint>(5);
        lastPossibleResultPoints = null;
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        aroundWidth = displayMetrics.density * AROUND_WIDTH;
        arroundGap = displayMetrics.density * AROUND_GAP;
        aroundLength = displayMetrics.density * AROUND_LENGTH;
        laserLength = displayMetrics.density * LASER_LENGTH;
        laserWidth = displayMetrics.density * LASER_WIDTH;
        logoWidth = displayMetrics.density * LOGO_WIDTH;
        logoHeight = displayMetrics.density * LOGO_HEIGHT;
        titleHeight = 0;
        absloteFrame = new Rect();
        trip = getResources().getString(R.string.qr_trip);
        topLogoDrawable = resources.getDrawable(R.drawable.qr_top_logo);
        bottomLogoDrawable = resources.getDrawable(R.drawable.qr_bottom_logo);
        topLogoDrawable.setBounds(0, 0, (int) logoWidth, (int) (logoHeight / 2));
        bottomLogoDrawable.setBounds(0, 0, (int) logoWidth, (int) (logoHeight / 2));
        laserPath = new Path();
        valueAnimator = ValueAnimator.ofFloat(BEGIN_LASER_ALPHA, END_LASER_ALPHA);
        valueAnimator.setDuration(LASER_ANI_TIME);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = Float.parseFloat(animation.getAnimatedValue().toString());
                invalidate(absloteFrame);
            }
        });
        topShut = new RectF();
        bottomShut = new RectF();
        openAnimator = ValueAnimator.ofFloat(1, 0);
        openAnimator.setDuration(OPEN_TIME);

        openAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                shutDownpersent = Float.parseFloat(animation.getAnimatedValue().toString());
                invalidate(0, (int) titleHeight, getMeasuredWidth(), getMeasuredHeight());
            }
        });
        openAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!valueAnimator.isRunning()) {
                    valueAnimator.start();
                }
                if (onViewFinderListener != null) {
                    onViewFinderListener.onOpen();
                }
            }
        });
        closeAnimator = ValueAnimator.ofFloat(0, 1);
        closeAnimator.setDuration(CLOSE_TIME);
        closeAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                shutDownpersent = Float.parseFloat(animation.getAnimatedValue().toString());
                invalidate(0, (int) titleHeight, getMeasuredWidth(), getMeasuredHeight());
            }
        });
        closeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (valueAnimator.isRunning()) {
                    valueAnimator.cancel();
                }
                if (onViewFinderListener != null) {
                    onViewFinderListener.onClose(action);
                }
            }
        });

    }

    public boolean isClosing() {
        return closeAnimator.isRunning();
    }

    public boolean isOpening() {
        return openAnimator.isRunning();
    }

    public void closeCamera(int action) {
        this.action = action;
        closeAnimator.start();
    }
    public void initCamera(){
        shutDownpersent=1f;
        postInvalidate();
    }

    public void openCamera() {
        openAnimator.setStartDelay(OPEN_DELAY_TIME);
        openAnimator.start();
    }

    public void openCameraDelay(int time) {
        openAnimator.setStartDelay(OPEN_DELAY_TIME + time);
        openAnimator.start();
    }

    public void setFrameTop(int top) {
        this.frameTop = top;
    }

    public void setCameraManager(CameraManager cameraManager) {
        hasCameraManager = cameraManager != null;
        this.cameraManager = cameraManager;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (hasCameraManager) {

            Rect frame = cameraManager.getFramingRect();

            Rect previewFrame = cameraManager.getFramingRectInPreview();
            if (frame == null || previewFrame == null) {
                return;
            }
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
            canvas.drawRect(absloteFrame.right + 1, absloteFrame.top, width,
                    absloteFrame.bottom + 1,
                    paint);
            canvas.drawRect(0, absloteFrame.bottom + 1, width, height, paint);

            drawLaser(canvas, absloteFrame);
            drawAround(canvas, absloteFrame);
            if (drawPoints) {
                drawPoints(canvas, previewFrame);

            }
            drawTrips(canvas, absloteFrame);
        }
        drawShutDown(canvas);

    }

    private void drawShutDown(Canvas canvas) {
        paint.setColor(0xff4d4d4d);
        int height = (int) (getMeasuredHeight() - titleHeight);
        int width = getMeasuredWidth();

        float distance = height / 2;
        float top = shutDownpersent * distance + titleHeight;
        float bottom = height - shutDownpersent * distance + titleHeight;
        topShut.top = titleHeight;
        topShut.left = 0;
        topShut.right = width;
        topShut.bottom = top;
        bottomShut.left = 0;
        bottomShut.right = width;
        bottomShut.bottom = getMeasuredHeight();
        bottomShut.top = bottom;
        canvas.drawRect(topShut, paint);
        canvas.drawRect(bottomShut, paint);

        float dy = top - topLogoDrawable.getBounds().height();
        float dx = (width - topLogoDrawable.getBounds().width()) / 2;
        canvas.save();
        canvas.translate(dx, dy);
        topLogoDrawable.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.translate(dx, bottom);
        bottomLogoDrawable.draw(canvas);
        canvas.restore();

    }

    private void drawTrips(Canvas canvas, Rect frame) {
        int x = (frame.left + frame.right) >> 1;
        paint.setTextSize(TRIP_TEXT_SIZE * getResources().getDisplayMetrics().density);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Align.CENTER);
        canvas.drawText(trip, x, frame.bottom + TRIP_POSITION, paint);
    }

    private void drawPoints(Canvas canvas, Rect previewFrame) {
        float scaleX = absloteFrame.width() / (float) previewFrame.width();
        float scaleY = absloteFrame.height() / (float) previewFrame.height();

        List<ResultPoint> currentPossible = possibleResultPoints;
        List<ResultPoint> currentLast = lastPossibleResultPoints;
        int frameLeft = absloteFrame.left;
        int frameTop = absloteFrame.top;
        if (currentPossible.isEmpty()) {
            lastPossibleResultPoints = null;
        } else {
            possibleResultPoints = new ArrayList<ResultPoint>(5);
            lastPossibleResultPoints = currentPossible;
            paint.setAlpha(CURRENT_POINT_OPACITY);
            paint.setColor(resultPointColor);
            synchronized (currentPossible) {
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            POINT_SIZE, paint);
                }
            }
        }
        if (currentLast != null) {
            paint.setAlpha(CURRENT_POINT_OPACITY / 2);
            paint.setColor(resultPointColor);
            synchronized (currentLast) {
                float radius = POINT_SIZE / 2.0f;
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            radius, paint);
                }
            }
        }
    }

    /**
     * @param canvas
     * @param frame
     */
    private void drawAround(Canvas canvas, Rect frame) {

        paint.setColor(AROUND_COLOR);
        // canvas.drawRect(frame, paint);
        float beginLeft = frame.left - arroundGap - aroundWidth;
        float beginTop = frame.top - arroundGap - aroundWidth;
        float endRight = frame.right + arroundGap + aroundWidth;
        float endBottom = frame.bottom + arroundGap + aroundWidth;
        // left,top -
        canvas.drawRect(beginLeft, beginTop, beginLeft + aroundLength, beginTop
                + aroundWidth,
                paint);
        // left,top |
        canvas.drawRect(beginLeft, beginTop, beginLeft + aroundWidth, beginTop + aroundLength,
                paint);
        // right,top -
        canvas.drawRect(endRight - aroundLength, beginTop, endRight, beginTop
                + aroundWidth,
                paint);
        // right,top |
        canvas.drawRect(endRight - aroundWidth, beginTop, endRight, beginTop + aroundLength,
                paint);
        // left,bottom -
        canvas.drawRect(beginLeft, endBottom - aroundWidth, beginLeft + aroundLength, endBottom
                ,
                paint);
        // left,bottom |
        canvas.drawRect(beginLeft, endBottom - aroundLength, beginLeft + aroundWidth, endBottom,
                paint);
        // right,bottom -
        canvas.drawRect(endRight - aroundLength, endBottom - aroundWidth, endRight, endBottom
                ,
                paint);
        // right,bottom |
        canvas.drawRect(endRight - aroundWidth, endBottom - aroundLength, endRight, endBottom,
                paint);
    }

    private void drawLaser(Canvas canvas, Rect frame) {
        paint.setColor(laserColor);
        int alpha = (int) (this.alpha * 255);
        paint.setAlpha(alpha);
        int middleY = frame.height() / 2 + frame.top;
        int middleX = frame.width() / 2 + frame.left;
        // + 2, paint);
        // 12 left-top
        laserPath.reset();
        laserPath.moveTo(middleX - laserLength, middleY - laserWidth);
        laserPath.lineTo(middleX - laserWidth, middleY - laserWidth);
        laserPath.lineTo(middleX - laserWidth, middleY - laserLength);
        laserPath.lineTo(middleX + laserWidth, middleY - laserLength);
        laserPath.lineTo(middleX + laserWidth, middleY - laserWidth);
        laserPath.lineTo(middleX + laserLength, middleY - laserWidth);
        laserPath.lineTo(middleX + laserLength, middleY + laserWidth);
        laserPath.lineTo(middleX + laserWidth, middleY + laserWidth);
        laserPath.lineTo(middleX + laserWidth, middleY + laserLength);
        laserPath.lineTo(middleX - laserWidth, middleY + laserLength);
        laserPath.lineTo(middleX - laserWidth, middleY + laserWidth);
        laserPath.lineTo(middleX - laserLength, middleY + laserWidth);
        laserPath.close();
        canvas.drawPath(laserPath, paint);
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     * 
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                // trim it
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }

    public void setOnViewFinderListener(OnViewFinderListener onViewFinderListener) {
        this.onViewFinderListener = onViewFinderListener;
    }

    public interface OnViewFinderListener {
        public void onClose(int action);

        public void onOpen();
    }

}
