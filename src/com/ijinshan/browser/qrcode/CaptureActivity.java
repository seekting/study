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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.URIParsedResult;
import com.ijinshan.browser.qrcode.ViewfinderView.OnViewFinderListener;
import com.ijinshan.browser.qrcode.camera.CameraManager;
import com.seekting.study.QRCodeResultActivity;
import com.seekting.study.R;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback,
        OnViewFinderListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private Result savedResultToShow;
    private ViewfinderView viewfinderView;
    private Result lastResult;
    private boolean hasSurface;
    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private static final int SEARCH_REQUEST_CODE = 1;
    private int viewFinderTop = -1;

    private ParsedResult parsedResult;

    private SurfaceView surfaceView;
    private static final int FINISH = 0;
    private static final int SHOWDETAIL = 1;
    private boolean needOpenCamera = false;
    private int DELAY_START_TIME = 500;
    private int DELAY_OPEN_TIME = 800;
    public boolean canBack = false;

    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        canBack = false;
        Window window = getWindow();
        // window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        // | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.capture);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setOnViewFinderListener(this);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        decodeFormats = new ArrayList<BarcodeFormat>();
        decodeFormats.add(BarcodeFormat.QR_CODE);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());
        viewfinderView.setCameraManager(cameraManager);
        viewfinderView.setFrameTop(getViewFinderTop());

        handler = null;
        lastResult = null;

        resetStatusView();

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surfaceHolder.addCallback(this);
        }
        beepManager.updatePrefs();
        inactivityTimer.onResume();
        source = IntentSource.NONE;
        characterSet = "ISO-8859-1";
        if (needOpenCamera) {
            viewfinderView.openCameraDelay(DELAY_OPEN_TIME);
            needOpenCamera = false;
        }

    }

    @Override
    public void onBackPressed() {

        closeViewFinder(FINISH);
    }

    private void closeViewFinder(int action) {
        if (!viewfinderView.isClosing() && !viewfinderView.isOpening() && canBack) {
            viewfinderView.closeCamera(action);
        }
    }

    private int getViewFinderTop() {
        if (viewFinderTop == -1) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            viewFinderTop = (int) ((int) (Math.ceil(25 * metrics.density) / 2));

        }
        return viewFinderTop;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_REQUEST_CODE) {
            if (data != null) {
                String url = data.getAction();
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent();
                    intent.setAction(url);
                    setResult(Activity.RESULT_OK, intent);
                    super.finish();

                }
            } else {
                needOpenCamera = true;
                canBack = false;
            }
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        viewfinderView.setVisibility(View.VISIBLE);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        viewfinderView.setVisibility(View.VISIBLE);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (source == IntentSource.NATIVE_APP_INTENT) {
                    setResult(RESULT_CANCELED);
                    finish();
                    return true;
                }
                if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK)
                        && lastResult != null) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // Handle these events so they don't launch the Camera app
                return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     * 
     * @param rawResult The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        if (viewfinderView.isOpening() || viewfinderView.isClosing() || !canBack) {
            if (handler != null) {
                handler.sendEmptyMessageDelayed(R.id.restart_preview, DELAY_START_TIME);
            }
            return;
        }
        inactivityTimer.onActivity();
        lastResult = rawResult;
        beepManager.playBeepSoundAndVibrate();
        parsedResult = ResultParser.parseResult(rawResult);

        if (parsedResult != null) {
            switch (parsedResult.getType()) {
                case URI:
                    URIParsedResult uriResult = (URIParsedResult) parsedResult;
                    String uri = uriResult.getURI();
                    notifyLoadUrl(uri);
                    return;
                default:
                    closeViewFinder(SHOWDETAIL);
                    break;
            }
        }

    }

    public void notifyLoadUrl(String url) {
        Intent intent = new Intent();
        intent.setAction(url);
        setResult(Activity.RESULT_OK, intent);
        closeViewFinder(FINISH);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints,
                        characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.s_app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        surfaceView.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;

    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @Override
    public void onClose(int action) {
        canBack = false;
        surfaceView.setVisibility(View.GONE);
        switch (action) {
            case FINISH:
                super.finish();
                overridePendingTransition(R.anim.kui_activity_left_in,
                        R.anim.kui_activity_right_out);
                break;
            case SHOWDETAIL:
                Intent intent = new Intent();
                intent.setClass(this, QRCodeResultActivity.class);
                intent.putExtra(QRCodeResultActivity.RESULT, parsedResult);
                startActivityForResult(intent, SEARCH_REQUEST_CODE);
                overridePendingTransition(R.anim.kui_activity_right_in,
                        R.anim.kui_activity_left_out);
                break;
            default:
                break;
        }

    }

    @Override
    public void finish() {
        closeViewFinder(FINISH);
    }

    @Override
    public void onOpen() {
        restartPreviewAfterDelay(0);
        canBack = true;
    }
}
