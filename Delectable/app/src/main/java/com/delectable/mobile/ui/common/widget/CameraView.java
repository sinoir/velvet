package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.util.CameraUtil;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraView";

    private SurfaceHolder mHolder;

    private Camera mCamera;

    private int mCameraId;

    private boolean mIsSurfaceCreated = false;

    /**
     * Uses the current width to find the appropriate scaled height to fix camera preview
     * stretching
     */
    private boolean mScaleToFitY;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsSurfaceCreated = true;
        setupCameraInHolder();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsSurfaceCreated = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Resize View to fit appropriate camera aspect ratio : Fix stretching
        int width = MeasureSpec.getSize(widthMeasureSpec);
        float aspectRatio = CameraUtil.getCameraPreviewAspectRatio(mCamera);
        if (mScaleToFitY) {
            int height = (int) (width * aspectRatio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void udpateCamera(Camera camera, int cameraId) {
        mCamera = camera;
        mCameraId = cameraId;
        setupCameraInHolder();
        requestLayout();
    }

    public void setScaleToFitY(boolean scaleToFitY) {
        this.mScaleToFitY = scaleToFitY;
    }

    private void setupCameraInHolder() {
        if (mCamera == null || !mIsSurfaceCreated) {
            return;
        }
        try {
            // Rotate the camera to the appropriate display rotation
            CameraUtil.setCameraDisplayOrientation(getContext(), mCameraId, mCamera);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            Toast.makeText(getContext(), "Failed to get camera Preview", Toast.LENGTH_SHORT);
        }
    }

    // TODO: Take Picture methods with callback
}
