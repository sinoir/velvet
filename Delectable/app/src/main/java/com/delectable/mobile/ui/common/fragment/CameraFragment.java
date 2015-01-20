package com.delectable.mobile.ui.common.fragment;

import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.CameraView;
import com.delectable.mobile.util.CameraUtil;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.AsyncTask;
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
        if (CameraUtil.checkSystemHasCameraHardware()) {
            // Open camera on another thread for UI performance
            final Handler handler = new Handler();
            final Runnable postCameraOpenAction = new Runnable() {
                public void run() {
                    mCameraView.udpateCamera(mCamera, mCameraId);
                    // If Flash was already on, such as we tapped home and resumed the fragment.
                    if (mIsFlashOn) {
                        mIsFlashOn = false;
                        toggleFlash();
                    } else {
                        // Turn flash off if it was on and we went "back" from next screen.
                        mIsFlashOn = true;
                        toggleFlash();
                    }
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

    protected void releaseCameraAndPreview() {
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
                public void onPictureTaken(final byte[] data, Camera camera) {
                    TransformTask task = new TransformTask();
                    task.jpegImageCallback = jpegImageCallback;
                    task.data = data;
                    task.execute();
                }
            });
        }
    }

    public boolean toggleFlash() {
        if (mCamera != null && CameraUtil.checkSystemHasFlash()) {
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
        // Don't focus yet if the camera is not being previewed, otherwise it crashes
        if (mCamera == null || !mCameraView.isSurfaceCreated()) {
            Log.e(TAG, "Tried Focusing on Point when Preview hasn't started yet");
            return;
        }
        Camera.Parameters p = mCamera.getParameters();
        int maxFocusAraes = p.getMaxNumFocusAreas();
        Camera.Area focusArea = CameraUtil.getFocusAreaFromFrameBounds(point, bounds);
        ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
        focusAreas.add(focusArea);

        if (maxFocusAraes > 0 && CameraUtil.checkSystemHasFocus()) {
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

    public class TransformTask extends AsyncTask<Void, Void, Bitmap> {

        byte[] data;

        PictureTakenCallback jpegImageCallback;

        @Override
        protected Bitmap doInBackground(Void... params) {
            return CameraUtil.rotateScaleAndCropImage(data, mCameraId);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (jpegImageCallback != null) {
                jpegImageCallback.onBitmapCaptured(bitmap);
            }
        }
    }
}
