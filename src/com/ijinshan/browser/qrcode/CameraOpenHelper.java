
package com.ijinshan.browser.qrcode;

import com.ijinshan.browser.qrcode.camera.CameraManager;

import android.os.Handler;
import android.os.HandlerThread;

public class CameraOpenHelper {
    private static CameraOpenHelper cameraOpenHelper;

    private static final String TAG = "Helper";
    private HandlerThread handlerThread;
    private Handler handler;
    private OnCameraListener onCameraListener;
    private CameraStatus cameraStatus;
    private CaptureActivity captureActivity;
    private ActivityStatus activityStatus;

    private CameraOpenHelper() {
        handlerThread = new HandlerThread("camera-onpen-helper");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        setCameraStatus(CameraStatus.PrepareOpen);
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

    public void onCreate(CaptureActivity captureActivity) {
        activityStatus = ActivityStatus.OnCreate;
        this.captureActivity = captureActivity;
        setOnCameraListener(captureActivity);
    }

    public void onDestory(CaptureActivity captureActivity) {
        activityStatus = ActivityStatus.OnDestory;
        if (this.captureActivity == captureActivity)
        {
            this.captureActivity = null;
            onCameraListener = null;
        }
    }

    public void onResume() {
        System.out.println("onResume");

        activityStatus = ActivityStatus.OnResume;
        if (getCameraStatus() == CameraStatus.PrepareOpen) {
            System.out.println(TAG + "onResume有效");
            System.out.println("ca openCamera onResume");
            openCamera();
        }
    }

    public void onPause() {
        activityStatus = ActivityStatus.OnPause;
        if (getCameraStatus() == CameraStatus.Opened) {
            closeCamera();
        }

    }

    public void openCamera() {
        setCameraStatus(CameraStatus.Opening);
        handler.post(new Runnable() {

            @Override
            public void run() {

                doOpenCamera();
                if (activityStatus == ActivityStatus.OnPause) {
                    closeCamera();
                }
            }
        });
    }

    private void doOpenCamera() {

        if (captureActivity != null) {
            captureActivity.onCameraOpen();
            setCameraStatus(CameraStatus.Opened);
        }

    }

    public synchronized void setCameraStatus(CameraStatus status) {
        cameraStatus = status;
    }

    private synchronized CameraStatus getCameraStatus() {
        return cameraStatus;
    }

    private void doCloseCamera() {

        if (cameraManager != null) {
            System.out.println("doCloseDriver");
            cameraManager.stopPreview();
            cameraManager.closeDriver();
            setCameraStatus(CameraStatus.PrepareOpen);
        } else {
            System.out.println("!!!!cameraManager=null");
        }
        if (captureActivity != null) {
            captureActivity.onCameraClose();
        }

    }

    public void closeCamera() {
        setCameraStatus(CameraStatus.Closing);
        handler.post(new Runnable() {

            @Override
            public void run() {
                doCloseCamera();
                if (activityStatus == ActivityStatus.OnResume) {
                    System.out.println("ca openCamera closeCamera");

                    openCamera();
                }
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
        public void onCameraClose();

        public void onCameraOpen();

    }

    public OnCameraListener getOnCameraListener() {
        return onCameraListener;
    }

    public void setOnCameraListener(OnCameraListener onCameraListener) {
        this.onCameraListener = onCameraListener;
    }

    /**
     * PrepareOpen->opening->opened->Closing->PrepareOpen
     */
    public enum CameraStatus {
        /**
         * 可以开启
         */
        PrepareOpen,
        /**
         * 开启中
         */
        Opening,
        /**
         * 已经是开启状态
         */
        Opened,
        /**
         * 关闭中
         */
        Closing

    }

    private enum ActivityStatus {
        OnCreate, OnResume, OnPause, OnDestory

    }

    CameraManager cameraManager;

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }
}
