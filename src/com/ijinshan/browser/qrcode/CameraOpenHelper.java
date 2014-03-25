
package com.ijinshan.browser.qrcode;

import android.os.Handler;
import android.os.HandlerThread;

public class CameraOpenHelper {
    private static CameraOpenHelper cameraOpenHelper;

    private HandlerThread handlerThread;
    private Handler handler;
    private OnCameraListener onCameraListener;
    private CameraStatus cameraStatus;
    private CaptureActivity captureActivity;
    private ActivityStatus activityStatus;

    private CameraOpenHelper() {
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public static CameraOpenHelper getInstance() {
        if (cameraOpenHelper == null) {
            synchronized (CameraOpenHelper.class) {
                if (cameraOpenHelper == null) {
                    cameraOpenHelper = new CameraOpenHelper();
                }
            }
        }
        return cameraOpenHelper;
    }

    public void regist(CaptureActivity captureActivity) {
        this.captureActivity = captureActivity;
        activityStatus = ActivityStatus.OnCreate;
    }

    public void unRegist(CaptureActivity captureActivity) {
        if (this.captureActivity == captureActivity)
        {
            this.captureActivity = null;
        }
        activityStatus = ActivityStatus.OnDestory;
    }

    public void onResume() {
        activityStatus = ActivityStatus.OnResume;
    }

    public void onPause() {
        activityStatus = ActivityStatus.OnPause;

    }

    public void openCamera() {

        handler.post(new Runnable() {

            @Override
            public void run() {

                tryOpenCamera();
                if (activityStatus == ActivityStatus.OnResume) {
                    // captureActivity

                }
            }
        });
    }

    private void tryOpenCamera() {
        if (cameraStatus == CameraStatus.PrepareOpen) {
            // TODO doOpen
            cameraStatus = CameraStatus.Opened;
        } else {

        }
    }

    private void tryCloseCamera() {
        cameraStatus = CameraStatus.Closing;

        cameraStatus = CameraStatus.PrepareOpen;
    }

    public void closeCamera() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                tryCloseCamera();
            }
        });
    }

    public void destory() {
        synchronized (CameraOpenHelper.class) {
            if (handlerThread != null) {
                handlerThread.quit();
                handlerThread = null;
                handler = null;
                cameraOpenHelper = null;
            }

        }
    }

    public static interface OnCameraListener {
        public void onCameraClosed();

    }

    public OnCameraListener getOnCameraListener() {
        return onCameraListener;
    }

    public void setOnCameraListener(OnCameraListener onCameraListener) {
        this.onCameraListener = onCameraListener;
    }

    private enum CameraStatus {
        PrepareOpen, Opening, Opened, Closing

    }

    private enum ActivityStatus {
        OnCreate, OnResume, OnPause, OnDestory

    }
}
