package com.delectable.mobile.ui.common.fragment;

import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.CameraView;
import com.delectable.mobile.util.CameraUtil;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

public class CameraFragment extends BaseFragment {

    private static final Object sCameraOpeningSync = new Object();

    private static final String TAG = "CameraFragment";

    private CameraView mCameraView;

    private Camera mCamera;

    // TODO: Helpers to change up Cam IDs for changing front / back camera
    private int mCameraId = 0;

    private boolean mIsFlashOn = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            safeCameraOpen();
        }
    }

    @Override
    public void onPause() {
        releaseCameraAndPreview();
        super.onPause();
    }

    // TODO: Pass up Alternative Camera IDs, for example, ability to togle front facing cam.
    public void setupCameraSurface(CameraView cameraView) {
        mCameraView = cameraView;
    }

    private void safeCameraOpen() {
        if (CameraUtil.checkSystemHasFrontCameraHardware(getActivity())) {
            // Open camera on another thread for UI performance
            final Handler handler = new Handler();
            final Runnable postCameraOpenAction = new Runnable() {
                public void run() {
                    mCameraView.udpateCamera(mCamera, mCameraId);
                }
            };

            new Thread() {
                public void run() {
                    synchronized (sCameraOpeningSync) {
                        if (mCamera == null) {
                            mCamera = CameraUtil.getCameraInstance(mCameraId);
                            handler.post(postCameraOpenAction);
                        }
                    }
                }
            }.start();
        }
    }

    private void releaseCameraAndPreview() {
        mCameraView.udpateCamera(null, 0);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void takeJpegCroppedPicture(final PictureTakenCallback jpegImageCallback) {
        if (mCamera != null && jpegImageCallback != null) {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Bitmap bitmap = CameraUtil.getRotatedBitmapTakenFromCamera(
                            getActivity(), data, mCameraId);
                    Bitmap croppedBitmap = cropRotatedCapturedBitmap(bitmap);
                    jpegImageCallback.onBitmapCaptured(croppedBitmap);
                }
            });
        }
    }

    public Bitmap cropRotatedCapturedBitmap(Bitmap bitmap) {
        return bitmap;
    }

    public boolean toggleFlash() {
        if (mCamera != null && CameraUtil.checkSystemHasFlash(getActivity())) {
            Camera.Parameters p = mCamera.getParameters();
            if (mIsFlashOn) {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mIsFlashOn = false;
            } else {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mIsFlashOn = true;
            }
            mCamera.setParameters(p);
        }
        return mIsFlashOn;
    }

    protected void focusOnPoint(PointF point, RectF bounds) {
        Camera.Parameters p = mCamera.getParameters();
        int maxFocusAraes = p.getMaxNumFocusAreas();
        Camera.Area focusArea = CameraUtil.getFocusAreaFromFrameBounds(point, bounds);
        ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
        focusAreas.add(focusArea);

        if (maxFocusAraes > 0 && CameraUtil.checkSystemHasFocus(getActivity())) {
            mCamera.cancelAutoFocus();
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            p.setFocusAreas(focusAreas);
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    Log.d(TAG, "onAutoFocus() " + success);
                }
            });
        }
        mCamera.setParameters(p);
    }

    public interface PictureTakenCallback {

        public void onBitmapCaptured(Bitmap bitmap);
    }
}
