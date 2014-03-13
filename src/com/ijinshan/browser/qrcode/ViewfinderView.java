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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
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

    private static final long ANIMATION_DELAY = 10L;
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;

    private CameraManager cameraManager;
    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int resultPointColor;
    private List<ResultPoint> possibleResultPoints;
    private List<ResultPoint> lastPossibleResultPoints;

    private float aroundWidth;
    private float aroundLength;
    private float arroundGap;
    private int scanningLinePosition = 0;
    private static final int MAX_POSITION = 100;
    private static final int OVER_POSITION = 150;
    private static final int SPEED = 1;
    private int frameTop;
    private Rect absloteFrame;
    public boolean drawPoints = false;
    LinearGradient linearGradient;
    private static final int SCANNING_LEFT_RIGHT_COLOR = 0x00FFFFFF;
    private static final int SCANNING_Middle_COLOR = 0xFFFFFFFF;

    private static final int AROUND_WIDTH = 5;
    private static final int AROUND_GAP = 3;
    private static final int AROUND_LENGTH = 25;
    private String trip;

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
        aroundWidth = resources.getDisplayMetrics().density * AROUND_WIDTH;
        arroundGap = resources.getDisplayMetrics().density * AROUND_GAP;
        aroundLength = resources.getDisplayMetrics().density * AROUND_LENGTH;
        absloteFrame = new Rect();

        trip = getResources().getString(R.string.qr_trip);
    }

    public void setFrameTop(int top) {
        this.frameTop = top;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
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
            if (drawPoints) {
                drawPoints(canvas, previewFrame);
            }
            drawtrips(canvas, absloteFrame);
            // Request another update at the animation interval, but only
            // repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY,
                    absloteFrame.left - POINT_SIZE,
                    absloteFrame.top - POINT_SIZE,
                    absloteFrame.right + POINT_SIZE,
                    absloteFrame.bottom + POINT_SIZE);
        }
    }

    private void drawtrips(Canvas canvas, Rect frame) {
        int x = (frame.left + frame.right) >> 1;
        paint.setTextSize(15 * getResources().getDisplayMetrics().density);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Align.CENTER);
        canvas.drawText(trip, x, frame.bottom + 100, paint);
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
        paint.setColor(Color.parseColor("#fb8c15"));
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

    /**
     * @param canvas
     * @param frame
     */
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
        if (scanningLinePosition <= MAX_POSITION) {
            float y = frame.top + 2 + scanningLinePosition / (MAX_POSITION * 1f)
                    * (frame.bottom - frame.top - 2);
            // canvas.drawLine(frame.left, y, frame.right, y, paint);
            System.out.println("y->" + y);
            canvas.drawRect(frame.left + 10, y - 1, frame.right - 10, y + 1, paint);
        }
        if (scanningLinePosition > OVER_POSITION) {
            scanningLinePosition = 0;
        } else {
            scanningLinePosition += SPEED;
        }
        paint.setShader(null);

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

}
